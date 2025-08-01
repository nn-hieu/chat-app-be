package com.hieunn.chatappbe.services.impls;

import com.hieunn.chatappbe.dtos.requests.SendMessageRequest;
import com.hieunn.chatappbe.dtos.responses.MessageDTO;
import com.hieunn.chatappbe.entities.Message;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.repositories.MessageRepository;
import com.hieunn.chatappbe.repositories.UserRepository;
import com.hieunn.chatappbe.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public MessageDTO sendMessage(Long senderId, SendMessageRequest request) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .type(request.getType())
                .isRead(false)
                .build();

        Message savedMessage = messageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    @Override
    public List<MessageDTO> getConversation(Long user1Id, Long user2Id, int page, int size) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User 1 not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User 2 not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Message> messages = messageRepository
                .findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtDesc(
                        user1, user2, user2, user1, pageable);

        List<MessageDTO> messageDTOs = messages.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        Collections.reverse(messageDTOs);
        return messageDTOs;
    }

    @Override
    @Transactional
    public void markAsRead(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to mark this message as read");
        }

        message.setRead(true);
        message.setReadAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Override
    @Transactional
    public void markConversationAsRead(Long currentUserId, Long otherUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("Other user not found"));

        List<Message> unreadMessages = messageRepository.findByReceiverAndIsReadFalse(currentUser);
        unreadMessages.stream()
                .filter(msg -> msg.getSender().getId().equals(otherUserId))
                .forEach(msg -> {
                    msg.setRead(true);
                    msg.setReadAt(LocalDateTime.now());
                });

        messageRepository.saveAll(unreadMessages);
    }

    @Override
    public long getUnreadMessageCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return messageRepository.countByReceiverAndIsReadFalse(user);
    }

    private MessageDTO convertToDTO(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .senderFullName(message.getSender().getFullName())
                .senderAvatar(message.getSender().getAvatar())
                .receiverId(message.getReceiver().getId())
                .receiverUsername(message.getReceiver().getUsername())
                .receiverFullName(message.getReceiver().getFullName())
                .receiverAvatar(message.getReceiver().getAvatar())
                .content(message.getContent())
                .type(message.getType())
                .isRead(message.isRead())
                .createdAt(message.getCreatedAt())
                .readAt(message.getReadAt())
                .build();
    }
}
