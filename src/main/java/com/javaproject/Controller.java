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
    private ObservableList<Person> personList;


    public void initialize() {
        //idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

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

                System.out.println("Loaded person: ID=" + id + ", First Name=" + firstName + ", Last Name=" + lastName);


                // Add the person to the list
                personList.add(new Person(id, lastName,firstName ));
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
            if (parts.length == 2) {
                String firstName = parts[0].trim();
                String lastName = parts[1].trim();

                // Insert into the database
                String insertSQL = "INSERT INTO person (firstName, lastName) VALUES (?, ?)";
                try (Connection conn = DatabaseHelper.connect();
                     PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setString(1, firstName);
                    pstmt.setString(2, lastName);
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
