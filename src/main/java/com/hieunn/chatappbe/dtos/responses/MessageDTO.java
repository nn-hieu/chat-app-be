package com.hieunn.chatappbe.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hieunn.chatappbe.entities.enums.MessageType;
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
    Long receiverId;
    String receiverUsername;
    String receiverFullName;
    String content;
    MessageType type;
    @JsonProperty("isRead")
    boolean isRead;
    LocalDateTime createdAt;
    LocalDateTime readAt;
}
