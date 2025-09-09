package com.hieunn.chatappbe.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDTO {
    Long id;
    Long senderId;
    String senderUsername;
    String senderFullName;
    Long conversationId;
    String content;
    Boolean isRead;
    LocalDateTime createdAt;
    LocalDateTime readAt;
}
