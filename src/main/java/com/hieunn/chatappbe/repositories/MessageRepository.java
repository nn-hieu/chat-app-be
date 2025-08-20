package com.hieunn.chatappbe.repositories;

import com.hieunn.chatappbe.entities.Message;
import com.hieunn.chatappbe.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtDesc(
            User sender1, User receiver1, User sender2, User receiver2, Pageable pageable);

    List<Message> findBySender_IdAndReceiver_IdAndIsReadFalse(Long senderId, Long receiverId);

    Message findFirstBySender_IdAndReceiver_IdOrSender_IdAndReceiver_IdOrderByCreatedAtDesc(
            Long senderId1, Long receiverId1,
            Long senderId2, Long receiverId2
    );

    long countBySender_IdAndReceiver_IdAndIsReadFalse(Long senderId, Long receiverId);
}
