package com.example.api;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration class for setting up entity scanning and repository enabling.
 *
 * This class is responsible for configuring the Spring Data JPA environment,
 * including entity scanning and repository activation for the `com.example.domain.entities`
 * package and the `com.example.domain.repositories` package. It is enabled when the application
 * is running outside of the test profile.
 *
 * It ensures that JPA entities and repositories are correctly recognized by Spring when the
 * application starts up, but it is excluded during tests by specifying the `@Profile("!test")` annotation.
 */
@EntityScan(basePackages = "com.example.domain.entities")
@EnableJpaRepositories(basePackages = "com.example.domain.repositories")
@Configuration
@Profile("!test")
public class JpaConfig {
}
