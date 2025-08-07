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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.Map;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatController {
    MessageService messageService;
    UserService userService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
        try {
            Long senderId = Long.parseLong(principal.getName());

            MessageDTO message = messageService.sendMessage(senderId, request);

            // Send message to receiver
            messagingTemplate.convertAndSendToUser(
                    message.getReceiverId().toString(),
                    "/queue/messages",
                    message
            );

            // Send message to sender
            messagingTemplate.convertAndSendToUser(
                    message.getSenderId().toString(),
                    "/queue/messages",
                    message
            );

        } catch (Exception e) {
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    "/queue/errors",
                    "Failed to send message: " + e.getMessage()
            );
        }
    }
}
