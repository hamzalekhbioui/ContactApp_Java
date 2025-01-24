package com.javaproject;

import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:contact.db";
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

//    public static void initializeDatabase() {
//        try (Connection conn = connect()) {
//            System.out.println("Database initialized successfully.");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
