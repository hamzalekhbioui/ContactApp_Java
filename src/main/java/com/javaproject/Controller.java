package com.javaproject;

import com.javaproject.Person;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;


public class Controller {

    @FXML
    private TableView<Person> personTable;

//    @FXML
//    private TableColumn<Person, Integer> idColumn;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;


    @FXML
    private TableColumn<Person, String> nicknameColumn;

    @FXML
    private TableColumn<Person, String> addressColumn;

    @FXML
    private TableColumn<Person, String> phoneNumberColumn;

    @FXML
    private TableColumn<Person, String> emailAddressColumn;

    @FXML
    private TableColumn<Person, String> birthDateColumn;

    @FXML
    private ObservableList<Person> personList;


    public void initialize() {
        //idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        nicknameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailAddressColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));


        // Load data from the database
        loadPersonsFromDatabase();

//        personList = FXCollections.observableArrayList(
//                new Person(1, "hamza", "lekhbioui"),
//                new Person(2, "Jane", "Smith")
//        );

    }

//    private void loadPersonsFromDatabase() {
//        ObservableList<Person> personList = FXCollections.observableArrayList();
//        String query = "SELECT * FROM person";
//
//        try (Connection conn = DatabaseHelper.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String firstName = rs.getString("firstName");
//                String lastName = rs.getString("lastName");
//                personList.add(new Person(id, firstName, lastName));
//            }
//            personTable.setItems(personList);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

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
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");
                String emailAddress = rs.getString("emailAddress");
                String birthDate = rs.getString("birthDate");

                System.out.println("Loaded person: ID=" + id + ", First Name=" + firstName + ", Last Name=" + lastName + ", Nickname=" + nickname + ", Phone Number=" + phoneNumber);


                // Add the person to the list
                personList.add(new Person(id, lastName,firstName, nickname, phoneNumber, address, emailAddress, birthDate));
            }

            // Set the TableView items
            personTable.setItems(personList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void addPerson() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Person");
        dialog.setHeaderText("Add a New Person");
        dialog.setContentText("Enter first name and last name (comma-separated):");

        dialog.showAndWait().ifPresent(input -> {
            String[] parts = input.split(",");
            if (parts.length == 7) {
                String firstName = parts[0].trim();
                String lastName = parts[1].trim();
                String nickname = parts[2].trim();
                String phoneNumber = parts[3].trim();
                String address = parts[4].trim();
                String emailAddress = parts[5].trim();
                String birthDate = parts[6].trim();

                // Insert into the database
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

                    // Reload the data from the database
                    loadPersonsFromDatabase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Invalid input format.");
            }
        });
    }



    @FXML
    public void deletePerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            String deleteSQL = "DELETE FROM person WHERE id = " + selectedPerson.getId();
            try (Connection conn = DatabaseHelper.connect(); Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteSQL);

                // Reload data from the database
                loadPersonsFromDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
