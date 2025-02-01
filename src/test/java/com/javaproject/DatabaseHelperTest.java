package com.javaproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseHelperTest {

    @BeforeEach
    public void setupDatabase() throws SQLException {
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS person");
            
            stmt.execute("CREATE TABLE person (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "nickname TEXT, " +
                    "phoneNumber TEXT, " +
                    "address TEXT, " +
                    "emailAddress TEXT, " +
                    "birthDate TEXT)");
        }
    }

    @Test
    public void testInsertPerson() {
        try (Connection conn = DatabaseHelper.connect()) {
            String insertSQL = "INSERT INTO person (firstName, lastName, nickname, phoneNumber, address, emailAddress, birthDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, "John");
                pstmt.setString(2, "Doe");
                pstmt.setString(3, "Johnny");
                pstmt.setString(4, "1234567890");
                pstmt.setString(5, "123 Street");
                pstmt.setString(6, "john.doe@example.com");
                pstmt.setString(7, "1990-01-01");
                int rows = pstmt.executeUpdate();
                assertEquals(1, rows, "One row should have been inserted");
            }

            String querySQL = "SELECT * FROM person WHERE firstName = ? AND lastName = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
                pstmt.setString(1, "John");
                pstmt.setString(2, "Doe");
                try (ResultSet rs = pstmt.executeQuery()) {
                    assertTrue(rs.next(), "Person should exist in the database");
                    assertEquals("Johnny", rs.getString("nickname"), "Nickname should match");
                }
            }
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}
