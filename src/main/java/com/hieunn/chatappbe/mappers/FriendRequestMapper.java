package com.hieunn.chatappbe.mappers;

import com.hieunn.chatappbe.dtos.responses.FriendRequestDTO;
import com.hieunn.chatappbe.entities.FriendRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.username", target = "senderUsername")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "receiver.username", target = "receiverUsername")
    @Mapping(source = "sender.avatarUrl", target = "senderAvatar")
    @Mapping(source = "receiver.avatarUrl", target = "receiverAvatar")
    FriendRequestDTO toFriendRequestDTO(FriendRequest friendRequest);
}
