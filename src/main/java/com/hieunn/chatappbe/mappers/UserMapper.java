package com.hieunn.chatappbe.mappers;

import com.hieunn.chatappbe.dtos.responses.UserDTO;
import com.hieunn.chatappbe.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "online", target = "isOnline")
    UserDTO toUserDTO(User user);
}
