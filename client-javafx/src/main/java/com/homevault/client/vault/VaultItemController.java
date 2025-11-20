package com.homevault.client.vault;

import com.homevault.client.service.ApiClient;
import com.homevault.client.service.AuthSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.UUID;

public class VaultItemController {

    @FXML private TextField titleField;
    @FXML private TextField usernameField;
    @FXML private TextField secretField;
    @FXML private Button saveButton;

    private VaultItemModel editingItem;
    private Runnable refreshCallback;

    private final ApiClient api = new ApiClient();
    private final UUID userId = AuthSession.getInstance().getUserId();

    public void setItem(VaultItemModel item) {
        this.editingItem = item;
        if (item != null) {
            titleField.setText(item.getTitle());
            usernameField.setText(item.getUsername());
            secretField.setText(item.getSecret());
        }
    }

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    @FXML
    private void onSave() {
        String title = titleField.getText();
        String username = usernameField.getText();
        String secret = secretField.getText();

        if (editingItem == null) {
            // create new
            api.createVaultItem(userId, title, username, secret);
        } else {
            // update
            api.updateVaultItem(userId, editingItem.getId(), title, username, secret);
        }

        if (refreshCallback != null) refreshCallback.run();

        closeWindow();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
