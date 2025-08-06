package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.responses.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO findUserById(Long id);

    UserDTO findUserByUsername(String username);

    List<UserDTO> searchUsers(String query);

    UserDTO updateOnlineStatus(Long userId, boolean online);

    UserDTO updateUserProfile(Long userId, String firstName, String lastName, String avatar);
}
