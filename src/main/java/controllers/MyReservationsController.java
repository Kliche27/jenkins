package controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Reservation;
import services.ReservationService;
import utils.SessionManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyReservationsController {

    @FXML
    private TableView<Reservation> tableReservations;

    @FXML
    private TableColumn<Reservation, String> colEventName;

    @FXML
    private TableColumn<Reservation, String> colStatus;

    @FXML
    private TableColumn<Reservation, Double> colTotalPrice;

    @FXML
    private TableColumn<Reservation, Integer> colTicketNumber;

    @FXML
    private TableColumn<Reservation, String> colTicketType;

    @FXML
    private TableColumn<Reservation, String> colEventDate;

    private final ReservationService reservationService = new ReservationService();


    private int currentUserId = SessionManager.getInstance().getCurrentUser().getId();

    @FXML
    public void initialize() {



        colEventName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEventName()));


        colStatus.setCellValueFactory(cellData -> {
            if (cellData.getValue().getStatus() != null) {
                return new SimpleStringProperty(cellData.getValue().getStatus().name());
            }
            return new SimpleStringProperty("");
        });


        colTotalPrice.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTotalPrice()));


        colTicketNumber.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTicketCount()));


        colTicketType.setCellValueFactory(cellData -> {
            String type = cellData.getValue().getTicketType();
            return new SimpleStringProperty(type != null ? type : "");
        });


        colEventDate.setCellValueFactory(cellData -> {
            if (cellData.getValue().getEventDate() != null) {

                String formatted = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                        .format(cellData.getValue().getEventDate());
                return new SimpleStringProperty(formatted);
            }
            return new SimpleStringProperty("");
        });


        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationService.getReservationsByUserId(currentUserId);
        tableReservations.setItems(FXCollections.observableArrayList(reservations));
    }

    @FXML
    private void handleUpdate() {

        Reservation selected = tableReservations.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a reservation to update.");
            return;
        }

        if (selected.getStatus() != Reservation.Status.PENDING) {
            showAlert("Update Not Allowed", "You can only update reservations that are pending.");
            return;
        }
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReservation.fxml"));
            Parent root = loader.load();

            UpdateReservationController updateController = loader.getController();

            updateController.setReservation(selected);


            Stage stage = new Stage();
            stage.setTitle("Update Reservation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadReservations();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open the update interface.");
        }
    }

    @FXML
    private void handleDelete() {
        Reservation selected = tableReservations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a reservation to delete.");
            return;
        }

        reservationService.supprimer(selected);
        loadReservations();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
