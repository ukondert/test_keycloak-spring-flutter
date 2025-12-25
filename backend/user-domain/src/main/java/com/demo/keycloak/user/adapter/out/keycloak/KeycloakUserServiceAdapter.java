package com.demo.keycloak.user.adapter.out.keycloak;

import com.demo.keycloak.user.domain.model.User;
import com.demo.keycloak.user.domain.port.KeycloakUserService;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Keycloak Service Adapter - Infrastructure Layer
 * 
 * Implements the domain port by calling Keycloak Admin API.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakUserServiceAdapter implements KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Override
    public String createUser(User user, String password) {
        log.info("Creating user in Keycloak: {}", user.getUsername());

        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(user.getUsername());
        userRep.setEmail(user.getEmail().getValue());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);

        log.debug("UserRepresentation values - Username: {}, Email: {}, FirstName: {}, LastName: {}",
                userRep.getUsername(), userRep.getEmail(), userRep.getFirstName(), userRep.getLastName());
        log.debug("Keycloak: {}", keycloak);
        log.debug("Realm: {}", realm);
        log.debug("Server URL: {}", serverUrl);
        UsersResource usersResource = keycloak.realm(realm).users();
        log.debug("UsersResource: {}", usersResource);
        try {
            log.debug("Calling Keycloak API (usersResource.create)...");
            Response response = usersResource.create(userRep);
            int status = response.getStatus();
            log.debug("Keycloak Response Status: {}", status);

            if (status < 200 || status >= 300) {
                String errorEntity = "N/A";
                try {
                    errorEntity = response.readEntity(String.class);
                } catch (Exception e) {
                    log.warn("Could not read error entity: {}", e.getMessage());
                }
                log.error("Failed to create user in Keycloak. Status: {}, Error: {}",
                        status, errorEntity);
                throw new RuntimeException("Keycloak API error: status=" + status + ", body=" + errorEntity);
            }

            String location = response.getHeaderString("Location");
            String keycloakId = (location != null)
                    ? location.substring(location.lastIndexOf("/") + 1)
                    : usersResource.search(user.getUsername()).get(0).getId();

            resetPassword(keycloakId, password);
            return keycloakId;
        } catch (Throwable t) {
            log.error("EXCEPTION in Keycloak adapter ({}): {}", t.getClass().getSimpleName(), t.getMessage());

            // Handle wrapped exceptions (like ProcessingException wrapping
            // BadRequestException)
            Throwable cause = t.getCause();
            if (cause != null) {
                log.error("  Caused by ({}): {}", cause.getClass().getSimpleName(), cause.getMessage());
                if (cause instanceof WebApplicationException we) {
                    try {
                        String errorBody = we.getResponse().readEntity(String.class);
                        log.error("  Detailed Error-Body from Cause: {}", errorBody);
                    } catch (Exception ignored) {
                    }
                }
            }

            if (t instanceof WebApplicationException we) {
                try {
                    String errorBody = we.getResponse().readEntity(String.class);
                    log.error("  WebApplicationException body: {}", errorBody);
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException("Keycloak integration failed", t);
        }
    }

    @Override
    public User getUserFromKeycloak(String keycloakId) {
        UserRepresentation userRep = keycloak.realm(realm).users().get(keycloakId).toRepresentation();
        // Since we are mapping back to domain User, we need to map the fields.
        // For simplicity, we implement only what's needed.
        return null; // Implementation not fully required for this demo's current needs
    }

    @Override
    public void deleteUser(String keycloakId) {
        log.info("Deleting user from Keycloak: {}", keycloakId);
        keycloak.realm(realm).users().get(keycloakId).remove();
    }

    @Override
    public AccessTokenResponse authenticate(String username, String password) {
        log.info("Authenticating user in Keycloak: {}", username);

        try (Keycloak userKeycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl) // serverUrl is available in this class
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("demo-flutter-app")
                .username(username)
                .password(password)
                .build()) {

            return userKeycloak.tokenManager().getAccessToken();
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", username, e);
            throw new RuntimeException("Authentication failed", e);
        }
    }

    private void resetPassword(String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloak.realm(realm).users().get(userId).resetPassword(credential);
    }
}
