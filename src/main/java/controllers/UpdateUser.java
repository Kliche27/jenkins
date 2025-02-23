package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;
import services.UserService;
import utils.SessionManager;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateUser  implements Initializable {

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField email;

    @FXML
    private TextField nom;

    @FXML
    private PasswordField password;

    @FXML
    private TextField prenom;

    @FXML
    void updateUser(ActionEvent event) {
        String nameRegex = "^[a-zA-ZÀ-ÖØ-öø-ÿ]+$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        UserService ps = new UserService();
        if(nom.getText().isEmpty() || prenom.getText().isEmpty() || email.getText().isEmpty())
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
        else {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            UserService us = new UserService();
            currentUser.setNom(nom.getText());
            currentUser.setPrenom(prenom.getText());
            currentUser.setEmail(email.getText());
            us.modifier(currentUser);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("User modifie");
            alert.show();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        nom.setText(currentUser.getNom());
        prenom.setText(currentUser.getPrenom());
        email.setText(currentUser.getEmail());


    }
}
