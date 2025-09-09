package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.responses.ApiResponse;
import com.hieunn.chatappbe.dtos.responses.MessageDTO;
import com.hieunn.chatappbe.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageController {
    MessageService messageService;

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> findMessagesOfConversation(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<MessageDTO> messages = messageService.findMessagesOfConversation(conversationId, page, size);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }
}
