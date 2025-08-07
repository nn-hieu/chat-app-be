package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.responses.FriendRequestDTO;

public interface FriendRequestService {
    FriendRequestDTO send(Long senderId, Long receiverId);

    FriendRequestDTO address(Long id, Boolean isAccepted, Long userId);
}
