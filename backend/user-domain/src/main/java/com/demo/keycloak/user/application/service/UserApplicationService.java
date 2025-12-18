package com.demo.keycloak.user.application.service;

import com.demo.keycloak.shared.exception.ResourceNotFoundException;
import com.demo.keycloak.shared.exception.UserAlreadyExistsException;
import com.demo.keycloak.shared.valueobject.Email;
import com.demo.keycloak.user.application.command.RegisterUserCommand;
import com.demo.keycloak.user.application.query.UserQuery;
import com.demo.keycloak.user.domain.model.User;
import com.demo.keycloak.user.domain.port.KeycloakUserService;
import com.demo.keycloak.user.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service for User Use Cases
 * 
 * This service orchestrates the domain layer and coordinates transactions.
 * It does NOT contain business logic - that belongs in the domain model.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplicationService {
    
    private final UserRepository userRepository;
    private final KeycloakUserService keycloakUserService;
    
    /**
     * Use Case: Register a new user
     * 
     * This creates the user in both the local database and Keycloak.
     */
    @Transactional
    public User registerUser(RegisterUserCommand command) {
        log.info("Registering new user with username: {}", command.username());
        
        // Check if user already exists
        if (userRepository.existsByUsername(command.username())) {
            throw new UserAlreadyExistsException("Username already exists: " + command.username());
        }
        
        Email email = Email.of(command.email());
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists: " + command.email());
        }
        
        // Create domain user object
        User user = User.createNew(
            command.username(),
            email,
            command.firstName(),
            command.lastName()
        );
        
        // Save user to database first
        User savedUser = userRepository.save(user);
        
        try {
            // Create user in Keycloak
            String keycloakId = keycloakUserService.createUser(savedUser, command.password());
            
            // Link user with Keycloak ID
            savedUser.linkWithKeycloak(keycloakId);
            
            // Update user with Keycloak ID
            savedUser = userRepository.save(savedUser);
            
            log.info("Successfully registered user: {} with Keycloak ID: {}", 
                command.username(), keycloakId);
            
            return savedUser;
            
        } catch (Exception e) {
            log.error("Failed to create user in Keycloak, rolling back", e);
            throw new RuntimeException("Failed to register user in authentication system", e);
        }
    }
    
    /**
     * Use Case: Get user by ID
     */
    @Transactional(readOnly = true)
    public User getUserById(UserQuery query) {
        log.debug("Fetching user by ID: {}", query.userId());
        
        return userRepository.findById(query.userId())
            .orElseThrow(() -> new ResourceNotFoundException("User", query.userId().toString()));
    }
    
    /**
     * Use Case: Get user by username
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User with username: " + username));
    }
    
    /**
     * Use Case: Get user by Keycloak ID
     */
    @Transactional(readOnly = true)
    public User getUserByKeycloakId(String keycloakId) {
        log.debug("Fetching user by Keycloak ID: {}", keycloakId);
        
        return userRepository.findByKeycloakId(keycloakId)
            .orElseThrow(() -> new ResourceNotFoundException("User with Keycloak ID: " + keycloakId));
    }
}
