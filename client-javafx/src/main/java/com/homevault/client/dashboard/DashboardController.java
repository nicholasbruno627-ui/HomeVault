package com.homevault.client.dashboard;

import com.homevault.client.MainApp;
import com.homevault.client.service.ApiClient;
import com.homevault.client.service.AuthSession;
import com.homevault.client.vault.VaultItemController;
import com.homevault.client.vault.VaultItemModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DashboardController {

    @FXML private TableView<VaultItemModel> vaultTable;
    @FXML private TableColumn<VaultItemModel, String> titleColumn;
    @FXML private TableColumn<VaultItemModel, String> usernameColumn;
    @FXML private TableColumn<VaultItemModel, String> secretColumn;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    //@FXML private Button backupButton;
    @FXML private Button logoutButton;

    private final ObservableList<VaultItemModel> vaultItems = FXCollections.observableArrayList();
    private final UUID userId = AuthSession.getInstance().getUserId();

    @FXML
    public void initialize() {
        
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        
        secretColumn.setCellValueFactory(new PropertyValueFactory<>("secret"));

        //show button for secret column
        secretColumn.setCellFactory(col -> new TableCell<>() {
            private final Button showButton = new Button("Show");

            {
                showButton.getStyleClass().add("show-secret-button");
            }

            @Override
            protected void updateItem(String ignored, boolean empty) {
                super.updateItem(ignored, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                showButton.setOnAction(evt -> {
                    VaultItemModel item = getTableView().getItems().get(getIndex());
                    if (item == null) return;

                    //get secret from server on demand
                    String secret = ApiClient.getVaultItemSecret(item.getId());
                    if (secret == null || secret.isBlank()) {
                        secret = "(no secret returned)";
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Secret");
                    alert.setHeaderText("Secret for: " + item.getTitle());
                    alert.setContentText(secret);
                    alert.initOwner(getTableView().getScene().getWindow());
                    alert.showAndWait();
                });

                setGraphic(showButton);
                setText(null);
            }
        });

        loadVaultItems();
    }

    private void loadVaultItems() {
        try {
            List<VaultItemModel> list = ApiClient.getVaultItems(userId);
            vaultItems.setAll(list);
            vaultTable.setItems(vaultItems);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //add
    @FXML
    private void onAddItem() {
        openVaultItemDialog(null);
    }

    //edit
    @FXML
    private void onEditItem() {
        VaultItemModel selected = vaultTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openVaultItemDialog(selected);
        }
    }

    //delete
    @FXML
    private void onDeleteItem() {
        VaultItemModel selected = vaultTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                ApiClient.deleteVaultItem(selected.getId());
                loadVaultItems();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //vault item dialog
    private void openVaultItemDialog(VaultItemModel item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/homevault/client/vault-item-dialog.fxml"));
            Scene scene = new Scene(loader.load());

            VaultItemController controller = loader.getController();
            controller.setItem(item);
            controller.setRefreshCallback(this::loadVaultItems);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(item == null ? "Add Vault Item" : "Edit Vault Item");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //logout
    @FXML
    private void onLogout() {
        AuthSession.getInstance().clear();
        MainApp.showLogin();
    }
}
