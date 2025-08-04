package com.hieunn.chatappbe.mappers;

import com.hieunn.chatappbe.dtos.responses.UserDTO;
import com.hieunn.chatappbe.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);
}
