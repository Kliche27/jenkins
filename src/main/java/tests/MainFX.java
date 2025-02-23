package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Session;
import services.SessionService;
import utils.SessionManager;
import java.sql.Timestamp;

import java.io.IOException;

public class MainFX extends Application {
    @Override
    public void start(Stage stage)  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginUser.fxml"));
        try {
            Parent root = loader.load();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                System.out.println("Window is closing...");
                handleWindowClose();
            });
            stage.setTitle("Ajouter");
            stage.show();}
        catch (RuntimeException | IOException r){
            System.out.println(r.getMessage());
        }
    }

    private void handleWindowClose() {
        System.out.println("Performing cleanup before closing...");
        SessionService sessionService = new SessionService();
        Session ss = new Session(SessionManager.getInstance().getSessionKey(), new Timestamp(System.currentTimeMillis()),false);
        sessionService.modifier(ss);
        SessionManager.getInstance().logout();
    }
}