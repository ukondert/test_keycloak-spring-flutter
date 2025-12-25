package com.demo.keycloak.shared.exception;

/**
 * Base exception for all domain-level exceptions
 */
public class DomainException extends RuntimeException {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
