package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.EventSpeaker;
import services.ServiceEventSpeaker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherEventSpeaker {

    @FXML
    private ListView<EventSpeaker> listViewEvenementsS;
    @FXML
    private Button btnsupprimerS;
    @FXML
    private Button btnmodifier1S;

    private final ServiceEventSpeaker serviceEvent = new ServiceEventSpeaker();
    private final ObservableList<EventSpeaker> eventsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            // Charger les événements depuis la base de données
            List<EventSpeaker> events = serviceEvent.afficher();
            eventsList.addAll(events);

            // Associer les boutons à leurs actions respectives
            btnmodifier1S.setOnAction(_ -> ouvrirFenetreModificationS());
            btnsupprimerS.setOnAction(_ -> supprimerEventSpeaker());

            // Associer les données à la ListView
            listViewEvenementsS.setItems(eventsList);

            // Personnaliser l'affichage des événements
            listViewEvenementsS.setCellFactory(_ -> new EventSpeakerListCell());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouterEventS() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AjouterEventSpeaker.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un événement");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setResizable(false);

            // Rafraîchir la liste des événements après fermeture de la fenêtre
            stage.setOnHidden((WindowEvent _) -> rafraichirAffichage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AjouterEvenement.fxml");
        }
    }

    public void supprimerEventSpeaker() {
        EventSpeaker eventSelectionne = listViewEvenementsS.getSelectionModel().getSelectedItem();
        if (eventSelectionne == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un eventspeaker à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer cet événement ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                serviceEvent.supprimer(eventSelectionne.getId());
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Événement supprimé avec succès !");
                rafraichirAffichage();
            } catch (SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer l'événement : " + e.getMessage());
            }
        }
    }

    public void ouvrirFenetreModificationS() {
        EventSpeaker event = listViewEvenementsS.getSelectionModel().getSelectedItem();
        if (event == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un événement à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ModifierEventSpeaker.fxml"));
            Parent root = loader.load();

            ModifierEventSpeaker controller = loader.getController();
            controller.setEvent(event);
            controller.setHomeController(this); // Passer la référence à ModifierEvenement

            Stage stage = new Stage();
            stage.setTitle("Modifier un événement");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rafraichirAffichage() {
        try {
            List<EventSpeaker> events = serviceEvent.afficher();
            listViewEvenementsS.setItems(FXCollections.observableArrayList(events));
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Impossible de rafraîchir l'affichage : " + e.getMessage());
        }
    }

    private static void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
