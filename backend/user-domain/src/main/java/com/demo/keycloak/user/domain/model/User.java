package com.demo.keycloak.user.domain.model;

import com.demo.keycloak.shared.valueobject.Email;
import com.demo.keycloak.shared.valueobject.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * User Aggregate Root - Rich Domain Model
 * 
 * This is the core domain entity representing a user in our system.
 * It contains business logic and enforces business rules.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private final UserId id;
    private String username;
    private Email email;
    private String firstName;
    private String lastName;
    private String keycloakId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Factory method to create an existing User (e.g. from persistence)
     */
    public static User create(
            UserId id,
            String username,
            Email email,
            String firstName,
            String lastName,
            String keycloakId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        validateUsername(username);
        validateName(firstName, "First name");
        validateName(lastName, "Last name");

        return new User(id, username, email, firstName, lastName, keycloakId, createdAt, updatedAt);
    }

    /**
     * Factory method to create a user without ID (for new registrations)
     */
    public static User createNew(
            String username,
            Email email,
            String firstName,
            String lastName) {

        LocalDateTime now = LocalDateTime.now();
        return create(
                UserId.generate(),
                username,
                email,
                firstName,
                lastName,
                null,
                now,
                now);
    }

    /**
     * Business method to link user with Keycloak
     */
    public void linkWithKeycloak(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("Keycloak ID cannot be null or blank");
        }
        if (this.keycloakId != null) {
            throw new IllegalStateException("User is already linked with Keycloak");
        }
        this.keycloakId = keycloakId;
    }

    /**
     * Business method to check if user is linked with Keycloak
     */
    public boolean isLinkedWithKeycloak() {
        return keycloakId != null && !keycloakId.isBlank();
    }

    /**
     * Business method to update profile information
     */
    public void updateProfile(String firstName, String lastName, Email email) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Get full name of the user
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Validation: Username must be 3-50 characters, alphanumeric with
     * underscores/hyphens
     */
    private static void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }
        if (!username.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException(
                    "Username can only contain alphanumeric characters, underscores, and hyphens");
        }
    }

    /**
     * Validation: Names must be 1-100 characters
     */
    private static void validateName(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException(fieldName + " cannot exceed 100 characters");
        }
    }
}
