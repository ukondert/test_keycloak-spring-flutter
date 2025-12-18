package com.demo.keycloak.user.adapter.in.rest.dto;

/**
 * DTO for user response
 */
public record UserResponseDto(
    String id,
    String username,
    String email,
    String firstName,
    String lastName,
    String keycloakId
) {
}
