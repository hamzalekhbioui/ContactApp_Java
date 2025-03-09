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

    /**
     *
     * @return 
     * @throws SQLException 
     */
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param scriptPath
     */
    public static void executeScript(String scriptPath) {
        try (Connection conn = connect();
             BufferedReader reader = new BufferedReader(new FileReader(scriptPath));
             Statement stmt = conn.createStatement()) {

            String line;
            StringBuilder sql = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sql.append(line);
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

    public static void ensureTableSchema() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS person ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "lastName TEXT NOT NULL, "
                    + "firstName TEXT NOT NULL, "
                    + "nickname TEXT NOT NULL, "
                    + "phoneNumber TEXT, "
                    + "address TEXT, "
                    + "emailAddress TEXT, "
                    + "birthDate TEXT, "
                    + "category TEXT"
                    + ")");
            System.out.println("Database schema checked successfully!");
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

