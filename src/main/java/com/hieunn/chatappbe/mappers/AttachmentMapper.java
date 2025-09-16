package com.hieunn.chatappbe.mappers;

import com.hieunn.chatappbe.dtos.responses.AttachmentDTO;
import com.hieunn.chatappbe.entities.Attachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentDTO toAttachmentDTO(Attachment attachment);
}
