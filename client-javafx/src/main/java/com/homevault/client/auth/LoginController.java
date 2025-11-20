package com.homevault.client.auth;

import com.homevault.client.service.ApiClient;
import com.homevault.client.service.AuthSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.util.UUID;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    private final ApiClient api = new ApiClient();

    @FXML
    private void onLoginClicked(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isBlank() || password.isBlank()) {
            showError("Please enter email and password.");
            return;
        }

        try {
            //calls /auth/login endpoint
            UUID userId = api.login(email, password);

            //store session
            AuthSession.getInstance().login(userId, email);

            //show dashboard
            com.homevault.client.MainApp.showDashboard();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Login failed. Make sure the server is running.");
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Login Failed");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
