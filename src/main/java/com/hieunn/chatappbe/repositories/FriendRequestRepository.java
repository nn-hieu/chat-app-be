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
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);

    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequestStatus status);

    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, FriendRequestStatus status);

    List<FriendRequest> findBySenderAndReceiverAndStatusOrReceiverAndSenderAndStatus(
            User sender1, User receiver1, FriendRequestStatus status1,
            User sender2, User receiver2, FriendRequestStatus status2);
}
