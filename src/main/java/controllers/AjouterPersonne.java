package controllers;

import models.User;
import services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AjouterPersonne {

    @FXML
    private Button btnAjout;

    @FXML
    private TextField textFieldNom;

    @FXML
    private TextField textFieldPrenom;

    @FXML
    void ajoutUser(ActionEvent event) throws IOException {
        if (textFieldNom.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Yahdik");
            alert.show();

        } else {
            UserService ps = new UserService();
            ps.ajouter(new User(textFieldNom.getText(), textFieldPrenom.getText(),"alaaa@email.com","ffdsdf","partcipant"));

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.show();
        }

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichagePersonne.fxml"));
//        Parent root = loader.load();
//
//        btnAjout.getScene().setRoot(root);
//
//        AffichagePersonne af = loader.getController();
//
//        af.setNomID(textFieldNom);
//        af.setPreNomID(textFieldPrenom);


    }
}