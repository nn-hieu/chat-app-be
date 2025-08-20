package com.hieunn.chatappbe.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    int status;
    T data;
    String error;
    String message;
    LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(HttpStatus status, T data, String message) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(HttpStatus status, T data) {
        return success(status, data, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(HttpStatus.OK, data, null);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}