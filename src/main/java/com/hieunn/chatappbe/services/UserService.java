package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.responses.FriendDTO;
import com.hieunn.chatappbe.dtos.responses.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO findById(Long id);

    void updateOnlineStatus(Long userId, boolean online);

    List<FriendDTO> findFriends(Long userId);
}
