package com.hieunn.chatappbe.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    Long id;
    String username;
    String email;
    String firstName;
    String lastName;
    String fullName;
    String avatar;
    boolean online;
    LocalDateTime lastSeen;
    LocalDateTime createdAt;
}
