package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.requests.UserUpdateRequest;
import com.hieunn.chatappbe.dtos.responses.ApiResponse;
import com.hieunn.chatappbe.dtos.responses.UserDTO;
import com.hieunn.chatappbe.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<ApiResponse<List<UserDTO>>> findFriends(@PathVariable Long id) {
        List<UserDTO> friends = userService.findFriends(id);
        return ResponseEntity.ok(ApiResponse.success(friends));
    }

    @GetMapping("/{id}/friends/paginated")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> findFriendsWithPagination(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<UserDTO> friends = userService.findFriends(id, pageable);

        return ResponseEntity.ok(ApiResponse.success(friends));
    }

    @GetMapping("/{userId}/friends/count")
    public ResponseEntity<ApiResponse<Long>> countFriends(@PathVariable Long userId) {
        long count = userService.countFriends(userId);

        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @GetMapping("/{userId}/friends/mutual")
    public ResponseEntity<ApiResponse<List<UserDTO>>> findMutualFriends(
            @PathVariable Long userId,
            @RequestParam Long otherUserId) {

        List<UserDTO> mutualFriends = userService.findMutualFriends(userId, otherUserId);

        return ResponseEntity.ok(ApiResponse.success(mutualFriends));
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
