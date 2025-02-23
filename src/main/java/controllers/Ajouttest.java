package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Event;
import services.ServiceEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
public class Ajouttest {

    @FXML
    private DatePicker dateEventField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField lieuField;

    @FXML
    private TextField nbPlaceField;

    @FXML
    private TextField prixField;

    @FXML
    private TextField titreField;
    @FXML
    private final ServiceEvent serviceEvent = new ServiceEvent();
    @FXML
    private Button ajouterbtn;

    @FXML
    public void initialize() {
        ajouterbtn.setOnAction(_ -> ajouterEvent());
    }
    @FXML
    private void ajouterEvent() {
        String titre = titreField.getText();
        int nbPlace = nbPlaceField.getText().isEmpty() ? 0 : Integer.parseInt(nbPlaceField.getText());
        String description = descriptionField.getText();
        double  prix = Double.parseDouble(prixField.getText());
        String lieu = lieuField.getText();
        LocalDateTime dateEvent = dateEventField.getValue() != null ? dateEventField.getValue().atStartOfDay() : null;

        // Vérification de la longueur du nom et de la description
        if (titre.length() < 3) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le nom doit comporter au moins 3 caractères.");
            return;
        }
        if (!lieu.isEmpty() && !Character.isUpperCase(lieu.charAt(0))) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le lieu doit commencer par une majuscule !");
            return;
        }

        if (description.length() < 10) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description doit comporter au moins 10 caractères.");
            return;
        }

        Event Event = new Event(titre,description, dateEvent, prix, lieu, nbPlace);
        try {
            serviceEvent.ajouter(Event);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Event ajouté avec succès !");
        } catch (SQLException e){
            String erreurMessage = e.getMessage();

            // Vérifier si le message d'erreur contient l'information sur l'absence de speaker
            if (erreurMessage.contains("Aucun eventspeaker disponible")) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Il n'y a pas de speaker disponible !");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Impossible d'ajouter l'événement : " + erreurMessage);
            }
        }
    }
    @FXML
    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
