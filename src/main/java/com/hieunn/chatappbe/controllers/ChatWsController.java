package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.requests.SendMessageRequest;
import com.hieunn.chatappbe.dtos.requests.TypingRequest;
import com.hieunn.chatappbe.services.MessageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatWsController {
    MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload @Valid SendMessageRequest request, Principal principal) {
        Long senderId = Long.parseLong(principal.getName());
        messageService.sendMessage(senderId, request, List.of());
    }

    @MessageMapping("/chat.typing")
    public void typing(@Payload @Valid TypingRequest request, Principal principal) {
        Long senderId = Long.parseLong(principal.getName());
        messageService.sendTypingStatus(senderId, request);
    }
}
