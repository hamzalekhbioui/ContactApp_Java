package com.javaproject;

import com.javaproject.Person;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Controller {

    @FXML
    private TableView<Person> personTable;

    @FXML
    private TableColumn<Person, Integer> idColumn;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        // Load data from the database
        loadPersonsFromDatabase();

//        personList = FXCollections.observableArrayList(
//                new Person(1, "hamza", "lekhbioui"),
//                new Person(2, "Jane", "Smith")
//        );

    }

    private void loadPersonsFromDatabase() {
        ObservableList<Person> personList = FXCollections.observableArrayList();
        String query = "SELECT * FROM person";

        try (Connection conn = DatabaseHelper.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                personList.add(new Person(id, firstName, lastName));
            }
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
        dialog.setContentText("Enter first and last name (comma-separated):");

        dialog.showAndWait().ifPresent(input -> {
            String[] parts = input.split(",");
            if (parts.length == 2) {
                String firstName = parts[0].trim();
                String lastName = parts[1].trim();

                // Insert into database
                String insertSQL = "INSERT INTO person (firstName, lastName) VALUES ('" + firstName + "', '" + lastName + "')";
                try (Connection conn = DatabaseHelper.connect(); Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(insertSQL);

                    // Reload data from the database
                    loadPersonsFromDatabase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
