//package com.javaproject;
//
//import java.sql.*;
//
//public class DatabaseHelper {
//    private static final String URL = "jdbc:sqlite:contact.db";
//    public static Connection connect() throws SQLException {
//        return DriverManager.getConnection(URL);
//    }
//
//    public static void initializeDatabase() {
//        try (Connection conn = connect()) {
//            System.out.println("Database initialized successfully.");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}

package com.javaproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:contact.db";

    // Connect to the database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Execute an SQLite script
    public static void executeScript(String scriptPath) {
        try (Connection conn = connect();
             BufferedReader reader = new BufferedReader(new FileReader(scriptPath));
             Statement stmt = conn.createStatement()) {

            String line;
            StringBuilder sql = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sql.append(line);
                // Execute each command when a semicolon is encountered
                if (line.trim().endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql = new StringBuilder();
                }
            }

            System.out.println("Database script executed successfully.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
