package com.demo.keycloak.shared.exception;

/**
 * Exception thrown when attempting to create a user that already exists
 */
public class UserAlreadyExistsException extends DomainException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public UserAlreadyExistsException(String username, String email) {
        super(String.format("User with username '%s' or email '%s' already exists", username, email));
    }
}
