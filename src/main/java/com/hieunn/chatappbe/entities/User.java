package com.hieunn.chatappbe.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, length = 50)
    String username;

    @Column(unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Column(length = 20)
    String firstName;

    @Column(length = 20)
    String lastName;

    String avatar;

    @Builder.Default
    boolean isOnline = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<FriendRequest> sentFriendRequests = new HashSet<>();

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private Set<FriendRequest> receivedFriendRequests = new HashSet<>();

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<Message> sentMessages = new HashSet<>();

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
