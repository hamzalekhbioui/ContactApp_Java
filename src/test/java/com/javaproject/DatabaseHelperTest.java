package com.javaproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void testUpdatePerson() {
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
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            fail("Failed to insert person: " + e.getMessage());
        }

        try (Connection conn = DatabaseHelper.connect()) {
            String updateSQL = "UPDATE person SET lastName = ? WHERE firstName = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setString(1, "Smith");
                pstmt.setString(2, "John");
                pstmt.executeUpdate();
            }

            String querySQL = "SELECT lastName FROM person WHERE firstName = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
                pstmt.setString(1, "John");
                ResultSet rs = pstmt.executeQuery();
                assertTrue(rs.next());
                assertEquals("Smith", rs.getString(1), "Last name should be updated");
            }
        } catch (SQLException e) {
            fail("Failed to update person: " + e.getMessage());
        }
    }

    @Test
    public void testExecuteInvalidSQL() {
        assertThrows(SQLException.class, () -> {
            DatabaseHelper.executeScript("invalid_script.sql"); 
        });
    }

    @Test
    public void testEnsureTableSchema() {
        DatabaseHelper.ensureTableSchema();
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='person'")) {
            assertTrue(rs.next(), "Person table should exist after ensureTableSchema is called");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     
     
}
