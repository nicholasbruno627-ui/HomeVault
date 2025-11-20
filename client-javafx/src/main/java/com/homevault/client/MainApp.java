package com.homevault.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("HomeVault");

        showLogin();
    }

    
    private static void loadScene(String fxmlFile, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/com/homevault/client/" + fxmlFile)
            );

            Scene scene = new Scene(loader.load(), width, height);

            scene.getStylesheets().add(
                MainApp.class.getResource("/com/homevault/client/application.css")
                    .toExternalForm()
            );

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    public static void showLogin() {
        loadScene("login-view.fxml", 380, 420);
    }

    
    public static void showDashboard() {
        loadScene("dashboard-view.fxml", 900, 600);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
