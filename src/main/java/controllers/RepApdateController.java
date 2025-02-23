package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import models.Reponse;
import services.ReponseService;
import services.Etat;
import utils.MyDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class RepApdateController {


    @FXML
    private Button ARepnse_d_UpateBtn;

    @FXML
    private TextArea AReponse_d_Description;

    @FXML
    private ComboBox<Etat> AReponse_d_Etat;

    @FXML
    private DatePicker AReponse_d_ScheduleBtn;

    @FXML
    private TextField AReponse_d_email;

    @FXML
    private Label Reponse_email1;

    @FXML
    void UpdateReponse(ActionEvent event) {
        if (AReponse_d_email.getText().isEmpty() || AReponse_d_Description.getText().isEmpty() ||
                AReponse_d_Etat.getValue() == null || AReponse_d_ScheduleBtn.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.show();
            return;
        }

        try {
            String userEmail = AReponse_d_email.getText();
            ReponseService rs = new ReponseService();

            // Étape 1 : Récupérer l'ID de la réclamation (idRec) en fonction de l'email
            String queryIdRec = "SELECT id FROM reclamation WHERE email = ?";
            PreparedStatement stmt1 = MyDatabase.getInstance().getConnection().prepareStatement(queryIdRec);
            stmt1.setString(1, userEmail);
            ResultSet rsQuery1 = stmt1.executeQuery();

            if (!rsQuery1.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Aucune réclamation trouvée pour cet email.");
                alert.show();
                return;
            }

            int id = rsQuery1.getInt("id");

            // Étape 2 : Utiliser idRec pour récupérer idRep et messageRec
            String queryDetails = "SELECT id, messageRec FROM reponse WHERE idRec = ?";
            PreparedStatement stmt2 = MyDatabase.getInstance().getConnection().prepareStatement(queryDetails);
            stmt2.setInt(1, id);
            ResultSet rsQuery2 = stmt2.executeQuery();

            if (!rsQuery2.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Aucune réponse trouvée pour cette réclamation.");
                alert.show();
                return;
            }

            int idReponse = rsQuery2.getInt("id");
            String messageRec = rsQuery2.getString("messageRec");

            // Utilisation des nouvelles valeurs saisies par l'utilisateur
            String newMessage = AReponse_d_Description.getText();
            Etat newEtat = AReponse_d_Etat.getValue();
            LocalDate newDate = AReponse_d_ScheduleBtn.getValue();

            // Création de l'objet avec les nouvelles valeurs
            Reponse updatedReponse = new Reponse(idReponse, id, messageRec, newMessage, java.sql.Date.valueOf(newDate), newEtat);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir mettre à jour cette réponse ?");

            ButtonType okButton = new ButtonType("OK");
            ButtonType cancelButton = new ButtonType("Annuler");
            alert.getButtonTypes().setAll(okButton, cancelButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == okButton) {
                    // Only update the database if "OK" is clicked
                    rs.modifier(updatedReponse);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Réponse mise à jour avec succès !");
                    successAlert.show();
                } else {
                    // Reset the fields if "Annuler" is clicked
                    AReponse_d_email.clear();
                    AReponse_d_Description.clear();
                    AReponse_d_Etat.setValue(Etat.IN_PROGRESS);
                    AReponse_d_ScheduleBtn.setValue(null);
                }
            });

        }catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur SQL");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'accès à la base de données.");
            alert.show();
        }
    }



    @FXML
    public void initialize() {
        ObservableList<Etat> etats = FXCollections.observableArrayList(Etat.values());
        AReponse_d_Etat.setItems(etats);
        AReponse_d_Etat.setValue(Etat.IN_PROGRESS);
    }
}

