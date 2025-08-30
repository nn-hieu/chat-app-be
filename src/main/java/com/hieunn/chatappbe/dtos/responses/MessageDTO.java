package com.hieunn.chatappbe.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isRead")
    Boolean isRead;
    LocalDateTime createdAt;
    LocalDateTime readAt;
}
