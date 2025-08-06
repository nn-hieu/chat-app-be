package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.responses.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO findUserById(Long id);

    void updateOnlineStatus(Long userId, boolean online);
}
