package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Event;
import services.ServiceEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModifierEvenement implements Initializable {

    @FXML
    private DatePicker dateEventField1;
    @FXML
    private TextField descriptionField1;
    @FXML
    private TextField lieuField1;
    @FXML
    private Button modifierbtn;
    @FXML
    private TextField nbPlaceField1;
    @FXML
    private TextField prixField1;
    @FXML
    private TextField titreField1;

    private final ServiceEvent serviceEvent = new ServiceEvent();
    private Event eventAModifier;
    private AfficherEvenement homeController; // Référence au contrôleur parent

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifierbtn.setOnAction(this::modifierEvent);
    }

    public void setEvent(Event event) {
        this.eventAModifier = event;
        if (event != null) {
            titreField1.setText(event.getTitre());
            descriptionField1.setText(event.getDescription());
            lieuField1.setText(event.getLieu());
            nbPlaceField1.setText(String.valueOf(event.getnbPlace()));
            prixField1.setText(String.valueOf(event.getPrix()));

            if (event.getDateEvent() != null) {
                dateEventField1.setValue(event.getDateEvent().toLocalDate());
            }
        }
    }

    public void setHomeController(AfficherEvenement homeController) {
        this.homeController = homeController;
    }

    @FXML
    void modifierEvent(ActionEvent event) {
        if (eventAModifier == null) {
            afficherAlerte("Erreur", "Aucun événement sélectionné !");
            return;
        }

        String titre = titreField1.getText().trim();
        String description = descriptionField1.getText().trim();
        String lieu = lieuField1.getText().trim();
        String nbPlaceStr = nbPlaceField1.getText().trim();
        String prixStr = prixField1.getText().trim();
        LocalDate dateEvent = dateEventField1.getValue();

        if (titre.length() < 3) {
            afficherAlerte("Erreur", "Le titre doit contenir au moins 3 caractères.");
            return;
        }
        if (description.length() < 10) {
            afficherAlerte("Erreur", "La description doit contenir au moins 10 caractères.");
            return;
        }
        if (!lieu.isEmpty() && !Character.isUpperCase(lieu.charAt(0))) {
            afficherAlerte("Erreur", "Le lieu doit commencer par une majuscule !");
            return;
        }

        try {
            int nbPlace = Integer.parseInt(nbPlaceStr);
            double prix = Double.parseDouble(prixStr);

            eventAModifier.setTitre(titre);
            eventAModifier.setDescription(description);
            eventAModifier.setLieu(lieu);
            eventAModifier.setnbPlace(nbPlace);
            eventAModifier.setPrix(prix);
            eventAModifier.setDateEvent(dateEvent.atStartOfDay());

            serviceEvent.modifier(eventAModifier);
            afficherAlerte("Succès", "Le speaker a été modifié avec succès !");

            if (homeController != null) {
                homeController.rafraichirAffichage();
            }

        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Impossible de modifier le speaker : " + e.getMessage());
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
