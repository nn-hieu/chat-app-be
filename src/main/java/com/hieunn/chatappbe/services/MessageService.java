package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.requests.SendMessageRequest;
import com.hieunn.chatappbe.dtos.responses.MessageDTO;

import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(Long senderId, SendMessageRequest request);

    List<MessageDTO> getConversation(Long user1Id, Long user2Id, int page, int size);

    void markConversationAsRead(Long currentUserId, Long otherUserId);
}
