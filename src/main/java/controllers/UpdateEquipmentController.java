package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Equipment;
import services.EquipmentService2;

import java.io.IOException;


public class UpdateEquipmentController {

    private static final String EMPTY_FIELD_WARNING_TITLE = "Champs manquants";
    private static final String EMPTY_FIELD_WARNING_CONTENT = "Veuillez remplir tous les champs.";
    private static final String INVALID_FORMAT_WARNING_TITLE = "Format non valide !";
    private static final String INVALID_FORMAT_WARNING_CONTENT = "Quantité doit etre un entier !";
    private static final String SUCCESS_TITLE = "Succès";
    private static final String SUCCESS_CONTENT = "Équipement ajouté avec succès !";
    private static final String ERROR_TITLE = "Erreur de saisie";
    private static final String ERROR_CONTENT = "La quantité doit être un nombre valide.";
    private static final String QUANTITY_PATTERN = "\\d+";

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldType;

    @FXML
    private ComboBox<String> comboBoxStatus;

    @FXML
    private TextField textFieldQuantity;

    @FXML
    private TextField textFieldDescription;

    private Equipment currentEquipment;

    // Method to receive and display the equipment data
    public void setEquipmentData(Equipment equipment) {
        this.currentEquipment = equipment;
        textFieldName.setText(equipment.getName());
        textFieldType.setText(equipment.getType());
        comboBoxStatus.setValue(equipment.getStatus()); // Fixed ComboBox value setting
        textFieldQuantity.setText(String.valueOf(equipment.getQuantity()));
        textFieldDescription.setText(equipment.getDescription());
    }

    @FXML
    void saveUpdatedEquipment(ActionEvent event) {
        // Validate input fields
        if (textFieldName.getText().isEmpty() ||
                textFieldType.getText().isEmpty() ||
                comboBoxStatus.getValue() == null ||
                textFieldQuantity.getText().isEmpty() ||
                textFieldDescription.getText().isEmpty()) {

            showErrorDialog("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        if (!textFieldQuantity.getText().matches(QUANTITY_PATTERN)) { // Validate quantity format
            showErrorDialog("Erreur de saisie", "La quantité doit être un nombre valide.");
            return;
        }

        try {
            // Update equipment object
            currentEquipment.setName(textFieldName.getText());
            currentEquipment.setType(textFieldType.getText());
            currentEquipment.setStatus(comboBoxStatus.getValue());
            currentEquipment.setQuantity(Integer.parseInt(textFieldQuantity.getText()));
            currentEquipment.setDescription(textFieldDescription.getText());

            // Call update function in service
            EquipmentService2 equipmentService = new EquipmentService2();
            equipmentService.modifier(currentEquipment);

            // Close window after saving
            // Load the next scene properly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ShowEquipement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher Équipement");
            stage.show();


            // Close current window (if using new stage)
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (NumberFormatException | IOException e) {
            showErrorDialog("Erreur de saisie", "La quantité doit être un nombre valide.");
        }
    }

    // Show error dialog
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
