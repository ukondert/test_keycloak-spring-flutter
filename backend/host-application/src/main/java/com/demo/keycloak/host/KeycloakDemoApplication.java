package com.demo.keycloak.host;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main Spring Boot Application
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.demo.keycloak")
@EntityScan(basePackages = "com.demo.keycloak.user.adapter.out.persistence")
public class KeycloakDemoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(KeycloakDemoApplication.class, args);
    }
}
