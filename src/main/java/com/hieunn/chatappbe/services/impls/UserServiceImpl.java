package com.hieunn.chatappbe.services.impls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hieunn.chatappbe.dtos.requests.UserUpdateRequest;
import com.hieunn.chatappbe.dtos.responses.FriendDTO;
import com.hieunn.chatappbe.dtos.responses.UserDTO;
import com.hieunn.chatappbe.entities.FriendRequest;
import com.hieunn.chatappbe.entities.Message;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.entities.enums.FriendRequestStatus;
import com.hieunn.chatappbe.mappers.UserMapper;
import com.hieunn.chatappbe.repositories.FriendRequestRepository;
import com.hieunn.chatappbe.repositories.MessageRepository;
import com.hieunn.chatappbe.repositories.UserRepository;
import com.hieunn.chatappbe.repositories.specifications.UserSpecifications;
import com.hieunn.chatappbe.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    FriendRequestRepository friendRequestRepository;
    UserMapper userMapper;
    MessageRepository messageRepository;
    Cloudinary cloudinary;

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return userMapper.toUserDTO(user);
    }

    @Override
    @Transactional
    public void updateOnlineStatus(Long userId, boolean online) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setIsOnline(online);
        if (!online) {
            user.setLastSeen(LocalDateTime.now());
        }

        userRepository.save(user);
    }

    @Override
    public List<FriendDTO> findFriends(Long userId) {
        List<FriendRequest> acceptedRequests =
                friendRequestRepository.findByStatusAndSenderIdOrStatusAndReceiverId(
                        FriendRequestStatus.ACCEPTED, userId,
                        FriendRequestStatus.ACCEPTED, userId
                );

        Set<User> uniqueFriends = acceptedRequests.stream()
                .map(fr -> fr.getSender().getId().equals(userId)
                        ? fr.getReceiver()
                        : fr.getSender())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<FriendDTO> result = new ArrayList<>();

        for (User friend : uniqueFriends) {
            Message lastMsg = messageRepository
                    .findFirstBySender_IdAndReceiver_IdOrSender_IdAndReceiver_IdOrderByCreatedAtDesc(
                            userId, friend.getId(),
                            friend.getId(), userId
                    );

            long unreadCount = messageRepository
                    .countBySender_IdAndReceiver_IdAndIsReadFalse(friend.getId(), userId);

            FriendDTO dto = new FriendDTO();
            dto.setUser(userMapper.toUserDTO(friend));

            if (lastMsg != null) {
                dto.setLastMessage(lastMsg.getContent());
                dto.setLastSenderId(lastMsg.getSender().getId());
                dto.setLastMessageTime(lastMsg.getCreatedAt());
            }

            dto.setUnreadCount(unreadCount);

            result.add(dto);
        }

        result.sort(Comparator.comparing(
                FriendDTO::getLastMessageTime,
                Comparator.nullsLast(Comparator.reverseOrder()))
        );

        return result;
    }

    @Override
    public List<UserDTO> search(String username, String email, String fullName) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<User> spec = Specification.allOf(
                UserSpecifications.hasUsername(username),
                UserSpecifications.hasEmail(email),
                UserSpecifications.hasFullName(fullName)
        );

        return userRepository.findAll(spec)
                .stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .map(userMapper::toUserDTO)
                .toList();
    }

    @Override
    @Transactional
    public UserDTO updateProfile(Long userId, UserUpdateRequest request, MultipartFile avatar) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        String oldAvatarPublicId = user.getAvatarPublicId();

        if (avatar != null && !avatar.isEmpty()) {
            if (avatar.getSize() > 5 * 1024 * 1024) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar size must be less than 5MB");
            }

            try {
                Map uploadResult = cloudinary
                        .uploader()
                        .upload(
                                avatar.getBytes(),
                                ObjectUtils.asMap("folder", "chat-app")
                        );

                String newAvatarUrl = (String) uploadResult.get("secure_url");
                String newAvatarPublicId = (String) uploadResult.get("public_id");

                user.setAvatarUrl(newAvatarUrl);
                user.setAvatarPublicId(newAvatarPublicId);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not upload avatar");
            }
        }

        userRepository.save(user);

        if (avatar != null && !avatar.isEmpty() && oldAvatarPublicId != null) {
            try {
                cloudinary.uploader().destroy(oldAvatarPublicId, ObjectUtils.emptyMap());
            } catch (IOException e) {
                log.error("Could not destroy old avatar", e);
            }
        }

        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
