package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.responses.UserDTO;
import com.hieunn.chatappbe.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.findUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        try {
            UserDTO user = userService.findUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query) {
        try {
            List<UserDTO> users = userService.searchUsers(query);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/online-status")
    public ResponseEntity<UserDTO> updateOnlineStatus(
            @PathVariable Long id,
            @RequestParam boolean online) {
        try {
            UserDTO user = userService.updateOnlineStatus(id, online);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserDTO> updateProfile(
            @PathVariable Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String avatar) {
        try {
            UserDTO user = userService.updateUserProfile(id, firstName, lastName, avatar);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
