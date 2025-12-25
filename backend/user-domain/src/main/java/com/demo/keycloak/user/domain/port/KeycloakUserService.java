package com.demo.keycloak.user.domain.port;

import com.demo.keycloak.user.domain.model.User;

/**
 * Keycloak Service Port (Interface) - Domain Layer
 * 
 * Anti-Corruption Layer interface - protects domain from external system details.
 * Implementation will be in the adapter layer.
 */
public interface KeycloakUserService {
    
    /**
     * Create user in Keycloak and return the Keycloak user ID
     * 
     * @param user User to create
     * @param password User's password
     * @return Keycloak user ID
     */
    String createUser(User user, String password);
    
    /**
     * Get user information from Keycloak
     * 
     * @param keycloakId Keycloak user ID
     * @return User information or empty if not found
     */
    User getUserFromKeycloak(String keycloakId);
    
    /**
     * Delete user from Keycloak
     * 
     * @param keycloakId Keycloak user ID
     */
    void deleteUser(String keycloakId);

    /**
     * Authenticate user and return tokens
     * 
     * @param username User's username
     * @param password User's password
     * @return Token information
     */
    Object authenticate(String username, String password);
}
