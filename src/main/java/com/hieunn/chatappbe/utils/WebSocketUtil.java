package com.hieunn.chatappbe.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketUtil {
    SimpMessagingTemplate messagingTemplate;

    public void notifyUsers(List<Long> userIds, String destination, Object payload) {
        for (Long userId : userIds) {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    destination,
                    payload
            );
        }
    }

    public void notifyUser(Long userId, String destination, Object payload) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                destination,
                payload
        );
    }
}
