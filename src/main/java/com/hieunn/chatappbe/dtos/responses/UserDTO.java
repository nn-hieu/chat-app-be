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
    String avatar;
    @JsonProperty("isOnline")
    boolean isOnline;
    LocalDateTime lastSeen;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
