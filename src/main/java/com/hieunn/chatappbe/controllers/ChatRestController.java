package com.hieunn.chatappbe.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieunn.chatappbe.dtos.requests.SendMessageRequest;
import com.hieunn.chatappbe.dtos.responses.ApiResponse;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/chat")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatRestController {
    MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> sendMessage(
            @RequestPart String request,
            @RequestPart List<MultipartFile> files,
            @AuthenticationPrincipal User user
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SendMessageRequest request2 = objectMapper.readValue(request, SendMessageRequest.class);

        messageService.sendMessage(user.getId(), request2, files);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK,
                null,
                "Send message successfully"
        ));
    }
}
