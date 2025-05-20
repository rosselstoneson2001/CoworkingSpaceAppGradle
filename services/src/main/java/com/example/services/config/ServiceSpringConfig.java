package com.example.services.config;

import com.example.config.DomainSpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for scanning and registering Spring components
 * under the base package {@code com.example.services} and importing
 * additional configuration from {@link DomainSpringConfig}.
 * <p>
 * This class ensures that all Spring components in the {@code com.example.services}
 * package are discovered and instantiated by the Spring container.
 * It also imports the configuration from {@link DomainSpringConfig}, which may
 * define beans and settings for the domain layer of the application.
 * </p>
 */
@Configuration
@ComponentScan(basePackages = "com.example.services")
@Import(DomainSpringConfig.class)
public class ServiceSpringConfig {
}
