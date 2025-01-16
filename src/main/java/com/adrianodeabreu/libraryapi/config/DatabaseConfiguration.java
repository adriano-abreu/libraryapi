package com.adrianodeabreu.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {
    @Value( "${spring.datasource.url}")
    String url;
    @Value( "${spring.datasource.username}")
    String username;
    @Value( "${spring.datasource.password}")
    String password;
    @Value( "${spring.datasource.driver-class-name}")
    String driverClassName;

    @Bean
    public DataSource hikariDataSource() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setPoolName("library-db-pool");
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);
        config.setConnectionTestQuery("select 1");
        return new HikariDataSource(config);
    }
}
