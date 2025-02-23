package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import models.Event;
import services.ServiceEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherEvenement {
    @FXML
    private Button profileSettingBtn;
    @FXML
    private ListView<Event> listViewEvenements;
    @FXML
    private Button btnsupprimer;
    @FXML
    private Button btnmodifier1;
    @FXML
    private Button btnafficher;
    @FXML
    private Button btnafficherR;
    @FXML
    private Button btnafficherR1;



    private final ServiceEvent serviceEvent = new ServiceEvent();
    private final ObservableList<Event> eventsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            // Charger les événements depuis la base de données
            List<Event> events = serviceEvent.afficher();
            eventsList.addAll(events);

            // Associer les boutons à leurs actions respectives
            btnmodifier1.setOnAction(_ -> ouvrirFenetreModification());
            btnsupprimer.setOnAction(_ -> supprimerEvent());
            btnafficher.setOnAction(_ -> handleAfficherEventSpeaker());
            //btnafficherR1.setOnAction(_ -> handlereclamation());
          //  btnafficherR.setOnAction(_ -> handlereservation());



            // Associer les données à la ListView
            listViewEvenements.setItems(eventsList);

            // Personnaliser l'affichage des événements
            listViewEvenements.setCellFactory(_ -> new EvenementListCell());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouterEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AjouterEvenement.fxml"));
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
    @FXML
    private void handleAfficherEventSpeaker() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AfficherEventSpeaker.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Afficher un eventspeaker");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setResizable(false);

            // Rafraîchir la liste des événements après fermeture de la fenêtre
            stage.setOnHidden((WindowEvent _) -> rafraichirAffichage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AfficherEventSpeaker.fxml");
        }
    }
  /*  @FXML
    private void handlereservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher reservation");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setResizable(false);

            // Rafraîchir la liste des événements après fermeture de la fenêtre
            stage.setOnHidden((WindowEvent _) -> rafraichirAffichage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de Afficherreservation.fxml");
        }
    }*/
     /*  @FXML
    private void handlereclamation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher une reclamation");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setResizable(false);

            // Rafraîchir la liste des événements après fermeture de la fenêtre
            stage.setOnHidden((WindowEvent _) -> rafraichirAffichage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de Afficherreclamation.fxml");
        }
    }*/

    public void supprimerEvent() {
        Event eventSelectionne = listViewEvenements.getSelectionModel().getSelectedItem();
        if (eventSelectionne == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un événement à supprimer.");
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

    public void ouvrirFenetreModification() {
        Event event = listViewEvenements.getSelectionModel().getSelectedItem();
        if (event == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un événement à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ModifierEvenement.fxml"));
            Parent root = loader.load();

            ModifierEvenement controller = loader.getController();
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
            List<Event> events = serviceEvent.afficher();
            listViewEvenements.setItems(FXCollections.observableArrayList(events));
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
    @FXML
    void onClickProfileSetting(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/UpdateUser.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Modifier un événement");
            stage.setScene(new Scene(root));
//            stage.setMaximized(true);
//            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
