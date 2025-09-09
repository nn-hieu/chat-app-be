package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.responses.ApiResponse;
import com.hieunn.chatappbe.dtos.responses.ConversationDTO;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.services.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ConversationController {
    ConversationService conversationService;

    @PostMapping("/single")
    public ResponseEntity<ApiResponse<ConversationDTO>> findOrCreateSingleConversation(
            @RequestParam Long friendId,
            @AuthenticationPrincipal User user
    ) {
        ConversationDTO conversation = conversationService.findOrCreateSingleConversation(
                user.getId(),
                friendId
        );
        return ResponseEntity.ok(ApiResponse.success(conversation));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> markConversationAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        conversationService.markConversationAsRead(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK,
                null,
                "Successful"
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConversationDTO>>> findAllConversationsOfUser(
            @AuthenticationPrincipal User user
    ) {
        List<ConversationDTO> conversation = conversationService.findAllConversationsOfUser(user.getId());
        return ResponseEntity.ok(ApiResponse.success(conversation));
    }
}
