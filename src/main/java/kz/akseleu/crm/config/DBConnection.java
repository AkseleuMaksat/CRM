package kz.akseleu.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DBConnection {

    private final Connection connection;

    public DBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Bitlab", "postgres", "postgres");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Connection getConnection() {
        return connection;
    }
}
