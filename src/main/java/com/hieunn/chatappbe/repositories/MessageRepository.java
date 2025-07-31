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
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtAsc(
            User sender1, User receiver1, User sender2, User receiver2);

    List<Message> findBySenderAndReceiverOrderByCreatedAtDesc(User sender, User receiver);

    List<Message> findByReceiverAndIsReadFalse(User receiver);

    long countByReceiverAndIsReadFalse(User receiver);

    Page<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtDesc(
            User sender1, User receiver1, User sender2, User receiver2, Pageable pageable);
}
