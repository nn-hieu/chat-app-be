package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.responses.FriendRequestDTO;

import java.util.List;

public interface FriendRequestService {
    FriendRequestDTO send(Long senderId, Long receiverId);

    FriendRequestDTO address(Long id, Boolean isAccepted, Long userId);

    List<FriendRequestDTO> findSentRequests(Long userId);

    List<FriendRequestDTO> findReceivedRequests(Long userId);
}
