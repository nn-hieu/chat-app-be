package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.requests.UserUpdateRequest;
import com.hieunn.chatappbe.dtos.responses.ApiResponse;
import com.hieunn.chatappbe.dtos.responses.FriendDTO;
import com.hieunn.chatappbe.dtos.responses.UserDTO;
import com.hieunn.chatappbe.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> findUserById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<ApiResponse<List<FriendDTO>>> findFriends(@PathVariable Long id) {
        List<FriendDTO> friends = userService.findFriends(id);
        return ResponseEntity.ok(ApiResponse.success(friends));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String fullName
    ) {
        List<UserDTO> users = userService.search(username, email, fullName);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @PathVariable Long id,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar,
            @RequestPart("request") UserUpdateRequest request
    ) {
        UserDTO user = userService.updateProfile(id, request, avatar);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
