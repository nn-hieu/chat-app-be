package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.responses.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long id);

    UserDTO getUserByUsername(String username);

    List<UserDTO> searchUsers(String query);

    UserDTO updateOnlineStatus(Long userId, boolean online);

    UserDTO updateUserProfile(Long userId, String firstName, String lastName, String avatar);
}
