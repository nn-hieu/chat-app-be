package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.responses.LoginResponse;

public interface AuthService {
    LoginResponse login(String username, String password);
}
