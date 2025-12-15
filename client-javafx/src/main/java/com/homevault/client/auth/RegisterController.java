package com.homevault.client.auth;

import com.homevault.client.MainApp;
import com.homevault.client.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField emailField;
    @FXML private TextField displayNameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void onRegisterClicked() {
        try {
            String email = emailField.getText();
            String displayName = displayNameField.getText();
            String password = passwordField.getText();

            ApiClient.register(email, displayName, password);

            // back to login after successful registration
            MainApp.showLogin();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onBackToLogin() {
        MainApp.showLogin();
    }
}
