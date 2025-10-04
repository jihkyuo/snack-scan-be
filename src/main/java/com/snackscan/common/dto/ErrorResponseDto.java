package com.snackscan.common.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String errorCode;

    public static ErrorResponseDto of(int status, String error, String message, String path) {
        return new ErrorResponseDto(
            LocalDateTime.now(),
            status,
            error,
            message,
            path,
            null
        );
    }
    
    public static ErrorResponseDto of(int status, String error, String message, String path, String errorCode) {
        return new ErrorResponseDto(
            LocalDateTime.now(),
            status,
            error,
            message,
            path,
            errorCode
        );
    }
}
