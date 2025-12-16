package com.homevault.client.auth;

import com.homevault.client.MainApp;
import com.homevault.client.service.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField emailField;
    @FXML private TextField displayNameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void onRegisterClicked() {
        try {
            ApiClient.register(
                emailField.getText().trim(),
                displayNameField.getText().trim(),
                passwordField.getText()
            );

            MainApp.showLogin();

        } catch (Exception ex) {
            errorLabel.setText(ex.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void onBackToLogin() {
        MainApp.showLogin();
    }
}
