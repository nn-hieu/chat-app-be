package com.hieunn.chatappbe.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypingDTO {
    Long senderId;
    Long conversationId;
    Boolean isTyping;
}
