package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Reclamation;
import services.Rate;
import services.ReclamationService;
import utils.MyDatabase;
import utils.SessionManager;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class UtilisateurReclamationController implements Initializable {

    @FXML
    private Label date_time;

    @FXML
    private TextArea Reclamation_d_DescriptionBtn;

    @FXML
    private TextField Reclamation_d_iduser;

    @FXML
    private TextField Reclamation_d_EventNameBtn;

    @FXML
    private ComboBox<Rate> Reclamation_d_RateBtn;

    @FXML
    private DatePicker Reclamation_d_ScheduleBtn;

    @FXML
    private Label Reclamation_ad_DateBtn;

    @FXML
    private Label Reclamation_ad_AdresseBtn;

    @FXML
    private Label Reclamation_ad_DescriptionBtn;

    @FXML
    private Label Reclamation_ad_EvNameBtn;

    @FXML
    private Label Reclamation_ad_FnameBtn;

    @FXML
    private Label Reclamation_ad_LnameBtn;

    @FXML
    private Label Reclamation_ad_RateBtn;

    @FXML
    private Button Reclamation_ad_SendBtn;


    @FXML
    private Button Reclamation_d_ComfirmBtn;

    public void runTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            date_time.setText(format.format(new Date()));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing UtilisateurReclamationController...");
        Reclamation_d_iduser.setText(SessionManager.getInstance().getCurrentUser().getEmail());
        if (date_time == null) {
            System.out.println("date_time is NULL! Check your FXML file for fx:id='date_time'.");
        } else {
            System.out.println("date_time is NOT null, proceeding with runTime()");
            runTime();
        }

        // ✅ Correctly populate the ComboBox with Rate values
        ObservableList<Rate> rates = FXCollections.observableArrayList(Rate.values());
        Reclamation_d_RateBtn.setItems(rates);

        // ✅ Set default selection
        Reclamation_d_RateBtn.setValue(Rate.ONE);

        // Adding a listener to ensure the description doesn't exceed 250 characters
        Reclamation_d_DescriptionBtn.textProperty().addListener((observable, oldText, newText) -> {
            if (newText.length() > 250) {
                // If the length exceeds 250 characters, trim the text
                Reclamation_d_DescriptionBtn.setText(newText.substring(0, 250));
            }
        });
    }



    @FXML
    public void supprimerReclamation(javafx.event.ActionEvent actionEvent) {
        if (Reclamation_d_iduser.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer l'adresse de l'utilisateur pour supprimer sa réclamation.");
            alert.show();
            return;
        }

        String userAddress = Reclamation_d_iduser.getText();

        try {
            Connection connection = MyDatabase.getInstance().getConnection();

            // 1. Retrieve user ID based on the given address
            String queryUser = "SELECT id FROM user WHERE email = ?";
            PreparedStatement psUser = connection.prepareStatement(queryUser);
            psUser.setString(1, userAddress);
            ResultSet rsUser = psUser.executeQuery();

            if (!rsUser.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Aucun utilisateur trouvé avec cette adresse.");
                alert.show();
                return;
            }

            int userId = rsUser.getInt("id");

            // 2. Check if a reclamation exists for the retrieved user ID
            ReclamationService rs = new ReclamationService();
            List<Reclamation> reclamations = rs.rechercher();
            boolean exists = reclamations.stream().anyMatch(r -> r.getIdUser() == userId);

            if (!exists) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Aucune réclamation trouvée pour cet utilisateur.");
                alert.show();
                return;
            }

            // 3. Display confirmation alert with an option to cancel
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");

            // ButtonType.CANCEL is added here for the cancel option
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            // Show the alert and capture the user's response
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                // Proceed with deletion if the user clicks OK
                String queryDelete = "DELETE FROM reclamation WHERE idUser = ?";
                PreparedStatement psDelete = connection.prepareStatement(queryDelete);
                psDelete.setInt(1, userId);
                int rowsAffected = psDelete.executeUpdate();

                if (rowsAffected > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Réclamation supprimée avec succès !");
                    successAlert.show();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Une erreur s'est produite lors de la suppression de la réclamation.");
                    errorAlert.show();
                }
            } else {
                // If CANCEL is pressed, do nothing
                System.out.println("Suppression annulée");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur SQL");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'accès à la base de données.");
            alert.show();
        }
    }



    @FXML
    public void updateReclamation(javafx.event.ActionEvent actionEvent) {
        try {
            // Load the Response Reclamation page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/UpdateRecUtilisateurinterface.fxml"));
            Parent root = loader.load();

            // Create a new stage (window)
            Stage newStage = new Stage();
            newStage.setTitle("Response Reclamation");  // Optionally set the title for the new window
            newStage.setScene(new Scene(root));

            // Show the new window without closing the current one
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Print error if FXML not found
        }

    }


    @FXML
    public void afficherDetailsReclamation(javafx.event.ActionEvent actionEvent) {
        if (Reclamation_d_iduser.getText().isEmpty() || Reclamation_d_EventNameBtn.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer une adresse utilisateur et un titre d'événement.");
            alert.show();
            return;
        }

        String userAddress = Reclamation_d_iduser.getText(); // Adresse de l'utilisateur
        String eventTitle = Reclamation_d_EventNameBtn.getText(); // Titre de l'événement
        String description = Reclamation_d_DescriptionBtn.getText();
        String schedule = Reclamation_d_ScheduleBtn.getValue() != null ? Reclamation_d_ScheduleBtn.getValue().toString() : "";

        // Vérification que la description et la date ne sont pas vides
        if (description.isEmpty() || schedule.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("La description et la date ne doivent pas être vides.");
            alert.show();
            return;
        }

        // Vérification de la longueur de la description (max 40 caractères)
        if (description.length() > 40) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("La description ne doit pas dépasser 40 caractères.");
            alert.show();
            return;
        }

        try {
            Connection connection = MyDatabase.getInstance().getConnection();

            // ✅ Récupérer les informations de l'utilisateur à partir de son adresse
            String queryUser = "SELECT id, nom, prenom, email FROM user WHERE email = ?";
            PreparedStatement psUser = connection.prepareStatement(queryUser);
            psUser.setString(1, userAddress);
            ResultSet rsUser = psUser.executeQuery();

            if (!rsUser.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Aucun utilisateur trouvé avec cette adresse.");
                alert.show();
                return;
            }

            int idUser = rsUser.getInt("id");
            String lastName = rsUser.getString("nom");
            String firstName = rsUser.getString("prenom");
            String retrievedAddress = rsUser.getString("email");

            // ✅ Affichage des informations utilisateur
            Reclamation_ad_AdresseBtn.setText(retrievedAddress);
            Reclamation_ad_LnameBtn.setText(lastName);
            Reclamation_ad_FnameBtn.setText(firstName);

            // ✅ Récupérer l'ID et le titre de l'événement à partir de son nom
            String queryEvent = "SELECT id, titre FROM evenement WHERE titre = ?";
            PreparedStatement psEvent = connection.prepareStatement(queryEvent);
            psEvent.setString(1, eventTitle);
            ResultSet rsEvent = psEvent.executeQuery();

            if (!rsEvent.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Aucun événement trouvé avec ce titre.");
                alert.show();
                return;
            }

            int idEvent = rsEvent.getInt("id");
            String retrievedEventTitle = rsEvent.getString("titre");

            // ✅ Affichage du titre de l'événement
            Reclamation_ad_EvNameBtn.setText(retrievedEventTitle);

            // ✅ Affichage des autres informations de la réclamation
            Reclamation_ad_DescriptionBtn.setText(description);
            Reclamation_ad_RateBtn.setText(String.valueOf(Reclamation_d_RateBtn.getValue()));
            Reclamation_ad_DateBtn.setText(schedule);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur SQL");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'accès à la base de données.");
            alert.show();
        }
    }




    public void ajouterReclamation1(javafx.event.ActionEvent actionEvent) {

        // Récupérer les valeurs depuis les labels mis à jour par afficherDetailsReclamation
        String description = Reclamation_ad_DescriptionBtn.getText();
        String rateStr = Reclamation_ad_RateBtn.getText();
        String dateStr = Reclamation_ad_DateBtn.getText();
        String email = Reclamation_ad_AdresseBtn.getText(); // L'adresse email récupérée
        String idEventStr = Reclamation_ad_EvNameBtn.getText(); // Le nom de l'événement récupéré

        // Vérification des champs
        if (description.isEmpty() || rateStr.isEmpty() || dateStr.isEmpty() || email.isEmpty() || idEventStr.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avant d'ajouter la réclamation.");
            alert.show();
            return;
        }

        try {
            // Récupérer la connexion
            Connection connection = MyDatabase.getInstance().getConnection();

            // Vérifier si une réclamation existe déjà pour cet email
            String checkQuery = "SELECT COUNT(*) FROM reclamation WHERE email = ?";
            PreparedStatement psCheck = connection.prepareStatement(checkQuery);
            psCheck.setString(1, email);
            ResultSet rsCheck = psCheck.executeQuery();

            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Il existe déjà une réclamation associée à cet email.");
                alert.show();
                return;
            }

            // Récupérer l'ID utilisateur à partir de l'adresse email
            String queryUser = "SELECT id FROM user WHERE email = ?";
            PreparedStatement psUser = connection.prepareStatement(queryUser);
            psUser.setString(1, email);
            ResultSet rsUser = psUser.executeQuery();

            if (!rsUser.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Utilisateur introuvable.");
                alert.show();
                return;
            }

            int idUser = rsUser.getInt("id");

            // Récupérer l'ID de l'événement à partir de son titre
            String queryEvent = "SELECT id FROM evenement WHERE titre = ?";
            PreparedStatement psEvent = connection.prepareStatement(queryEvent);
            psEvent.setString(1, idEventStr);
            ResultSet rsEvent = psEvent.executeQuery();

            if (!rsEvent.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Événement introuvable.");
                alert.show();
                return;
            }

            int idEvent = rsEvent.getInt("id");

            // Construire le subject avec le format demandé (Nom de l'événement : Description)
            String subject = idEventStr + " : " + description;

            // Convertir le Rate en Enum si nécessaire
            Rate rate = Rate.valueOf(rateStr.toUpperCase());

            // Convertir la date
            LocalDate localDate = LocalDate.parse(dateStr);
            java.util.Date dateReclamation = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Création de l'objet Reclamation
            Reclamation reclamation = new Reclamation(idUser, idEvent, subject, email, dateReclamation, rate);

            // Appel au service pour ajouter la réclamation
            ReclamationService service = new ReclamationService();
            service.ajouter(reclamation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Votre réclamation a été envoyée. Vous recevrez une réponse prochainement par email.");
            alert.show();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur SQL");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'ajout de la réclamation.");
            alert.show();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Valeur de taux invalide !");
            alert.show();
        }
    }


}







