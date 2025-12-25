package com.demo.keycloak.shared.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for Email value object
 */
class EmailTest {
    
    @Test
    void shouldCreateValidEmail() {
        // Given
        String emailValue = "test@example.com";
        
        // When
        Email email = Email.of(emailValue);
        
        // Then
        assertThat(email).isNotNull();
        assertThat(email.getValue()).isEqualTo(emailValue);
    }
    
    @Test
    void shouldNormalizeEmailToLowerCase() {
        // Given
        String emailValue = "Test@Example.COM";
        
        // When
        Email email = Email.of(emailValue);
        
        // Then
        assertThat(email.getValue()).isEqualTo("test@example.com");
    }
    
    @Test
    void shouldTrimEmailWhitespace() {
        // Given
        String emailValue = "  test@example.com  ";
        
        // When
        Email email = Email.of(emailValue);
        
        // Then
        assertThat(email.getValue()).isEqualTo("test@example.com");
    }
    
    @Test
    void shouldThrowExceptionForNullEmail() {
        // When & Then
        assertThatThrownBy(() -> Email.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or blank");
    }
    
    @Test
    void shouldThrowExceptionForBlankEmail() {
        // When & Then
        assertThatThrownBy(() -> Email.of("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or blank");
    }
    
    @Test
    void shouldThrowExceptionForInvalidEmailFormat() {
        // When & Then
        assertThatThrownBy(() -> Email.of("invalid-email"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid email format");
        
        assertThatThrownBy(() -> Email.of("@example.com"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid email format");
        
        assertThatThrownBy(() -> Email.of("test@"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid email format");
    }
    
    @Test
    void shouldBeEqualForSameEmailValue() {
        // Given
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("test@example.com");
        
        // Then
        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }
}
