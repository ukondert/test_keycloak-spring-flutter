package com.demo.keycloak.user.adapter.in.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for error responses
 */
public record ErrorResponseDto(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<String> details
) {
    public ErrorResponseDto(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }
    
    public ErrorResponseDto(int status, String error, String message, String path, List<String> details) {
        this(LocalDateTime.now(), status, error, message, path, details);
    }
}
