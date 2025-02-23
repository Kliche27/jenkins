package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.EventSpeaker;
import services.ServiceEventSpeaker;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifierEventSpeaker implements Initializable {

    @FXML
    private TextField descriptionField1;

    @FXML
    private Button modifierbtns;

    @FXML
    private TextField nomField1;

    @FXML
    private TextField prenomField1;

    @FXML
    private TextField statusField11;

    private final ServiceEventSpeaker serviceEvent = new ServiceEventSpeaker();
    private EventSpeaker eventAModifier;
    private AfficherEventSpeaker homeController; // Référence au contrôleur parent

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifierbtns.setOnAction(this::modifierEventS);
    }

    public void setEvent(EventSpeaker event) {
        this.eventAModifier = event;
        if (event != null) {
            nomField1.setText(event.getNom());
            descriptionField1.setText(event.getDescription());
            prenomField1.setText(event.getPrenom());
            statusField11.setText(String.valueOf(event.getStatus()));
        }
    }

    public void setHomeController(AfficherEventSpeaker homeController) {
        this.homeController = homeController;
    }

    @FXML
    void modifierEventS(ActionEvent event) {
        if (eventAModifier == null) {
            afficherAlerte("Erreur", "Aucun événement sélectionné !");
            return;
        }

        String nom = nomField1.getText().trim();
        String description = descriptionField1.getText().trim();
        String status = statusField11.getText().trim();
        String prenom = prenomField1.getText().trim();

        if (status.length() < 3) {
            afficherAlerte("Erreur", "Le status doit contenir au moins 3 caractères.");
            return;
        }
        if (description.length() < 10) {
            afficherAlerte("Erreur", "La description doit contenir au moins 10 caractères.");
            return;
        }
        if (!nom.isEmpty() && !Character.isUpperCase(nom.charAt(0))) {
            afficherAlerte("Erreur", "Le nom doit commencer par une majuscule !");
            return;
        }

        try {


            eventAModifier.setNom(nom);
            eventAModifier.setDescription(description);
            eventAModifier.setPrenom(prenom);
            eventAModifier.setStatus(status);

            serviceEvent.modifier(eventAModifier);
            afficherAlerte("Succès", "L'événement a été modifié avec succès !");

            if (homeController != null) {
                homeController.rafraichirAffichage();
            }

        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Impossible de modifier l'événement : " + e.getMessage());
        }
    }

    private void afficherAlerte(String titre, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
