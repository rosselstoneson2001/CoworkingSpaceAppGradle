package com.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Configuration class for setting up the database connection using HikariCP.
 * This class reads database properties from the {@code db.properties} file
 * and configures a {@link DataSource} bean.
 */
@Configuration
@PropertySource(value = "classpath:db.properties")
public class DBSpringConfig {

    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Value("${database.driver-class-name}")
    private String driverClassName;

    @Value("${hikari.maximum-pool-size}")
    private int maxPoolSize;

    @Value("${hikari.minimum-idle}")
    private int minIdle;

    @Value("${hikari.idle-timeout}")
    private long idleTimeout;

    @Value("${hikari.connection-timeout}")
    private long connectionTimeout;

    @Value("${hikari.max-lifetime}")
    private long maxLifetime;

    @Value("${hikari.auto-commit}")
    private boolean autoCommit;

    /**
     * Creates and configures a {@link HikariDataSource} bean.
     *
     * @return a configured {@link DataSource} instance managed by HikariCP.
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setMinimumIdle(minIdle);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setMaxLifetime(maxLifetime);
        hikariConfig.setAutoCommit(autoCommit);

        return new HikariDataSource(hikariConfig);
    }
}
