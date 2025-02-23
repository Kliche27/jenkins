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

public class Afficheruser {
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
    @FXML
    private Button myReservationBtn;
    @FXML
    private Button reclamationBtn;



    private final ServiceEvent serviceEvent = new ServiceEvent();
    private final ObservableList<Event> eventsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            // Charger les événements depuis la base de données
            List<Event> events = serviceEvent.afficher();
            eventsList.addAll(events);

            // Associer les boutons à leurs actions respectives
          //  btnafficherR1.setOnAction(_ -> handlereclamation());
            btnafficherR.setOnAction(_ -> handlereservation());
            myReservationBtn.setOnAction(_ -> handlemesreservation());



            // Associer les données à la ListView
            listViewEvenements.setItems(eventsList);

            // Personnaliser l'affichage des événements
            listViewEvenements.setCellFactory(_ -> new EvenementListCell());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlereservation() {
        // Vérifie si un événement est sélectionné
        Event selectedEvent = listViewEvenements.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un événement avant de réserver.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AjouterReservation.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre de réservation
            AddReservationController controller = loader.getController();
            controller.setEvent(selectedEvent); // Passe l'objet Event

            Stage stage = new Stage();
            stage.setTitle("Réservation de l'événement");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            stage.setOnHidden((WindowEvent _) -> rafraichirAffichage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AjouterReservation.fxml");
        }
    }
    @FXML
    private void handlemesreservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AfficherReservation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher une reservation");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setResizable(false);

            // Rafraîchir la liste des événements après fermeture de la fenêtre
            stage.setOnHidden((WindowEvent _) -> rafraichirAffichage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de Afficherreservaion.fxml");
        }
    }

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
    void reclamationOnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/UtilisateurReclamation.fxml"));
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
