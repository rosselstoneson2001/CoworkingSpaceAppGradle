package com.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for scanning and registering Spring components
 * under the base package {@code com.example}.
 * <p>
 * This class ensures that all the Spring components in the specified package
 * are discovered and instantiated by the Spring container.
 * </p>
 */
@Configuration
@ComponentScan(basePackages = "com.example")
public class DomainSpringConfig {

}
