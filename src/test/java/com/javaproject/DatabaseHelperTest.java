package com.javaproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    public void testInsertPerson() { // we verify that a person is added successfully to the database
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
    public void testDeletePerson() {//we verify that the person no longer exists in the database
        try (Connection conn = DatabaseHelper.connect()) {
            //new person is added to the database
            String insertSQL = "INSERT INTO person (firstName, lastName, nickname, phoneNumber, address, emailAddress, birthDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, "John");
                pstmt.setString(2, "Doe");
                pstmt.setString(3, "Johnny");
                pstmt.setString(4, "1234567890");
                pstmt.setString(5, "123 Street");
                pstmt.setString(6, "john.doe@example.com");
                pstmt.setString(7, "1990-01-01");
                pstmt.executeUpdate();

                //Retrieve the generated ID for deletion
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                int personId = -1;
                if (generatedKeys.next()) {
                    personId = generatedKeys.getInt(1);
                }

                //Delete the person using the ID
                String deleteSQL = "DELETE FROM person WHERE id = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                    deleteStmt.setInt(1, personId);
                    int rowsDeleted = deleteStmt.executeUpdate();
                    assertEquals(1, rowsDeleted, "One row should have been deleted");
                }

                //Verify the person no longer exists in the database
                String querySQL = "SELECT * FROM person WHERE id = ?";
                try (PreparedStatement queryStmt = conn.prepareStatement(querySQL)) {
                    queryStmt.setInt(1, personId);
                    try (ResultSet rs = queryStmt.executeQuery()) {
                        assertFalse(rs.next(), "The person should no longer exist in the database");
                    }
                }
            }
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    @Test
    public void testUpdatePerson() {
        try (Connection conn = DatabaseHelper.connect()) {
            //insert a person into the database
            String insertSQL = "INSERT INTO person (firstName, lastName, nickname, phoneNumber, address, emailAddress, birthDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            int personId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, "Jane");
                pstmt.setString(2, "Doe");
                pstmt.setString(3, "Janey");
                pstmt.setString(4, "9876543210");
                pstmt.setString(5, "123 Main St");
                pstmt.setString(6, "jane.doe@example.com");
                pstmt.setString(7, "1985-06-15");
                pstmt.executeUpdate();

                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    personId = generatedKeys.getInt(1);
                }
            }

            //update the person's infos
            String updateSQL = "UPDATE person SET firstName = ?, lastName = ?, nickname = ?, phoneNumber = ?, address = ?, emailAddress = ?, birthDate = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setString(1, "John");
                pstmt.setString(2, "Smith");
                pstmt.setString(3, "Johnny");
                pstmt.setString(4, "1234567890");
                pstmt.setString(5, "456 Elm St");
                pstmt.setString(6, "john.smith@example.com");
                pstmt.setString(7, "1990-01-01");
                pstmt.setInt(8, personId);
                int rowsUpdated = pstmt.executeUpdate();
                assertEquals(1, rowsUpdated, "One row should have been updated");
            }

            //verify the updated person's details
            String querySQL = "SELECT * FROM person WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
                pstmt.setInt(1, personId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        assertEquals("John", rs.getString("firstName"), "First name should be updated to 'John'");
                        assertEquals("Smith", rs.getString("lastName"), "Last name should be updated to 'Smith'");
                        assertEquals("Johnny", rs.getString("nickname"), "Nickname should be updated to 'Johnny'");
                        assertEquals("1234567890", rs.getString("phoneNumber"), "Phone number should be updated to '1234567890'");
                        assertEquals("456 Elm St", rs.getString("address"), "Address should be updated to '456 Elm St'");
                        assertEquals("john.smith@example.com", rs.getString("emailAddress"), "Email address should be updated to 'john.smith@example.com'");
                        assertEquals("1990-01-01", rs.getString("birthDate"), "Birth date should be updated to '1990-01-01'");
                    } else {
                        fail("Person not found after update");
                    }
                }
            }
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
     
     
}
