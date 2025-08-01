package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.requests.SendMessageRequest;
import com.hieunn.chatappbe.dtos.responses.MessageDTO;
import com.hieunn.chatappbe.services.MessageService;
import com.hieunn.chatappbe.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketController {
    MessageService messageService;
    UserService userService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
        try {
            // Giả sử senderId được truyền trong request hoặc lấy từ principal
            Long senderId = Long.parseLong(principal.getName()); // Cần implement authentication

            MessageDTO message = messageService.sendMessage(senderId, request);

            // Gửi tin nhắn đến người nhận
            messagingTemplate.convertAndSendToUser(
                    message.getReceiverId().toString(),
                    "/queue/messages",
                    message
            );

            // Gửi tin nhắn về cho người gửi để confirm
            messagingTemplate.convertAndSendToUser(
                    message.getSenderId().toString(),
                    "/queue/messages",
                    message
            );

        } catch (Exception e) {
            // Handle error
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    "/queue/errors",
                    "Failed to send message: " + e.getMessage()
            );
        }
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload Map<String, Object> payload,
                        SimpMessageHeaderAccessor headerAccessor) {
        Long userId = Long.parseLong(payload.get("userId").toString());
        String username = payload.get("username").toString();

        // Lưu user session
        headerAccessor.getSessionAttributes().put("userId", userId);
        headerAccessor.getSessionAttributes().put("username", username);

        // Cập nhật trạng thái online
        userService.updateOnlineStatus(userId, true);

        // Thông báo user online
        messagingTemplate.convertAndSend("/topic/user.online", Map.of(
                "userId", userId,
                "username", username,
                "online", true
        ));
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        // User connected
        System.out.println("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (userId != null) {
            // Cập nhật trạng thái offline
            userService.updateOnlineStatus(userId, false);

            // Thông báo user offline
            messagingTemplate.convertAndSend("/topic/user.offline", Map.of(
                    "userId", userId,
                    "username", username,
                    "online", false
            ));
        }
    }
}
