package com.javaproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    private TextField firstNameField, lastNameField, nicknameField, phoneNumberField, addressField, emailAddressField, birthDateField;


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

    private void loadPersonsFromDatabase() {
        personList = FXCollections.observableArrayList();
        String query = "SELECT * FROM person";

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                personList.add(new Person(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("nickname"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("emailAddress"),
                        rs.getString("birthDate")
                ));
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
        if (firstName.isEmpty() || lastName.isEmpty() || nickname.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled!");
            return false;
        }
        if (!phoneNumber.matches("\\d+")) {
            showAlert("Validation Error", "Phone Number must contain only digits!");
            return false;
        }
        if (!emailAddress.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            showAlert("Validation Error", "Invalid Email Address format!");
            return false;
        }
        if (!birthDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            showAlert("Validation Error", "Birth Date must follow the format YYYY-MM-DD!");
            return false;
        }
        return true;
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

        // Validate input before saving changes
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
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, nickname);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, address);
            pstmt.setString(6, emailAddress);
            pstmt.setString(7, birthDate);
            pstmt.setInt(8, selectedPerson.getId());
            pstmt.executeUpdate();

            // Reload data
            loadPersonsFromDatabase();
            // Clear form fields
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
