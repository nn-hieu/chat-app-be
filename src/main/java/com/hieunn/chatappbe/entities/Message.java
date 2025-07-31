package com.hieunn.chatappbe.entities;

import com.hieunn.chatappbe.entities.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    User receiver;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    MessageType type = MessageType.TEXT;

    @Builder.Default
    boolean isRead = false;

    @CreatedDate
    LocalDateTime createdAt;

    LocalDateTime readAt;
}
