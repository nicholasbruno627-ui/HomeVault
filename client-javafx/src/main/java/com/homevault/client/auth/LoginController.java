package com.homevault.client.auth;

import com.homevault.client.MainApp;
import com.homevault.client.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void onGoToRegister() {
        MainApp.showRegister();
    }
    
    @FXML
    private void onLoginClicked() {
        try {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            //check method is being called
            System.out.println("DEBUG: onLoginClicked fired");

            ApiClient.login(email, password);

            //confirm token was assigned
            System.out.println("Token = " + ApiClient.getAuthToken());

            MainApp.showDashboard();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (errorLabel != null) {
                errorLabel.setText("Login failed: " + ex.getMessage());
            }
        }
    }
}
