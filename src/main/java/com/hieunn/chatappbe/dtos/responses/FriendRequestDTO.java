package com.hieunn.chatappbe.dtos.responses;

import com.hieunn.chatappbe.entities.enums.FriendRequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendRequestDTO {
    Long id;
    Long senderId;
    String senderUsername;
    Long receiverId;
    String receiverUsername;
    FriendRequestStatus status;
    LocalDateTime createdAt;
    LocalDateTime respondedAt;
}
