package com.demo.keycloak.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository - Infrastructure Layer
 */
@Repository
public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    
    Optional<UserJpaEntity> findByUsername(String username);
    
    Optional<UserJpaEntity> findByEmail(String email);
    
    Optional<UserJpaEntity> findByKeycloakId(String keycloakId);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
