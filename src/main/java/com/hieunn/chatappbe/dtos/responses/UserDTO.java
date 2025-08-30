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
public class UserDTO {
    Long id;
    String username;
    String email;
    String firstName;
    String lastName;
    String fullName;
    String avatarUrl;
    String avatarPublicId;
    @JsonProperty("isOnline")
    Boolean isOnline;
    LocalDateTime lastSeen;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
