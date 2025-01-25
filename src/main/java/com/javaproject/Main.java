//package com.javaproject;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//public class Main extends Application {
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        //DatabaseHelper.initializeDatabase();
//
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Contact App");
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

package com.javaproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Execute the SQLite script to initialize the database
        //DatabaseHelper.executeScript("script.sql");

        // Load the JavaFX UI
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Contact App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
