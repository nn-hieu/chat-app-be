package com.hieunn.chatappbe.controllers;

import com.hieunn.chatappbe.dtos.requests.LoginRequest;
import com.hieunn.chatappbe.dtos.responses.ApiResponse;
import com.hieunn.chatappbe.dtos.responses.LoginResponse;
import com.hieunn.chatappbe.services.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(authService.login(request.getUsername(), request.getPassword()))
        );
    }
}
