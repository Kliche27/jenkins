package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Reclamation;
import services.Rate;
import services.ReclamationService;
import utils.MyDatabase;
import utils.SessionManager;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RecUpdateController implements Initializable {

    @FXML
    private TextArea AReclamation_d_DescriptionBtn;

    @FXML
    private TextField AReclamation_d_EventNameBtn;

    @FXML
    private ComboBox<Rate> AReclamation_d_RateBtn;

    @FXML
    private DatePicker AReclamation_d_ScheduleBtn;

    @FXML
    private Button AReclamation_d_UpdateBtn;

    @FXML
    private TextField AReclamation_d_iduser;

    @FXML
    public void updateReclamation(ActionEvent event) {
        if (AReclamation_d_iduser.getText().isEmpty() || AReclamation_d_EventNameBtn.getText().isEmpty() ||
                AReclamation_d_DescriptionBtn.getText().isEmpty() || AReclamation_d_RateBtn.getValue() == null ||
                AReclamation_d_ScheduleBtn.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.show();
            return;
        }

        try {
            String userEmail = AReclamation_d_iduser.getText(); // Email de l'utilisateur
            ReclamationService rs = new ReclamationService();

            // Récupérer idUser et idEvent depuis la table 'reclamation' en utilisant l'email
            String query = "SELECT id, idUser, idEvent FROM reclamation WHERE email = ?";
            PreparedStatement stmt = MyDatabase.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, userEmail);
            ResultSet rsQuery = stmt.executeQuery();

            if (!rsQuery.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Aucune réclamation trouvée pour cet email.");
                alert.show();
                return;
            }

            int idReclamation = rsQuery.getInt("id");
            int idUser = rsQuery.getInt("idUser");
            int idEvent = rsQuery.getInt("idEvent");

            // Récupération des nouvelles valeurs des champs
            String description = AReclamation_d_DescriptionBtn.getText();
            Rate selectedRate = (Rate) AReclamation_d_RateBtn.getValue();
            LocalDate date = AReclamation_d_ScheduleBtn.getValue();

            // Création d'un nouvel objet Reclamation avec les valeurs mises à jour
            Reclamation updatedReclamation = new Reclamation(idReclamation, idUser, idEvent, description, userEmail, java.sql.Date.valueOf(date), selectedRate);

            // Mise à jour de la réclamation dans la base de données
            rs.modifier(updatedReclamation);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Réclamation mise à jour avec succès !");
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



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AReclamation_d_iduser.setText(SessionManager.getInstance().getCurrentUser().getEmail());
        // ✅ Correctly populate the ComboBox with Rate values
        ObservableList<Rate> rates = FXCollections.observableArrayList(Rate.values());
        AReclamation_d_RateBtn.setItems(rates);

        // ✅ Set default selection
        AReclamation_d_RateBtn.setValue(Rate.ONE);
    }


}
