package com.demo.keycloak.user.adapter.in.rest;

import com.demo.keycloak.shared.valueobject.UserId;
import com.demo.keycloak.user.adapter.in.rest.dto.RegisterUserRequestDto;
import com.demo.keycloak.user.adapter.in.rest.dto.UserResponseDto;
import com.demo.keycloak.user.application.command.RegisterUserCommand;
import com.demo.keycloak.user.application.query.UserQuery;
import com.demo.keycloak.user.application.service.UserApplicationService;
import com.demo.keycloak.user.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for User operations
 * 
 * Thin controller that delegates to application services.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserApplicationService userApplicationService;
    private final UserRestMapper mapper;
    
    /**
     * Register a new user
     * 
     * POST /api/v1/users/register
     * Public endpoint - no authentication required
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody RegisterUserRequestDto request) {
        
        log.info("Received registration request for username: {}", request.username());
        
        RegisterUserCommand command = mapper.toCommand(request);
        User user = userApplicationService.registerUser(command);
        UserResponseDto response = mapper.toDto(user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get current authenticated user
     * 
     * GET /api/v1/users/me
     * Protected endpoint - requires authentication
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        log.info("Fetching current authenticated user");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        
        // Extract username from JWT
        String username = jwt.getClaimAsString("preferred_username");
        
        if (username == null || username.isBlank()) {
            log.error("Username not found in JWT token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        User user = userApplicationService.getUserByUsername(username);
        UserResponseDto response = mapper.toDto(user);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get user by ID
     * 
     * GET /api/v1/users/{id}
     * Protected endpoint - requires authentication
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
        log.info("Fetching user by ID: {}", id);
        
        UserId userId = UserId.fromString(id);
        UserQuery query = new UserQuery(userId);
        User user = userApplicationService.getUserById(query);
        UserResponseDto response = mapper.toDto(user);
        
        return ResponseEntity.ok(response);
    }
}
