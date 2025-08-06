package com.hieunn.chatappbe.listeners;

import com.hieunn.chatappbe.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketEventListener {
    UserService userService;
    SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String destination = headerAccessor.getDestination();

        if ("/topic/user.online".equals(destination)) {
            String userId = (String) headerAccessor.getSessionAttributes().get("userId");
            String username = (String) headerAccessor.getSessionAttributes().get("username");

            if (userId != null) {
                userService.updateOnlineStatus(Long.parseLong(userId), true);

                messagingTemplate.convertAndSend("/topic/user.online", Map.of(
                        "userId", userId,
                        "username", username,
                        "online", true
                ));
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (userId != null) {
            userService.updateOnlineStatus(Long.parseLong(userId), false);

            messagingTemplate.convertAndSend("/topic/user.offline", Map.of(
                    "userId", userId,
                    "username", username,
                    "online", false
            ));
        }
    }
}
