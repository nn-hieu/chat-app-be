package com.hieunn.chatappbe.dtos.responses;

import com.hieunn.chatappbe.entities.enums.ConversationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationDTO {
    Long id;
    ConversationType type;
    String name;
    Set<UserDTO> participants;
    UserDTO createdBy;
    String lastMessage;
    Long lastSenderId;
    LocalDateTime lastMessageTime;
    Long unreadCount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
