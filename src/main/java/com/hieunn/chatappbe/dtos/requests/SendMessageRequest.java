package com.hieunn.chatappbe.dtos.requests;

import com.hieunn.chatappbe.entities.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
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
public class SendMessageRequest {
    @NotNull(message = "Receiver ID is required")
    Long receiverId;

    @NotBlank(message = "Content is required")
    String content;

    MessageType type = MessageType.TEXT;
}
