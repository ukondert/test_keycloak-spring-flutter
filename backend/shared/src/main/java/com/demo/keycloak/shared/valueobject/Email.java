package com.demo.keycloak.shared.valueobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

/**
 * Email Value Object - Immutable and validated
 */
@Value
public class Email {
    
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = EMAIL_PATTERN, message = "Invalid email format")
    String value;
    
    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        // Normalize: trim and lowercase
        String normalized = value.toLowerCase().trim();
        if (!normalized.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
        this.value = normalized;
    }
    
    public static Email of(String value) {
        return new Email(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
