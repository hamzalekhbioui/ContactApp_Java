package com.javaproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class Controller {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn, lastNameColumn;
    @FXML
    private TextField firstNameField, lastNameField, nicknameField, phoneNumberField, addressField, emailAddressField, birthDateField, searchField, categoryField;
    private ObservableList<Person> personList;

    public void initialize() {
        firstNameField.setPromptText("Enter First Name");
        lastNameField.setPromptText("Enter Last Name");
        nicknameField.setPromptText("Enter Nickname");
        phoneNumberField.setPromptText("Enter Phone Number (10 digits)");
        addressField.setPromptText("Enter Address");
        emailAddressField.setPromptText("Enter Email Address");
        birthDateField.setPromptText("YYYY-MM-DD");
        categoryField.setPromptText("Friend, Family, or Work");

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

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
            loadPersonsFromDatabase();
            return;
        }
        ObservableList<Person> filteredList = FXCollections.observableArrayList();
        filteredList.addAll(personList.stream()
            .filter(person -> person.getFirstName().toLowerCase().contains(query) ||
                             person.getLastName().toLowerCase().contains(query) ||
                             person.getNickname().toLowerCase().contains(query) ||
                             person.getPhoneNumber().contains(query))
            .collect(Collectors.toList()));
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
                String category = rs.getString("category");
                personList.add(new Person(id, firstName, lastName, nickname, phoneNumber, address, emailAddress, birthDate, category));
            }
            personTable.setItems(personList);
            autoResizeColumns();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
public void addPerson() {
    String[] fields = {
        firstNameField.getText().trim(),
        lastNameField.getText().trim(),
        nicknameField.getText().trim(),
        phoneNumberField.getText().trim(),
        addressField.getText().trim(),
        emailAddressField.getText().trim(),
        birthDateField.getText().trim(),
        categoryField.getText().trim()
    };

    if (validateInput(fields[0], fields[1], fields[2], fields[3], fields[5], fields[6])) {
        String insertSQL = "INSERT INTO person (firstName, lastName, nickname, phoneNumber, address, emailAddress, birthDate, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (int i = 0; i < fields.length; i++) {
                pstmt.setString(i + 1, fields[i]);
            }
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
    resetFieldStyles(); 

    boolean valid = true;

    List<BiPredicate<String, String>> validationRules = Arrays.asList(
        (field, message) -> !field.isEmpty(),
        (field, message) -> !field.matches(".*[0-9].*"),
        (field, message) -> phoneNumber.matches("^(06|07)\\d{8}$"),
        (field, message) -> emailAddress.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"),
        (field, message) -> isValidBirthDate(birthDate)
    );

    String[] messages = {
        "All fields must be filled!",
        "Names cannot contain numbers!",
        "Phone Number must start with 06 or 07 and contain exactly 10 digits!",
        "Invalid Email Address format!",
        "Birth Date must follow the format YYYY-MM-DD!"
    };

    for (int i = 0; i < validationRules.size(); i++) {
        if (!validationRules.get(i).test(i < 3 ? firstName : i == 3 ? phoneNumber : emailAddress, messages[i])) {
            showAlert("Validation Error", messages[i]);
            valid = false;
        }
    }

    return valid;
}
    private boolean isValidBirthDate(String birthDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(birthDate, formatter);
            LocalDate currentDate = LocalDate.now();
            if (date.isAfter(currentDate)) {
                return false; 
            }
            return true; 
        } catch (DateTimeParseException e) {
            return false; 
        }
    }
    
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
        categoryField.clear();
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
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this contact?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.NO) {
            return; 
        }
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
            categoryField.setText(selectedPerson.getCategory());
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
        String category = categoryField.getText();

        if (!validateInput(firstName, lastName, nickname, phoneNumber, emailAddress, birthDate)) {
            return;
        }

        String updateSQL = "UPDATE person SET firstName = ?, lastName = ?, nickname = ?, phoneNumber = ?, address = ?, emailAddress = ?, birthDate = ?, category = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
            PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, firstName);   
            pstmt.setString(2, lastName);   
            pstmt.setString(3, nickname);   
            pstmt.setString(4, phoneNumber); 
            pstmt.setString(5, address);     
            pstmt.setString(6, emailAddress); 
            pstmt.setString(7, birthDate);   
            pstmt.setString(8, category);    
            pstmt.setInt(9, selectedPerson.getId()); 

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
