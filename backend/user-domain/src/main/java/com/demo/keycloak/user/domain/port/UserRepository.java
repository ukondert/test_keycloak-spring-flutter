package com.demo.keycloak.user.domain.port;

import com.demo.keycloak.shared.valueobject.Email;
import com.demo.keycloak.shared.valueobject.UserId;
import com.demo.keycloak.user.domain.model.User;

import java.util.Optional;

/**
 * Repository Port (Interface) - Domain Layer
 * 
 * This is a port in hexagonal architecture - an interface defined in the domain layer
 * that will be implemented by adapters in the infrastructure layer.
 */
public interface UserRepository {
    
    /**
     * Save or update a user
     */
    User save(User user);
    
    /**
     * Find user by ID
     */
    Optional<User> findById(UserId id);
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(Email email);
    
    /**
     * Find user by Keycloak ID
     */
    Optional<User> findByKeycloakId(String keycloakId);
    
    /**
     * Check if user exists by username
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if user exists by email
     */
    boolean existsByEmail(Email email);
}
