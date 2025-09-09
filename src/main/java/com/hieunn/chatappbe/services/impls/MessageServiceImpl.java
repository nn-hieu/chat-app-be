package com.hieunn.chatappbe.services.impls;

import com.hieunn.chatappbe.dtos.requests.SendMessageRequest;
import com.hieunn.chatappbe.dtos.requests.TypingRequest;
import com.hieunn.chatappbe.dtos.responses.MessageDTO;
import com.hieunn.chatappbe.dtos.responses.TypingDTO;
import com.hieunn.chatappbe.entities.Conversation;
import com.hieunn.chatappbe.entities.Message;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.mappers.MessageMapper;
import com.hieunn.chatappbe.repositories.ConversationRepository;
import com.hieunn.chatappbe.repositories.MessageRepository;
import com.hieunn.chatappbe.repositories.UserRepository;
import com.hieunn.chatappbe.services.MessageService;
import com.hieunn.chatappbe.utils.WebSocketUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    MessageRepository messageRepository;
    UserRepository userRepository;
    ConversationRepository conversationRepository;
    WebSocketUtil webSocketUtil;
    MessageMapper messageMapper;

    @Override
    @Transactional
    public void sendMessage(Long senderId, SendMessageRequest request) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found with id: " + senderId));

        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found with id: " + request.getConversationId()));

        if (!conversation.getParticipants().contains(sender)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is not a participant of this conversation");
        }

        Message message = Message.builder()
                .sender(sender)
                .conversation(conversation)
                .content(request.getContent())
                .isRead(false)
                .build();

        Message savedMessage = messageRepository.save(message);

        webSocketUtil.notifyUsers(
                conversation
                        .getParticipants()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList()),
                "/queue/messages",
                messageMapper.toMessageDTO(savedMessage)
        );
    }

    @Override
    @Transactional
    public void sendTypingStatus(Long senderId, TypingRequest request) {
        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found with id: " + request.getConversationId()));

        TypingDTO dto = TypingDTO
                .builder()
                .isTyping(request.getIsTyping())
                .conversationId(request.getConversationId())
                .senderId(senderId)
                .build();

        webSocketUtil.notifyUsers(
                conversation
                        .getParticipants()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList()),
                "/queue/typing",
                dto
        );
    }

    @Override
    public List<MessageDTO> findMessagesOfConversation(Long conversationId, int page, int size) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversation not found with id: " + conversationId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Message> messages = messageRepository.findByConversation(conversation, pageable);

        List<MessageDTO> messageDTOs = messages.getContent()
                .stream()
                .map(messageMapper::toMessageDTO)
                .collect(Collectors.toList());

        Collections.reverse(messageDTOs);

        return messageDTOs;
    }
}
