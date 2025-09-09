package com.hieunn.chatappbe.mappers;

import com.hieunn.chatappbe.dtos.responses.MessageDTO;
import com.hieunn.chatappbe.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.username", target = "senderUsername")
    @Mapping(source = "sender.fullName", target = "senderFullName")
    @Mapping(source = "conversation.id", target = "conversationId")
    MessageDTO toMessageDTO(Message message);
}
