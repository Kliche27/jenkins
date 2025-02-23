package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Equipment;
import services.EquipmentService2;

import java.io.IOException;

public class AjouterEquipement {

    // Constants
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
    private Button btnAjout;

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

    @FXML
    void ajoutEquipement(ActionEvent event) {
        try {
            // Get input values
            String name = textFieldName.getText();
            String type = textFieldType.getText();
            String status = comboBoxStatus.getSelectionModel().getSelectedItem();
            String quantityText = textFieldQuantity.getText();
            String description = textFieldDescription.getText();

            // Validate fields
            if (name.isEmpty() || type.isEmpty() || status == null || quantityText.isEmpty() || description.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, EMPTY_FIELD_WARNING_TITLE, EMPTY_FIELD_WARNING_CONTENT);
                return;
            }

            if (!quantityText.matches(QUANTITY_PATTERN)) { // Validate quantity format
                showAlert(Alert.AlertType.WARNING, INVALID_FORMAT_WARNING_TITLE, INVALID_FORMAT_WARNING_CONTENT);
                return;
            }

            EquipmentService2 es = new EquipmentService2();
            if (es.findByName(name) != null) {
                showAlert(Alert.AlertType.WARNING, "Nom dupliqué", "Le nom d'équipement existe !");
                return;
            }
            // Convert quantity and add the equipment
            int quantity = Integer.parseInt(quantityText);
            es.ajouter(new Equipment(name, type, status, quantity, description));

            // Load the next scene properly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ShowEquipement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher Équipement");
            stage.show();


            // Close current window (if using new stage)
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, ERROR_TITLE, ERROR_CONTENT);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de charger l'interface.");
            e.printStackTrace();
        }
    }


    // Method to show alerts with given parameters
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
