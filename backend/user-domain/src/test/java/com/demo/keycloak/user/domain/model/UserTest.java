package com.demo.keycloak.user.domain.model;

import com.demo.keycloak.shared.valueobject.Email;
import com.demo.keycloak.shared.valueobject.UserId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for User domain model
 */
class UserTest {
    
    @Test
    void shouldCreateNewUser() {
        // Given
        String username = "testuser";
        Email email = Email.of("test@example.com");
        String firstName = "Test";
        String lastName = "User";
        
        // When
        User user = User.createNew(username, email, firstName, lastName);
        
        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getLastName()).isEqualTo(lastName);
        assertThat(user.getKeycloakId()).isNull();
        assertThat(user.isLinkedWithKeycloak()).isFalse();
    }
    
    @Test
    void shouldLinkUserWithKeycloak() {
        // Given
        User user = User.createNew("testuser", Email.of("test@example.com"), "Test", "User");
        String keycloakId = "keycloak-123";
        
        // When
        user.linkWithKeycloak(keycloakId);
        
        // Then
        assertThat(user.getKeycloakId()).isEqualTo(keycloakId);
        assertThat(user.isLinkedWithKeycloak()).isTrue();
    }
    
    @Test
    void shouldThrowExceptionWhenLinkingWithNullKeycloakId() {
        // Given
        User user = User.createNew("testuser", Email.of("test@example.com"), "Test", "User");
        
        // When & Then
        assertThatThrownBy(() -> user.linkWithKeycloak(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Keycloak ID cannot be null or blank");
    }
    
    @Test
    void shouldThrowExceptionWhenLinkingAlreadyLinkedUser() {
        // Given
        User user = User.createNew("testuser", Email.of("test@example.com"), "Test", "User");
        user.linkWithKeycloak("keycloak-123");
        
        // When & Then
        assertThatThrownBy(() -> user.linkWithKeycloak("keycloak-456"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("already linked with Keycloak");
    }
    
    @Test
    void shouldUpdateProfile() {
        // Given
        User user = User.createNew("testuser", Email.of("test@example.com"), "Test", "User");
        String newFirstName = "NewFirst";
        String newLastName = "NewLast";
        Email newEmail = Email.of("newemail@example.com");
        
        // When
        user.updateProfile(newFirstName, newLastName, newEmail);
        
        // Then
        assertThat(user.getFirstName()).isEqualTo(newFirstName);
        assertThat(user.getLastName()).isEqualTo(newLastName);
        assertThat(user.getEmail()).isEqualTo(newEmail);
    }
    
    @Test
    void shouldGetFullName() {
        // Given
        User user = User.createNew("testuser", Email.of("test@example.com"), "Test", "User");
        
        // When
        String fullName = user.getFullName();
        
        // Then
        assertThat(fullName).isEqualTo("Test User");
    }
    
    @Test
    void shouldThrowExceptionForInvalidUsername() {
        // Given
        Email email = Email.of("test@example.com");
        
        // When & Then - username too short
        assertThatThrownBy(() -> User.createNew("ab", email, "Test", "User"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("must be between 3 and 50 characters");
        
        // When & Then - username with invalid characters
        assertThatThrownBy(() -> User.createNew("test user!", email, "Test", "User"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("can only contain alphanumeric");
    }
    
    @Test
    void shouldThrowExceptionForInvalidName() {
        // Given
        Email email = Email.of("test@example.com");
        
        // When & Then - blank first name
        assertThatThrownBy(() -> User.createNew("testuser", email, "", "User"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("First name cannot be null or blank");
        
        // When & Then - name too long
        String longName = "a".repeat(101);
        assertThatThrownBy(() -> User.createNew("testuser", email, longName, "User"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot exceed 100 characters");
    }
}
