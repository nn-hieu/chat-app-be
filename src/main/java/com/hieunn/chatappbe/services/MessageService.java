package com.hieunn.chatappbe.services;

import com.hieunn.chatappbe.dtos.requests.SendMessageRequest;
import com.hieunn.chatappbe.dtos.requests.TypingRequest;
import com.hieunn.chatappbe.dtos.responses.MessageDTO;

import java.util.List;

public interface MessageService {
    void sendMessage(Long senderId, SendMessageRequest request);

    void sendTypingStatus(Long senderId, TypingRequest request);

    List<MessageDTO> findMessagesOfConversation(Long conversationId, int page, int size);
}
