package com.hieunn.chatappbe.dtos.responses;

import com.hieunn.chatappbe.entities.enums.FileType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDTO {
    String url;
    String name;
    String publicId;
    FileType type;
    String format;
    Long size;
}
