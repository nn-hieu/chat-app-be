package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.responses.ApiResponse;
import com.hieunn.chatappbe.dtos.responses.FriendRequestDTO;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.services.FriendRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend-requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FriendRequestController {
    FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<FriendRequestDTO>> send(@RequestParam Long receiverId, @AuthenticationPrincipal User sender) {
        FriendRequestDTO requestDTO = friendRequestService.send(sender.getId(), receiverId);

        return ResponseEntity.ok(ApiResponse.success(
                        HttpStatus.CREATED,
                        requestDTO
                )
        );
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<FriendRequestDTO>> address(
            @RequestParam Long id,
            @RequestParam(required = false) Boolean isAccepted,
            @AuthenticationPrincipal User user
    ) {
        FriendRequestDTO requestDTO = friendRequestService.address(id, isAccepted, user.getId());

        return ResponseEntity.ok(ApiResponse.success(
                        HttpStatus.OK,
                        requestDTO
                )
        );
    }
}
