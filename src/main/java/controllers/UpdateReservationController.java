package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import models.Event;
import models.Reservation;
import services.ReservationService;
import services.ServiceEvent;
import services.TicketService;

public class UpdateReservationController {

    @FXML
    private Spinner<Integer> spinnerTickets;
    @FXML
    private ComboBox<String> cmbTicketType;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lblEventName;
    @FXML
    private Label lblTotalPrice;

    private Reservation reservation;

    private final ReservationService reservationService = new ReservationService();
    private final TicketService ticketService = new TicketService();
    private final ServiceEvent eventService = new ServiceEvent();


    public void setReservation(Reservation reservation) {
        this.reservation = reservation;

        lblEventName.setText(reservation.getEventName());
        lblTotalPrice.setText(String.format("%.2f$", reservation.getTotalPrice()));
        int currentCount = reservation.getTicketCount() > 0 ? reservation.getTicketCount() : 1;
        spinnerTickets.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, currentCount));
        cmbTicketType.getItems().setAll("STANDARD", "VIP", "KID");
        cmbTicketType.setValue(reservation.getTicketType() != null ? reservation.getTicketType() : "STANDARD");
    }

    @FXML
    private void handleSave() {

        int newTicketCount = spinnerTickets.getValue();
        String newTicketType = cmbTicketType.getValue();


        System.out.println("New ticket count: " + newTicketCount);
        System.out.println("New ticket type: " + newTicketType);
        Event event = eventService.getEventById(reservation.getEventId());
        if (event == null) {
            System.err.println("Event not found for eventId: " + reservation.getEventId());
            return;
        }
        double eventPrice = event.getPrix();
        double newTotalPrice = eventPrice * newTicketCount;
        System.out.println("New total price: " + eventPrice );
        System.out.println("ticket count : " + newTicketCount );



        reservation.setTicketCount(newTicketCount);
        reservation.setTicketType(newTicketType);
        reservation.setTotalPrice(newTotalPrice);


        ticketService.modifierByReservationId(reservation.getId(), newTicketCount,newTotalPrice,newTicketType);


        reservationService.modifier(reservation);

        System.out.println("Reservation updated with new total price: " + newTotalPrice);


        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void handleCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
