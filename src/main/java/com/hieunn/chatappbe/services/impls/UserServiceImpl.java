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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public List<UserDTO> findFriends(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        List<FriendRequest> acceptedRequests = friendRequestRepository
                .findAcceptedFriendRequestsByUserId(userId);

        return acceptedRequests.stream()
                .map(request -> {
                    User friend = request.getSender().getId().equals(userId)
                            ? request.getReceiver()
                            : request.getSender();
                    return userMapper.toUserDTO(friend);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> findFriends(Long userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        Page<FriendRequest> acceptedRequests = friendRequestRepository
                .findAcceptedFriendRequestsByUserIdWithPagination(userId, pageable);

        return acceptedRequests.map(request -> {
            User friend = request.getSender().getId().equals(userId)
                    ? request.getReceiver()
                    : request.getSender();
            return userMapper.toUserDTO(friend);
        });
    }

    @Override
    public long countFriends(Long userId) {
        return friendRequestRepository.countAcceptedFriendRequestsByUserId(userId);
    }

    @Override
    public List<UserDTO> findMutualFriends(Long userId1, Long userId2) {
        List<UserDTO> friends1 = findFriends(userId1);
        List<UserDTO> friends2 = findFriends(userId2);

        return friends1.stream()
                .filter(friend1 -> friends2.stream()
                        .anyMatch(friend2 -> friend1.getId().equals(friend2.getId())))
                .collect(Collectors.toList());
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
