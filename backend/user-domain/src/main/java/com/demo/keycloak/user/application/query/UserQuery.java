package com.demo.keycloak.user.application.query;

import com.demo.keycloak.shared.valueobject.UserId;

/**
 * Query for retrieving user information
 */
public record UserQuery(UserId userId) {
    
    public UserQuery {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
    }
}
