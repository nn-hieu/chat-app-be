package com.hieunn.chatappbe.configs;

import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findAll().isEmpty()) {
            User user1 = new User();
            user1.setUsername("hieu");
            user1.setPassword("12345");
            user1.setEmail("hieu@gmail.com");
            user1.setFirstName("Hieu");
            user1.setLastName("Nguyen");

            User user2 = new User();
            user2.setUsername("quan");
            user2.setPassword("12345");
            user2.setEmail("quan@gmail.com");
            user2.setFirstName("Quan");
            user2.setLastName("Huynh Minh");

            userRepository.save(user1);
            userRepository.save(user2);
        }
    }
}
