package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Reclamation;
import models.Reponse;
import services.Etat;
import services.Rate;
import services.ReclamationService;
import services.ReponseService;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class reponseAdminController implements Initializable {


    @FXML
    private Label date_time;

    @FXML
    private Button Repnse_d_UpateBtn;

    @FXML
    private Button Reponse_d_AddBtn;

    @FXML
    private Button Reponse_d_ClearBtn;

    @FXML
    private TextArea Reponse_d_Description;

    @FXML
    private ComboBox<Etat> Reponse_d_Etat;

    @FXML
    private DatePicker Reponse_d_ScheduleBtn;

    @FXML
    private TextField Reponse_d_email;



    @FXML
    private TableView<Reclamation> list_reclamation_table;

    @FXML
    private TableColumn<Reclamation, String> rec_message_coln;

    @FXML
    private TableColumn<Reclamation, String> rec_mail_coln;

    @FXML
    private TableColumn<Reclamation, Date> rec_date_coln;

    @FXML
    private TableColumn<Reclamation, Rate> rec_rate_coln;


    @FXML
    private TableView<Reponse>  list_reponse_table; // TableView pour afficher les réponses

    @FXML
    private TableColumn<Reponse, String> subject_coln;// Colonne pour le message (subject)

    @FXML
    private TableColumn<Reponse, String> date_rep_coln;  // Colonne pour la date
    @FXML
    private TableColumn<Reponse, String> etatcoln;  // Colonne pour l'état

    @FXML
    private TableColumn<?, ?> msgrec;



    @FXML
    void UpdateReponse(ActionEvent event) {
        try {
            // Load the Response Reclamation page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/UpdateRepAdminInterface.fxml"));
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
    public void addReponse(javafx.event.ActionEvent actionEvent) {
        // Validation check
        if (Reponse_d_Description.getText().isEmpty() || Reponse_d_Etat.getValue() == null || Reponse_d_ScheduleBtn.getValue() == null || Reponse_d_email.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.show();
            return;
        }

        String userEmail = Reponse_d_email.getText();      // Récupérer l'email depuis Reponse_d_email
        String message = Reponse_d_Description.getText();  // Message de la réponse
        Etat etat = (Etat) Reponse_d_Etat.getValue();      // État de la réponse (Enum)
        LocalDate date = Reponse_d_ScheduleBtn.getValue(); // Date de la réponse

        try {
            Connection connection = MyDatabase.getInstance().getConnection();

            // ✅ Récupérer l'ID utilisateur depuis son email
            String queryUser = "SELECT id FROM user WHERE email = ?";
            PreparedStatement psUser = connection.prepareStatement(queryUser);
            psUser.setString(1, userEmail);
            ResultSet rsUser = psUser.executeQuery();

            if (!rsUser.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Aucun utilisateur trouvé avec cet email.");
                alert.show();
                return;
            }
            int idUser = rsUser.getInt("id");

            // ✅ Récupérer l'ID de la dernière réclamation de cet utilisateur
            String queryReclamation = "SELECT id, subject FROM reclamation WHERE idUser = ? ORDER BY dateReclamation DESC LIMIT 1";
            PreparedStatement psReclamation = connection.prepareStatement(queryReclamation);
            psReclamation.setInt(1, idUser);
            ResultSet rsReclamation = psReclamation.executeQuery();

            if (!rsReclamation.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Aucune réclamation trouvée pour cet utilisateur.");
                alert.show();
                return;
            }
            int idRec = rsReclamation.getInt("id");
            String messageRec = rsReclamation.getString("subject"); // Récupérer le 'subject' de la réclamation

            // ✅ Vérifier si cette réclamation a déjà une réponse
            String checkResponseQuery = "SELECT COUNT(*) FROM reponse WHERE idRec = ?";
            PreparedStatement psCheckResponse = connection.prepareStatement(checkResponseQuery);
            psCheckResponse.setInt(1, idRec);
            ResultSet rsCheckResponse = psCheckResponse.executeQuery();
            rsCheckResponse.next();
            int responseCount = rsCheckResponse.getInt(1);

            if (responseCount > 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Cette réclamation a déjà une réponse.");
                alert.show();
                return;
            }

            // ✅ Création de l'objet Réponse en incluant 'messageRec'
            Reponse reponse = new Reponse(idRec, messageRec, message, java.sql.Date.valueOf(date), etat); // Passing messageRec here
            ReponseService rsService = new ReponseService();
            rsService.ajouter(reponse); // Add response to the database

            // ✅ Ajouter la nouvelle réponse à la table (TableView)
            list_reponse_table.getItems().add(reponse);

            // ✅ Confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Réponse ajoutée avec succès !");
            alert.show();

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
    void supprimerReponse(ActionEvent event) {
        if (Reponse_d_email.getText().isEmpty()) {
            showAlert("Attention", "Veuillez entrer l'adresse e-mail de l'utilisateur pour supprimer sa réponse.", Alert.AlertType.WARNING);
            return;
        }

        String userEmail = Reponse_d_email.getText().trim();

        try {
            Connection connection = MyDatabase.getInstance().getConnection();

            // 1️⃣ Récupérer l'ID de l'utilisateur à partir de son email
            String queryUser = "SELECT id FROM user WHERE email = ?";
            PreparedStatement psUser = connection.prepareStatement(queryUser);
            psUser.setString(1, userEmail);
            ResultSet rsUser = psUser.executeQuery();

            if (!rsUser.next()) {
                showAlert("Erreur", "Aucun utilisateur trouvé avec cette adresse e-mail.", Alert.AlertType.ERROR);
                return;
            }

            int userId = rsUser.getInt("id");

            // 2️⃣ Récupérer l'ID de la réclamation associée à cet utilisateur
            String queryRec = "SELECT id FROM reclamation WHERE idUser = ?";
            PreparedStatement psRec = connection.prepareStatement(queryRec);
            psRec.setInt(1, userId);
            ResultSet rsRec = psRec.executeQuery();

            if (!rsRec.next()) {
                showAlert("Erreur", "Aucune réclamation trouvée pour cet utilisateur.", Alert.AlertType.ERROR);
                return;
            }

            int recId = rsRec.getInt("id");

            // 3️⃣ Vérifier si une réponse existe pour cette réclamation
            String queryCheck = "SELECT id FROM reponse WHERE idRec = ?";
            PreparedStatement psCheck = connection.prepareStatement(queryCheck);
            psCheck.setInt(1, recId);
            ResultSet rsCheck = psCheck.executeQuery();

            if (!rsCheck.next()) {
                showAlert("Erreur", "Aucune réponse trouvée pour cette réclamation.", Alert.AlertType.ERROR);
                return;
            }

            int reponseId = rsCheck.getInt("id");

            // 4️⃣ Supprimer la réponse et tous ses champs associés
            String queryDelete = "DELETE FROM reponse WHERE id = ?";
            PreparedStatement psDelete = connection.prepareStatement(queryDelete);
            psDelete.setInt(1, reponseId);
            int rowsAffected = psDelete.executeUpdate();

            if (rowsAffected > 0) {
                // ✅ Remove the response from the TableView
                Reponse responseToRemove = null;
                for (Reponse r : list_reponse_table.getItems()) {
                    if (r.getId() == reponseId) {  // Assuming you have a getId() method
                        responseToRemove = r;
                        break;
                    }
                }
                if (responseToRemove != null) {
                    list_reponse_table.getItems().remove(responseToRemove);
                }

                // Refresh the TableView by reloading the data
                list_reponse_table.setItems(getReponses());

                showAlert("Succès", "Réponse supprimée avec succès !", Alert.AlertType.CONFIRMATION);
            } else {
                showAlert("Erreur", "Une erreur s'est produite lors de la suppression de la réponse.", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur SQL", "Une erreur s'est produite lors de l'accès à la base de données.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }


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

        if (date_time == null) {
            System.out.println("date_time is NULL! Check your FXML file for fx:id='date_time'.");
        } else {
            System.out.println("date_time is NOT null, proceeding with runTime()");
            runTime();
        }


        // Ajouter les valeurs de l'énumération Etat à la ComboBox
        ObservableList<Etat> etats = FXCollections.observableArrayList(Etat.values());
        Reponse_d_Etat.setItems(etats);

        // Définir une valeur par défaut (optionnel)
        Reponse_d_Etat.setValue(Etat.NOT_TREATED);

        // Configuration des colonnes
        subject_coln.setCellValueFactory(new PropertyValueFactory<>("message"));  // "message" pour le sujet
        date_rep_coln.setCellValueFactory(new PropertyValueFactory<>("dateRep"));
        etatcoln.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Ajouter la colonne pour msgrec
        msgrec.setCellValueFactory(new PropertyValueFactory<>("messageRec"));  // "messageRec" pour la colonne messageRec

        // Charger les données dans la TableView
        list_reponse_table.setItems(getReponses());  // Méthode pour récupérer les données de la base

        rec_message_coln.setCellValueFactory(new PropertyValueFactory<>("subject"));
        rec_mail_coln.setCellValueFactory(new PropertyValueFactory<>("email"));
        rec_date_coln.setCellValueFactory(new PropertyValueFactory<>("dateReclamation"));
        rec_rate_coln.setCellValueFactory(new PropertyValueFactory<>("rate"));

        // Fetch the data and set it to the table
        list_reclamation_table.setItems(getReclamations());
    }

    // Fetch the reclamations from the database using the existing service
    private ObservableList<Reclamation> getReclamations() {
        ReclamationService reclamationService = new ReclamationService();
        // Fetch all reclamations
        return FXCollections.observableArrayList(reclamationService.rechercher());
    }

    // Méthode pour récupérer les réponses depuis la base de données
    private ObservableList<Reponse> getReponses() {
        // Créer une ObservableList et y ajouter les réponses
        List<Reponse> reponses = new ReponseService().rechercher(); // Récupérer les données via ReponseService
        ObservableList<Reponse> observableReponses = FXCollections.observableArrayList(reponses);  // Convertir en ObservableList
        return observableReponses;
    }




}
