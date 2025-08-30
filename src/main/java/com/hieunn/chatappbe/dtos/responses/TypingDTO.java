package com.hieunn.chatappbe.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypingDTO {
    Long senderId;
    Long receiverId;
    @JsonProperty("isTyping")
    Boolean isTyping;
}
