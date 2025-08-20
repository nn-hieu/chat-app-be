package com.hieunn.chatappbe.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendDTO {
    UserDTO user;
    String lastMessage;
    Long lastSenderId;
    LocalDateTime lastMessageTime;
    long unreadCount;
}
