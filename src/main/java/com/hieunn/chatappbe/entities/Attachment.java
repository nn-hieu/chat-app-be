package com.hieunn.chatappbe.entities;

import com.hieunn.chatappbe.entities.enums.AttachmentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 500)
    String url;

    @Column(nullable = false, length = 200)
    String publicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    AttachmentType type;

    String originalFilename;

    Long size;

    @Column(nullable = false, length = 10)
    String format;

    @CreatedDate
    @Setter(AccessLevel.NONE)
    LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    Message message;
}