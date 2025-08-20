package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.responses.ApiResponse;
import com.hieunn.chatappbe.dtos.responses.MessageDTO;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageController {
    MessageService messageService;

    @GetMapping("/conversation")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getConversation(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<MessageDTO> messages = messageService.getConversation(user1Id, user2Id, page, size);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    @PatchMapping("/conversation")
    public ResponseEntity<ApiResponse<Void>> markConversationAsRead(
            @RequestParam Long currentUserId,
            @RequestParam Long otherUserId) {
        messageService.markConversationAsRead(currentUserId, otherUserId);
        return ResponseEntity.ok().build();
    }
}
