package com.hieunn.chatappbe.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypingRequest {
    @NotNull(message = "Conversation ID is required")
    Long conversationId;

    @NotNull(message = "Typing status is required")
    Boolean isTyping;
}
