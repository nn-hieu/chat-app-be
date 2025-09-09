package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.responses.ConversationDTO;

import java.util.List;

public interface ConversationService {
    ConversationDTO findOrCreateSingleConversation(Long currentUserId, Long otherUserId);

    void markConversationAsRead(Long conversationId, Long userId);

    List<ConversationDTO> findAllConversationsOfUser(Long userId);
}
