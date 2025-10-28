package com.homevault.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("HomeVault & JavaFX 21");
        stage.setScene(new Scene(new Label("Launched"), 400, 200));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}