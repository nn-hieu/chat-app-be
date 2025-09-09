package com.hieunn.chatappbe.mappers;

import com.hieunn.chatappbe.dtos.responses.ConversationDTO;
import com.hieunn.chatappbe.entities.Conversation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ConversationMapper {
    ConversationDTO toConversationDTO(Conversation conversation);
}
