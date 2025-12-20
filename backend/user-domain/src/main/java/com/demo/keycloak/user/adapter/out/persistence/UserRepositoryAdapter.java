package com.demo.keycloak.user.adapter.out.persistence;

import com.demo.keycloak.shared.valueobject.Email;
import com.demo.keycloak.shared.valueobject.UserId;
import com.demo.keycloak.user.domain.model.User;
import com.demo.keycloak.user.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * User Repository Adapter - Infrastructure Layer
 * 
 * Implements the domain port by delegating to Spring Data JPA.
 * Handles mapping between Domain and Persistence models.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserJpaEntity.builder()
                .id(user.getId().getValue())
                .username(user.getUsername())
                .email(user.getEmail().getValue())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .keycloakId(user.getKeycloakId())
                .createdAt(LocalDateTime.now()) // Note: In reality, we should handle this via @CreatedDate
                .updatedAt(LocalDateTime.now())
                .build();
        
        UserJpaEntity savedEntity = springDataUserRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return springDataUserRepository.findById(id.getValue())
                .map(this::mapToDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springDataUserRepository.findByUsername(username)
                .map(this::mapToDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return springDataUserRepository.findByEmail(email.getValue())
                .map(this::mapToDomain);
    }

    @Override
    public Optional<User> findByKeycloakId(String keycloakId) {
        return springDataUserRepository.findByKeycloakId(keycloakId)
                .map(this::mapToDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return springDataUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return springDataUserRepository.existsByEmail(email.getValue());
    }

    private User mapToDomain(UserJpaEntity entity) {
        return User.create(
                UserId.of(entity.getId()),
                entity.getUsername(),
                Email.of(entity.getEmail()),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getKeycloakId()
        );
    }
}
