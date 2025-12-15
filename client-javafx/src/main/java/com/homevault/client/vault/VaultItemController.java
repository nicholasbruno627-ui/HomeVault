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

    //item being edited
    private VaultItemModel editingItem;

    //refreshes table
    private Runnable refreshCallback;

    //current user ID from session
    private final UUID userId = AuthSession.getInstance().getUserId();

    //called by DashboardController after FXML load
    public void setItem(VaultItemModel item) {
        this.editingItem = item;

        if (item != null) {
            // populate fields for editing
            titleField.setText(item.getTitle());
            usernameField.setText(item.getUsername());
            secretField.setText(item.getSecret());
        }
    }

    //called by DashboardController after FXML load
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    //called when Save button is clicked (onAction="#onSaveClicked")
    @FXML
    private void onSaveClicked() {
        String title = titleField.getText() != null ? titleField.getText().trim() : "";
        String username = usernameField.getText() != null ? usernameField.getText().trim() : "";
        String secret = secretField.getText() != null ? secretField.getText().trim() : "";

        //requires title
        if (title.isEmpty()) {
            return;
        }

        try {
            if (editingItem == null) {
                //adding new item
                VaultItemModel model = new VaultItemModel();
                model.setTitle(title);
                model.setUsername(username);
                model.setSecret(secret);

                ApiClient.createVaultItem(userId, model);

            } else {
                //editing existing item
                editingItem.setTitle(title);
                editingItem.setUsername(username);
                editingItem.setSecret(secret);

                ApiClient.updateVaultItem(editingItem.getId(), editingItem);
            }

            //asks dashboard to refresh the table
            if (refreshCallback != null) {
                refreshCallback.run();
            }

            //close the window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
