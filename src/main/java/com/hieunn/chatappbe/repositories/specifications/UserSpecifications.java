package com.hieunn.chatappbe.repositories.specifications;

import com.hieunn.chatappbe.entities.User;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isEmpty()) return null;

            String likePattern = "%" + username.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("username")), likePattern);
        };
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isEmpty()) return null;

            String likePattern = "%" + email.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("email")), likePattern);
        };
    }
    public static Specification<User> hasFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.isEmpty()) return null;

            String likePattern = "%" + fullName.toLowerCase() + "%";

            Expression<String> fullNameExpression = cb.concat(
                    cb.concat(cb.lower(root.get("firstName")), " "),
                    cb.lower(root.get("lastName"))
            );

            return cb.like(fullNameExpression, likePattern);
        };
    }
}
