package com.hieunn.chatappbe.dtos.responses;

import com.hieunn.chatappbe.entities.enums.FileType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentDTO {
    Long id;
    String url;
    String publicId;
    FileType type;
    String originalFilename;
    Long size;
    String format;
    LocalDateTime createdAt;
}
