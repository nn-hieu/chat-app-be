package com.hieunn.chatappbe.repositories;

import com.hieunn.chatappbe.entities.Conversation;
import com.hieunn.chatappbe.entities.enums.ConversationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Conversation findByTypeAndParticipants_IdIn(ConversationType type, Set<Long> userIds);

    List<Conversation> findByParticipants_Id(Long userId);

    @Query("SELECT c FROM Conversation c " +
            "JOIN c.participants p " +
            "WHERE c.type = :type AND p.id IN :userIds " +
            "GROUP BY c.id " +
            "HAVING COUNT(DISTINCT p.id) = :size AND COUNT(DISTINCT p.id) = SIZE(c.participants)")
    Conversation findConversationByTypeAndParticipants(
            @Param("type") ConversationType type,
            @Param("userIds") Set<Long> userIds,
            @Param("size") long size
    );
}
