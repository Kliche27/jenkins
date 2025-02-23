package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ReclamationController implements Initializable {

    @FXML
    private Label date_time;

    @FXML
    private Button Rep_link_Btn;

    @FXML
    void Rep_link_fun(ActionEvent event) {
        try {
            // Load the Response Reclamation page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/reponseAdmin.fxml"));
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
    }
}
