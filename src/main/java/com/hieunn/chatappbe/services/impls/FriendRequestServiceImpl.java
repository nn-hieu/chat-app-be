package com.hieunn.chatappbe.services.impls;

import com.hieunn.chatappbe.dtos.responses.FriendRequestDTO;
import com.hieunn.chatappbe.entities.FriendRequest;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.entities.enums.FriendRequestStatus;
import com.hieunn.chatappbe.mappers.FriendRequestMapper;
import com.hieunn.chatappbe.repositories.FriendRequestRepository;
import com.hieunn.chatappbe.repositories.UserRepository;
import com.hieunn.chatappbe.services.FriendRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {
    FriendRequestRepository friendRequestRepository;
    UserRepository userRepository;
    FriendRequestMapper friendRequestMapper;
    SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public FriendRequestDTO send(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));

        List<FriendRequest> pastRequests = friendRequestRepository.findBySender_IdAndReceiver_Id(senderId, receiverId);

        boolean hasActiveRequest = pastRequests.stream()
                .anyMatch(req -> req.getStatus() == FriendRequestStatus.PENDING || req.getStatus() == FriendRequestStatus.ACCEPTED);

        if (hasActiveRequest) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already an active request");
        }

        FriendRequest currentRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        friendRequestRepository.save(currentRequest);

        FriendRequestDTO requestDTO = friendRequestMapper.toFriendRequestDTO(currentRequest);

        messagingTemplate.convertAndSendToUser(
                requestDTO.getReceiverId().toString(),
                "/queue/friend-request",
                requestDTO
        );

        return requestDTO;
    }

    @Override
    @Transactional
    public FriendRequestDTO address(Long id, Boolean isAccepted, Long userId) {
        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend request not found"));

        if (!request.getStatus().equals(FriendRequestStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Friend request is not pending");
        }

        FriendRequestStatus newStatus;
        Long expectedUserId;

        if (isAccepted == null) {
            newStatus = FriendRequestStatus.CANCELLED;
            expectedUserId = request.getSender().getId();
        } else {
            newStatus = isAccepted ? FriendRequestStatus.ACCEPTED : FriendRequestStatus.REJECTED;
            expectedUserId = request.getReceiver().getId();
        }

        if (!userId.equals(expectedUserId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This is not your request");
        }

        request.setStatus(newStatus);
        request.setRespondedAt(LocalDateTime.now());

        friendRequestRepository.save(request);

        FriendRequestDTO requestDTO = friendRequestMapper.toFriendRequestDTO(request);

        messagingTemplate.convertAndSendToUser(
                expectedUserId.toString(),
                "/queue/friend-request",
                requestDTO
        );

        return requestDTO;
    }
}
