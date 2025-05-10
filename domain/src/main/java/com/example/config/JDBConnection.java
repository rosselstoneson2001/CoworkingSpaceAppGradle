package com.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class JDBConnection {

    /**
     * A utility class for managing the JDBC connection pool using HikariCP.
     * <p>
     * This class provides a singleton HikariDataSource that is configured
     * with the database connection properties such as JDBC URL, username, password,
     * and connection pool settings. It ensures efficient management of database connections
     * with connection pooling capabilities.
     * </p>
     */
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5450/postgres");
        config.setUsername("postgres");
        config.setPassword("cospaceapp123");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000); // 30 seconds

        dataSource = new HikariDataSource(config);
    }

    /**
     * Retrieves the singleton instance of the HikariDataSource.
     * <p>
     * This method returns the DataSource instance configured with the
     * PostgreSQL connection settings, providing access to the connection pool.
     * </p>
     *
     * @return the configured DataSource instance
     */
    public static DataSource getDataSource() {
        return dataSource;
    }


}
