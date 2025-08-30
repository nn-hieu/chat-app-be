package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.requests.SendMessageRequest;
import com.hieunn.chatappbe.dtos.requests.TypingRequest;
import com.hieunn.chatappbe.dtos.responses.MessageDTO;
import com.hieunn.chatappbe.dtos.responses.TypingDTO;
import com.hieunn.chatappbe.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatController {
    MessageService messageService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
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
    }

    @MessageMapping("/chat.typing")
    public void typing(@Payload TypingRequest request, Principal principal) {
        TypingDTO typingDTO = TypingDTO.builder()
                .isTyping(request.isTyping())
                .receiverId(request.getReceiverId())
                .senderId(Long.parseLong(principal.getName()))
                .build();

        messagingTemplate.convertAndSendToUser(
                request.getReceiverId().toString(),
                "/queue/typing",
                typingDTO
        );
    }
}
