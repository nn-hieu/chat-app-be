package com.hieunn.chatappbe.services.impls;

import com.hieunn.chatappbe.dtos.responses.ConversationDTO;
import com.hieunn.chatappbe.entities.Conversation;
import com.hieunn.chatappbe.entities.Message;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.entities.enums.ConversationType;
import com.hieunn.chatappbe.mappers.ConversationMapper;
import com.hieunn.chatappbe.repositories.ConversationRepository;
import com.hieunn.chatappbe.repositories.MessageRepository;
import com.hieunn.chatappbe.repositories.UserRepository;
import com.hieunn.chatappbe.services.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    UserRepository userRepository;
    ConversationRepository conversationRepository;
    ConversationMapper conversationMapper;
    MessageRepository messageRepository;

    @Override
    @Transactional
    public ConversationDTO findOrCreateSingleConversation(Long currentUserId, Long otherUserId) {
        Set<Long> userIds = new HashSet<>(Arrays.asList(currentUserId, otherUserId));

        Conversation existing = conversationRepository
                .findConversationByTypeAndParticipants(ConversationType.SINGLE, userIds, userIds.size());

        if (existing != null) {
            return toConversationDTOWithExtras(existing, currentUserId);
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + currentUserId));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + otherUserId));

        Conversation conversation = Conversation.builder()
                .type(ConversationType.SINGLE)
                .participants(new HashSet<>(Arrays.asList(currentUser, otherUser)))
                .build();

        conversationRepository.save(conversation);

        return toConversationDTOWithExtras(conversation, currentUserId);
    }

    @Override
    @Transactional
    public void markConversationAsRead(Long conversationId, Long userId) {
        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found with id: " + conversationId
                ));

        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + userId
                ));

        messageRepository.markMessagesAsRead(conversationId, userId);
    }

    @Override
    public List<ConversationDTO> findAllConversationsOfUser(Long userId) {
        List<Conversation> conversations = conversationRepository.findByParticipants_Id(userId);

        return conversations.stream()
                .map(conversation -> toConversationDTOWithExtras(conversation, userId))
                .sorted(Comparator.comparing(
                        ConversationDTO::getLastMessageTime,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .collect(Collectors.toList());
    }

    private ConversationDTO toConversationDTOWithExtras(Conversation conversation, Long currentUserId) {
        ConversationDTO dto = conversationMapper.toConversationDTO(conversation);

        Optional<Message> lastMessage = messageRepository
                .findTopByConversation_IdOrderByCreatedAtDesc(conversation.getId());

        lastMessage.ifPresent(msg -> {
            dto.setLastMessage(msg.getContent());
            dto.setLastSenderId(msg.getSender().getId());
            dto.setLastMessageTime(msg.getCreatedAt());
        });

        long unreadCount = messageRepository
                .countByConversation_IdAndIsReadFalseAndSender_IdNot(conversation.getId(), currentUserId);
        dto.setUnreadCount(unreadCount);

        return dto;
    }
}
