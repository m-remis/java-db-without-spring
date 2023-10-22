package com.example.demo.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Objects;

public class DbConfig {

    private DbConfig() {
        // nothing to instantiate
    }

    // to create initial table...
    public static void executeSchemaScript() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(DbConfig.class.getClassLoader().getResourceAsStream("schema.sql"))));
             Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "password");
             Statement statement = connection.createStatement()) {

            String line;
            StringBuilder statementBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    statementBuilder.append(line);
                    if (line.endsWith(";")) {
                        statement.execute(statementBuilder.toString());
                        statementBuilder.setLength(0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}