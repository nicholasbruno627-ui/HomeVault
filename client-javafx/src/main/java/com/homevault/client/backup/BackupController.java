/*package com.homevault.client.backup;

import com.homevault.client.service.ApiClient;
import com.homevault.client.service.AuthSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BackupController {

    @FXML private TableView<BackupModel> backupTable;
    @FXML private TableColumn<BackupModel, String> locationColumn;
    @FXML private TableColumn<BackupModel, Long> sizeColumn;
    @FXML private TableColumn<BackupModel, String> statusColumn;
    @FXML private TableColumn<BackupModel, Instant> createdColumn;

    private final ObservableList<BackupModel> backups = FXCollections.observableArrayList();
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
        try {
            List<BackupModel> list = ApiClient.getBackups(userId);
            backups.setAll(list);
            backupTable.setItems(backups);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onCreateBackup() {
        try {
            BackupModel model = BackupModel.createLocalBackup(userId);
            ApiClient.createBackup(userId, model);
            loadBackups();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onRestoreBackup() {
        try {
            BackupModel selected = backupTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ApiClient.restoreBackup(userId, selected.getId());
                loadBackups();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
*/
