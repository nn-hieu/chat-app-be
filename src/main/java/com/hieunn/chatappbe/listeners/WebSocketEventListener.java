package com.hieunn.chatappbe.listeners;

import com.hieunn.chatappbe.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
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
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        userService.updateOnlineStatus(Long.parseLong(userId), true);

        messagingTemplate.convertAndSend("/topic/user.onlineStatus", Map.of(
                "userId", Long.parseLong(userId),
                "isOnline", true
        ));
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        userService.updateOnlineStatus(Long.parseLong(userId), false);

        messagingTemplate.convertAndSend("/topic/user.onlineStatus", Map.of(
                "userId", Long.parseLong(userId),
                "isOnline", false
        ));
    }
}
