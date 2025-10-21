package com.experiment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DatabaseChecker implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT version(), current_database(), current_user")) {

            if (rs.next()) {
                System.out.println("✅ Database connected successfully!");
                System.out.println("✅ PostgreSQL Version: " + rs.getString(1));
                System.out.println("✅ Database: " + rs.getString(2));
                System.out.println("✅ User: " + rs.getString(3));
                System.out.println("✅ Profile: " + System.getProperty("spring.profiles.active"));
            }
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}