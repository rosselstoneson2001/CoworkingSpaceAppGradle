package com.example.ui.config;

import com.example.services.config.ServiceSpringConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configuration class for scanning and registering Spring components
 * under the base package {@code com.example}, specifically focused on
 * the user interface (UI) layer, and importing the configuration from
 * {@link ServiceSpringConfig}.
 * <p>
 * This class ensures that all Spring components in the {@code com.example.ui}
 * package, as well as any necessary beans from the service layer, are
 * discovered and instantiated by the Spring container. It imports the
 * {@link ServiceSpringConfig} class to access the service layer's beans
 * and configurations.
 * </p>
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.example")
@Import(ServiceSpringConfig.class)
public class UISpringConfig {
}
