package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.requests.UserUpdateRequest;
import com.hieunn.chatappbe.dtos.responses.FriendDTO;
import com.hieunn.chatappbe.dtos.responses.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO findById(Long id);

    void updateOnlineStatus(Long userId, boolean online);

    List<UserDTO> findFriends(Long userId);

    Page<UserDTO> findFriends(Long userId, Pageable pageable);

    long countFriends(Long userId);

    List<UserDTO> findMutualFriends(Long userId1, Long userId2);

    List<UserDTO> search(String username, String email, String fullName);

    UserDTO updateProfile(Long userId, UserUpdateRequest request, MultipartFile avatar);
}
