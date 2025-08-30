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
import java.util.List;

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

    @Column(unique = true, length = 100)
    String email;

    @Column(nullable = false, length = 50)
    String password;

    @Column(length = 20)
    String firstName;

    @Column(length = 20)
    String lastName;

    @Column(length = 500)
    String avatarUrl;

    @Column(length = 50)
    String avatarPublicId;

    @Builder.Default
    Boolean isOnline = false;

    @CreatedDate
    @Setter(AccessLevel.NONE)
    LocalDateTime createdAt;

    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    LocalDateTime updatedAt;

    LocalDateTime lastSeen;

    public String getFullName() {
        if (firstName == null && lastName == null) {
            return null;
        } else if (firstName == null) {
            return lastName;
        } else if (lastName == null) {
            return firstName;
        } else {
            return lastName + " " + firstName;
        }
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
