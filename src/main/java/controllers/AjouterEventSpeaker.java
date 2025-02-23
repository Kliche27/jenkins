package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.EventSpeaker;
import services.ServiceEventSpeaker;
import java.sql.SQLException;

public class AjouterEventSpeaker {

    @FXML
    private Button ajouterbtns;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField statusField1;
    @FXML
    private final ServiceEventSpeaker serviceEventSpeaker = new ServiceEventSpeaker();
    @FXML
    public void initialize() {
        ajouterbtns.setOnAction(_ -> ajouterEventS());
    }
    @FXML
    private void ajouterEventS() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String description = descriptionField.getText();
        String status = statusField1.getText();
        // Vérification de la longueur du nom et de la description
        if (status.length() < 3) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", " status doit comporter au moins 3 caractères.");
            return;
        }
        if (!nom.isEmpty() && !Character.isUpperCase(nom.charAt(0))) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le nom doit commencer par une majuscule !");
            return;
        }

        if (description.length() < 10) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description doit comporter au moins 10 caractères.");
            return;
        }

        EventSpeaker Event = new EventSpeaker(nom,prenom, description, status);
        try {
            serviceEventSpeaker.ajouter(Event);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Speaker ajouté avec succès !");
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter le Speaker : " + e.getMessage());
        }
    }
    @FXML
    private void afficherAlerte(Alert.AlertType type, String nom, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(nom);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}