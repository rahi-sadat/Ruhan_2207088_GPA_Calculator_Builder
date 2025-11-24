package com.example.ruhan_2207088_gpa_calculator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database/identifier.sqlite";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
