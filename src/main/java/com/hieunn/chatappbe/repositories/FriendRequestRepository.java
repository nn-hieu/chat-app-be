package com.hieunn.chatappbe.repositories;

import com.hieunn.chatappbe.entities.FriendRequest;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.entities.enums.FriendRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

    List<FriendRequest> findBySender_Id(Long senderId);

    List<FriendRequest> findByReceiver_Id(Long receiverId);

    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId) " +
            "AND fr.status = 'ACCEPTED'")
    List<FriendRequest> findAcceptedFriendRequestsByUserId(@Param("userId") Long userId);

    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId) " +
            "AND fr.status = 'ACCEPTED' " +
            "ORDER BY fr.createdAt DESC")
    Page<FriendRequest> findAcceptedFriendRequestsByUserIdWithPagination(
            @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(fr) FROM FriendRequest fr " +
            "WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId) " +
            "AND fr.status = 'ACCEPTED'")
    long countAcceptedFriendRequestsByUserId(@Param("userId") Long userId);
}
