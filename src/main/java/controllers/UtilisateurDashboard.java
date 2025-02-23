package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class UtilisateurDashboard {
    @FXML
    private Button profileSetting;

    @FXML
    void profileSettingBtn(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/UpdateUser.fxml"));
            Parent root = loader.load();

            // Change the scene to AdminDashboard
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (RuntimeException | IOException r){
            System.out.println(r.getMessage());
        }
    }
}
