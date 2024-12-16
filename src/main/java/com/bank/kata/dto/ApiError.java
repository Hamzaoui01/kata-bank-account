package com.bank.kata.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Builder
public record ApiError(
    int status,
    String error,
    String message,
     String path,
     LocalDateTime timestamp){

    public static ApiError create(String message, HttpStatus httpStatus, WebRequest request){
        return ApiError.builder()
                .error(httpStatus.name())
                .status(httpStatus.value())
                .message(message)
                .path(request.getDescription(false).replace("uri=",""))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
