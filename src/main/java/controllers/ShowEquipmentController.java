package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Equipment;
import services.EquipmentService2;


import java.io.IOException;
import java.util.List;

public class ShowEquipmentController {

    @FXML
    private TableView<Equipment> tableViewEquipment;
    @FXML
    private TableColumn<Equipment, Integer> colId;
    @FXML
    private TableColumn<Equipment, String> colName;
    @FXML
    private TableColumn<Equipment, String> colType;
    @FXML
    private TableColumn<Equipment, String> colStatus;
    @FXML
    private TableColumn<Equipment, Integer> colQuantity;
    @FXML
    private TableColumn<Equipment, String> colDescription;
    @FXML
    private TableColumn<Equipment, Void> colActions;

    private final EquipmentService2 equipmentService = new EquipmentService2();

    @FXML
    public void initialize() {
        loadData();
    }


    private void loadData() {
        List<Equipment> equipmentList = equipmentService.rechercher();
        ObservableList<Equipment> observableList = FXCollections.observableArrayList(equipmentList);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableViewEquipment.setItems(observableList);
    }

    @FXML
    void AddEquipement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AjouterEquipement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage(); // Ensure stage is properly initialized
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Équipement");
            stage.show();
            // Close the current window
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Erreur de chargement", "Impossible d'ouvrir la fenêtre Ajouter un Équipement.");
        }
    }

    @FXML
    void UpdateEquipement(ActionEvent event) {
        Equipment selectedEquipment = tableViewEquipment.getSelectionModel().getSelectedItem();

        if (selectedEquipment == null) {
            showErrorDialog("Aucune sélection", "Veuillez sélectionner un équipement à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/UpdateEquipement.fxml"));
            Parent root = loader.load();

            // Get the controller of UpdateEquipement.fxml and pass the selected equipment
            UpdateEquipmentController updateController = loader.getController();
            updateController.setEquipmentData(selectedEquipment);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Équipement");
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Erreur de chargement", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }


    // Optional: Method to display an error dialog
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void modifyEquipment(Equipment equipment) {
        System.out.println("Modify: " + equipment.getName());
        // Open Modify Dialog (To be implemented)
    }

    private void deleteEquipment(Equipment equipment) {
        equipmentService.supprimer(equipment);
        loadData(); // Refresh TableView
    }

    @FXML
    private void deleteSelectedEquipment() {
        Equipment selectedEquipment = tableViewEquipment.getSelectionModel().getSelectedItem();

        if (selectedEquipment == null) {
            showErrorDialog("Aucun équipement sélectionné", "Veuillez sélectionner un équipement à supprimer.");
            return;
        }

        // Confirmation alert before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet équipement ?");

        // If the user confirms
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Remove from Database
                equipmentService.supprimer(selectedEquipment);

                // Remove from TableView
                tableViewEquipment.getItems().remove(selectedEquipment);

                showSuccessDialog("Équipement supprimé", "L'équipement a été supprimé avec succès.");
            }
        });
    }

    private void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
