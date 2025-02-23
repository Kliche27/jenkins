package controllers;
import java.sql.Timestamp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Session;
import models.User;
import services.SessionService;
import services.UserService;
import utils.SessionManager;

import java.io.IOException;

import static utils.PasswordHasher.checkPassword;

public class LoginUser {

    @FXML
    private Button btnLogin;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    void authentication(ActionEvent event) {

        if(email.getText().isEmpty() || password.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Validation");
            alert.setContentText("You must provide your email and password.");
            alert.show();
        }
        else {
            UserService us = new UserService();
            User user = us.rechercherParEmail(email.getText());
            System.out.println(user.getState());
            if(user == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Validation");
                alert.setContentText("You Email or password are not valid");
                alert.show();
            }
            else if (!checkPassword(password.getText(), user.getPassword())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Validation");
                alert.setContentText("You Email or password are not valid");
                alert.show();

            }
            else if (user.getState().equals("desactive")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Desactivation");
                alert.setContentText("Your account has been desactivated");
                alert.show();
            }
            else{

                    // Generate a session key (for example, using UUID)
                    String sessionKey = java.util.UUID.randomUUID().toString();
                    // Store user session globally
                    SessionManager.getInstance().login(user, sessionKey);
                    System.out.println("User logged in with session: " + sessionKey);
                    SessionService s = new SessionService();
                    Session ss = new Session(sessionKey,user.getId(),new Timestamp(System.currentTimeMillis()),null,true);
                    s.ajouter(ss);
                    System.out.println(ss);
                // hni bech n3adih lel template mte3 admin kana admin wale template mte3 user kana user wale templat mte3 organisateur kana organisateur
                if(user.getRole().equals("admin")){
                    try{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AdminDashboard.fxml"));
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
                else if (user.getRole().equals("Organisateur"))
                {
                    try{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AfficherEvenement.fxml"));
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
                else {
                    try{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/UIEvent.fxml"));
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
        }
    }

}

