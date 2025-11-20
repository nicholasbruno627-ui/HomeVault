package com.homevault.client.backup;

import com.homevault.client.service.ApiClient;
import com.homevault.client.service.AuthSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Instant;
import java.util.UUID;

public class BackupController {

    @FXML private TableView<BackupModel> backupTable;
    @FXML private TableColumn<BackupModel, String> locationColumn;
    @FXML private TableColumn<BackupModel, Long> sizeColumn;
    @FXML private TableColumn<BackupModel, String> statusColumn;
    @FXML private TableColumn<BackupModel, Instant> createdColumn;
    @FXML private Button createBackupButton;
    @FXML private Button restoreButton;

    private final ObservableList<BackupModel> backups = FXCollections.observableArrayList();

    private final ApiClient api = new ApiClient();
    private final UUID userId = AuthSession.getInstance().getUserId();

    @FXML
    public void initialize() {
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("sizeBytes"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        loadBackups();
    }

    private void loadBackups() {
        backups.setAll(api.getBackups(userId));
        backupTable.setItems(backups);
    }

    @FXML
    private void onCreateBackup() {
        api.createBackup(userId);
        loadBackups();
    }

    @FXML
    private void onRestoreBackup() {
        BackupModel selected = backupTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            api.restoreBackup(userId, selected.getId());
            loadBackups();
        }
    }
}
