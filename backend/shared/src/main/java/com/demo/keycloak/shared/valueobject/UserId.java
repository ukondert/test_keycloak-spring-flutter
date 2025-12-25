package com.demo.keycloak.shared.valueobject;

import lombok.Value;

import java.util.UUID;

/**
 * UserId Value Object - Immutable identifier
 */
@Value
public class UserId {
    
    UUID value;
    
    public UserId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        this.value = value;
    }
    
    public static UserId of(UUID value) {
        return new UserId(value);
    }
    
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
    
    public static UserId fromString(String value) {
        try {
            return new UserId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UserId format: " + value, e);
        }
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
