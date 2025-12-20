package com.demo.keycloak.user.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for user login request
 */
public record LoginRequestDto(
    @NotBlank(message = "Username is required")
    String username,
    
    @NotBlank(message = "Password is required")
    String password
) {
}
