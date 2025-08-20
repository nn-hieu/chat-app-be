package com.hieunn.chatappbe.repositories;

import com.hieunn.chatappbe.entities.FriendRequest;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.entities.enums.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

    List<FriendRequest> findByStatusAndSenderIdOrStatusAndReceiverId(
            FriendRequestStatus status1, Long senderId,
            FriendRequestStatus status2, Long receiverId
    );

    List<FriendRequest> findBySender_Id(Long senderId);

    List<FriendRequest> findByReceiver_Id(Long receiverId);
}
