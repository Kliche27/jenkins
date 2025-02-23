package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.User;
import services.UserService;
import utils.PasswordHasher;

import java.net.URL;
import java.util.ResourceBundle;

public class AjouterUser {

    @FXML
    private TextField email;

    @FXML
    private TextField nom;

    @FXML
    private PasswordField password;

    @FXML
    private TextField prenom;
    @FXML
    private ComboBox<String> role;

    @FXML
    private Button btnAjt;

    @FXML
    void ajoutUser(ActionEvent event) {
        String nameRegex = "^[a-zA-ZÀ-ÖØ-öø-ÿ]+$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        UserService ps = new UserService();
        if(nom.getText().isEmpty() || prenom.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty())
        {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Validation");
            alert.setHeaderText("All fields are required!");
            alert.setContentText("Please fill in all the fields before submitting.");
            alert.show();
        }
        else if(!nom.getText().matches(nameRegex) || !prenom.getText().matches(nameRegex)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Validation");
            alert.setContentText("Name and Surname must contain only letters!,Please enter valid characters (A-Z, a-z).");
            alert.show();
        }
        else if (!email.getText().matches(emailRegex)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email Validation");
            alert.setContentText("Please enter a valid email format");
            alert.show();
        }
        else if (!password.getText().matches(passwordRegex))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Password Validation");
            alert.setContentText("Password must have at least 8 caracters 1 digit and 1 UpperCase");
            alert.show();
        }
        else if (ps.rechercherParEmail(email.getText())!=null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email Validation");
            alert.setContentText("Email already exist");
            alert.show();
        }
        else{
            String hashedPassword = PasswordHasher.hashPassword(password.getText());
            ps.ajouter(new User(nom.getText(), prenom.getText(),email.getText(),hashedPassword,role.getSelectionModel().getSelectedItem().toString()));
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.show();
        }

    }
    @FXML
    public void initialize() {
        role.setItems(FXCollections.observableArrayList("Organisateur", "Participant"));
    }
}
