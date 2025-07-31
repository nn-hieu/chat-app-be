package com.hieunn.chatappbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChatAppBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatAppBeApplication.class, args);
    }

}
