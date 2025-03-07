package com.javaproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class Controller {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn, lastNameColumn, nicknameColumn, phoneNumberColumn, addressColumn, emailAddressColumn, birthDateColumn;

    @FXML
    private TextField firstNameField, lastNameField, nicknameField, phoneNumberField, addressField, emailAddressField, birthDateField, searchField;

    private ObservableList<Person> personList;

    public void initialize() {
        firstNameField.setPromptText("Enter First Name");
        lastNameField.setPromptText("Enter Last Name");
        nicknameField.setPromptText("Enter Nickname");
        phoneNumberField.setPromptText("Enter Phone Number (10 digits)");
        addressField.setPromptText("Enter Address");
        emailAddressField.setPromptText("Enter Email Address");
        birthDateField.setPromptText("YYYY-MM-DD");


        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        nicknameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailAddressColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        personTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormFields();
            }
        });

        loadPersonsFromDatabase();
    }

    @FXML
    public void searchPerson() {
        String query = searchField.getText().toLowerCase();
        if (query.isEmpty()) {
            // If the search field is empty, reload all persons from the database
            loadPersonsFromDatabase();
            return;
        }
        ObservableList<Person> filteredList = FXCollections.observableArrayList();
        for (Person person : personList) {
            if (person.getFirstName().toLowerCase().contains(query) ||
                person.getLastName().toLowerCase().contains(query) ||
                person.getNickname().toLowerCase().contains(query) ||
                person.getPhoneNumber().contains(query)) {
                filteredList.add(person);
            }
        }
        personTable.setItems(filteredList);
    }


    private void loadPersonsFromDatabase() {
        personList = FXCollections.observableArrayList();
        String query = "SELECT * FROM person";

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String nickname = rs.getString("nickname");
                String phoneNumber = rs.getString("phoneNumber");
                String address = rs.getString("address");
                String emailAddress = rs.getString("emailAddress");
                String birthDate = rs.getString("birthDate");
                personList.add(new Person(id, firstName, lastName, nickname, phoneNumber, address, emailAddress, birthDate));
            }
            personTable.setItems(personList);
            autoResizeColumns();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addPerson() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String nickname = nicknameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String address = addressField.getText().trim();
        String emailAddress = emailAddressField.getText().trim();
        String birthDate = birthDateField.getText().trim();

        if (validateInput(firstName, lastName, nickname, phoneNumber, emailAddress, birthDate)) {
            String insertSQL = "INSERT INTO person (firstName, lastName, nickname, phoneNumber, address, emailAddress, birthDate) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseHelper.connect();
                 PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, nickname);
                pstmt.setString(4, phoneNumber);
                pstmt.setString(5, address);
                pstmt.setString(6, emailAddress);
                pstmt.setString(7, birthDate);
                pstmt.executeUpdate();

                loadPersonsFromDatabase();
                clearFormFields();
                autoResizeColumns();
            } catch (SQLException e) {
                e.printStackTrace();
            }  
        }
    }

    private boolean validateInput(String firstName, String lastName, String nickname, String phoneNumber, String emailAddress, String birthDate) {
        resetFieldStyles(); // Reset all fields before validation
    
        boolean valid = true;
    
        if (firstName.isEmpty() || lastName.isEmpty() || nickname.isEmpty() || firstName.matches(".*[0-9].*") || lastName.matches(".*[0-9].*") || nickname.matches(".*[0-9].*")) {
            showAlert("Validation Error", "All fields must be filled!");
            valid = false;
        }
    
        // Phone number validation: Must start with 06 or 07 and have exactly 10 digits
        if (!phoneNumber.matches("^(06|07)\\d{8}$")) {
            showAlert("Validation Error", "Phone Number must start with 06 or 07 and contain exactly 10 digits!");
            phoneNumberField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            valid = false;
        }
    
        // Email format validation
        if (!emailAddress.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            showAlert("Validation Error", "Invalid Email Address format!");
            emailAddressField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            valid = false;
        }
    
        // Birth Date validation: Check if it follows YYYY-MM-DD and is not in the future
        if (!isValidBirthDate(birthDate)) {
            showAlert("Validation Error", "Birth Date must follow the format YYYY-MM-DD!");
            birthDateField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            valid = false;
        }
    
        return valid;
    }
    private boolean isValidBirthDate(String birthDate) {
        try {
            // Define the expected date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
            // Parse the input date
            LocalDate date = LocalDate.parse(birthDate, formatter);
    
            // Get the current date
            LocalDate currentDate = LocalDate.now();
    
            // Validate if birthdate is in the future
            if (date.isAfter(currentDate)) {
                return false; // Birthdate cannot be in the future
            }
    
            return true; // Valid birthdate
        } catch (DateTimeParseException e) {
            return false; // Invalid format
        }
    }
    
    
    // Reset styles before validation
    private void resetFieldStyles() {
        phoneNumberField.setStyle(""); 
        emailAddressField.setStyle("");
        birthDateField.setStyle("");
    }
    
    

    @FXML
    private void clearFormFields() {
        firstNameField.clear();
        lastNameField.clear();
        nicknameField.clear();
        phoneNumberField.clear();
        addressField.clear();
        emailAddressField.clear();
        birthDateField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void deletePerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            String deleteSQL = "DELETE FROM person WHERE id = ?";
            try (Connection conn = DatabaseHelper.connect();
                 PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                pstmt.setInt(1, selectedPerson.getId());
                pstmt.executeUpdate();
                loadPersonsFromDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void fillFormFields() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            firstNameField.setText(selectedPerson.getFirstName());
            lastNameField.setText(selectedPerson.getLastName());
            nicknameField.setText(selectedPerson.getNickname());
            phoneNumberField.setText(selectedPerson.getPhoneNumber());
            addressField.setText(selectedPerson.getAddress());
            emailAddressField.setText(selectedPerson.getEmailAddress());
            birthDateField.setText(selectedPerson.getBirthDate());
        }
    }

    @FXML
    private void modifyPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            showAlert("No Selection", "Please select a person to modify.");
            return;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String nickname = nicknameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        String emailAddress = emailAddressField.getText();
        String birthDate = birthDateField.getText();

        if (!validateInput(firstName, lastName, nickname, phoneNumber, emailAddress, birthDate)) {
            return;
        }

        String updateSQL = "UPDATE person SET firstName = ?, lastName = ?, nickname = ?, phoneNumber = ?, address = ?, emailAddress = ?, birthDate = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
            PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, firstName);   // Update firstName
            pstmt.setString(2, lastName);   // Update lastName
            pstmt.setString(3, nickname);   // Update nickname
            pstmt.setString(4, phoneNumber); // Update phoneNumber
            pstmt.setString(5, address);     // Update address
            pstmt.setString(6, emailAddress); // Update emailAddress
            pstmt.setString(7, birthDate);    // Update birthDate
            pstmt.setInt(8, selectedPerson.getId()); // WHERE condition with ID

            pstmt.executeUpdate();

            loadPersonsFromDatabase();

            personTable.refresh();

            clearFormFields();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private void autoResizeColumns() {
    personTable.getColumns().forEach(column -> {
        column.setResizable(false); 
        column.setPrefWidth(150);  
        column.setResizable(true); 
    });
}


}
