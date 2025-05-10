package com.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class JDBConnection {

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

    public static DataSource getDataSource() {
        return dataSource;
    }


}
