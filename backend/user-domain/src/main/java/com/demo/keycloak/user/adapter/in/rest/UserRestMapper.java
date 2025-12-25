package com.demo.keycloak.user.adapter.in.rest;

import com.demo.keycloak.user.adapter.in.rest.dto.RegisterUserRequestDto;
import com.demo.keycloak.user.adapter.in.rest.dto.UserResponseDto;
import com.demo.keycloak.user.application.command.RegisterUserCommand;
import com.demo.keycloak.user.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper for REST DTOs
 */
@Component
public class UserRestMapper {
    
    public RegisterUserCommand toCommand(RegisterUserRequestDto dto) {
        return new RegisterUserCommand(
            dto.username(),
            dto.email(),
            dto.password(),
            dto.firstName(),
            dto.lastName()
        );
    }
    
    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
            user.getId().getValue().toString(),
            user.getUsername(),
            user.getEmail().getValue(),
            user.getFirstName(),
            user.getLastName(),
            user.getKeycloakId()
        );
    }
}
