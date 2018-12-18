package com.arkasian.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent p = FXMLLoader.load(this.getClass().getResource("/main.fxml"));
        Scene main = new Scene(p);
        primaryStage.setScene(main);
        primaryStage.setTitle("Travel System");
        primaryStage.show();
    }
}
