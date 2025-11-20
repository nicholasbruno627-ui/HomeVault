package com.homevault.client.vault;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class DashboardController {


    @FXML
    private TextField searchField;

    @FXML
    private TableView<VaultItemRow> vaultTable;

    @FXML
    private TableColumn<VaultItemRow, String> titleCol;

    @FXML
    private TableColumn<VaultItemRow, String> usernameCol;

    @FXML
    private Label statusLabel;


    private final ObservableList<VaultItemRow> masterData =
            FXCollections.observableArrayList();

    private FilteredList<VaultItemRow> filteredData;

    //initialization
    @FXML
    private void initialize() {
        //map the columns to VaultItemRow fields
        titleCol.setCellValueFactory(this::titleCellValueFactory);
        usernameCol.setCellValueFactory(this::usernameCellValueFactory);

        //wrap master list in a FilteredList for searching
        filteredData = new FilteredList<>(masterData, item -> true);

        // show all items if no text in field
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String filter = newText == null ? "" : newText.trim().toLowerCase();
            filteredData.setPredicate(item -> {
                if (filter.isEmpty()) {
                    return true;
                }
                return (item.getTitle() != null && item.getTitle().toLowerCase().contains(filter))
                        || (item.getUsername() != null && item.getUsername().toLowerCase().contains(filter));
            });
        });

        // wrap filtered datafor table sorting
        SortedList<VaultItemRow> sorted = new SortedList<>(filteredData);
        sorted.comparatorProperty().bind(vaultTable.comparatorProperty());
        vaultTable.setItems(sorted);

        //initial data ( still in work)
        loadVaultItems();
    }

    private ObservableValue<String> titleCellValueFactory(TableColumn.CellDataFeatures<VaultItemRow, String> cell) {
        return new SimpleStringProperty(cell.getValue().getTitle());
    }

    private ObservableValue<String> usernameCellValueFactory(TableColumn.CellDataFeatures<VaultItemRow, String> cell) {
        return new SimpleStringProperty(cell.getValue().getUsername());
    }

   
    //buttons
    @FXML
    private void onLogout(ActionEvent event) {

        try {
            
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/homevault/client/auth/login-view.fxml")
            );
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("HomeVault - Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            //close the window if we can't go back to login
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void onAddItem() {
        Dialog<VaultItemRow> dialog = createVaultItemDialog("Add Vault Item", null);

        Optional<VaultItemRow> result = dialog.showAndWait();
        result.ifPresent(item -> {
            masterData.add(item);
            setStatus("Added item: " + item.getTitle());
            // ****TODO: send "create vault item" request to server here****
        });
    }

    @FXML
    private void onEditItem() {
        VaultItemRow selected = vaultTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("No selection", "Please select an item to edit.");
            return;
        }

        Dialog<VaultItemRow> dialog = createVaultItemDialog("Edit Vault Item", selected);

        Optional<VaultItemRow> result = dialog.showAndWait();
        result.ifPresent(edited -> {
            // update the existing row
            selected.setTitle(edited.getTitle());
            selected.setUsername(edited.getUsername());
            //force table to refresh
            vaultTable.refresh();
            setStatus("Updated item: " + selected.getTitle());
            // ******TODO: send "update vault item" request to server here******
        });
    }

    @FXML
    private void onDeleteItem() {
        VaultItemRow selected = vaultTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("No selection", "Please select an item to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Vault Item");
        confirm.setHeaderText("Delete \"" + selected.getTitle() + "\"?");
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            masterData.remove(selected);
            setStatus("Deleted item: " + selected.getTitle());
            // *******TODO: send "delete vault item" request to server here*********
        }
    }


    private void loadVaultItems() {
        //doesn't call to backend yet, static data
        masterData.clear();
        masterData.add(new VaultItemRow(uuid(), "School Email", "nbruno10@my.brookdalecc.edu"));
        masterData.add(new VaultItemRow(uuid(), "Steam Username", "nickbruno627"));

        setStatus("Loaded " + masterData.size() + " items.");
    }


    private Dialog<VaultItemRow> createVaultItemDialog(String title, VaultItemRow existing) {
        Dialog<VaultItemRow> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField();
        TextField usernameField = new TextField();

        if (existing != null) {
            titleField.setText(existing.getTitle());
            usernameField.setText(existing.getUsername());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Username:"), 0, 1);
        grid.add(usernameField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        //if box is empty save won't work
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(titleField.getText().trim().isEmpty());

        titleField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String id = existing != null ? existing.getId() : uuid();
                return new VaultItemRow(
                        id,
                        titleField.getText().trim(),
                        usernameField.getText().trim()
                );
            }
            return null;
        });

        return dialog;
    }

    private void setStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String uuid() {
        return UUID.randomUUID().toString();
    }

  
    public static class VaultItemRow {
        private String id;
        private String title;
        private String username;

        public VaultItemRow(String id, String title, String username) {
            this.id = id;
            this.title = title;
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) { this.id = id; }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) { this.title = title; }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) { this.username = username; }
    }
}
