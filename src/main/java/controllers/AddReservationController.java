package controllers;



import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import models.Event;
import models.Reservation;
import models.Payment;
import models.Ticket;
import services.ReservationService;
import services.PaymentService;
import services.TicketService;
import utils.SessionManager;

import java.util.Date;

public class AddReservationController {

    @FXML
    private Label lblEventName;
    @FXML
    private Spinner<Integer> spinnerTickets;
    @FXML
    private Label lblTotalPrice;
    @FXML
    private ComboBox<String> cmbPaymentMethod;
    @FXML
    private ComboBox<String> cmbTicketType;
    @FXML
    private Button btnConfirm;
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


    private ReservationService reservationService = new ReservationService();
    private PaymentService paymentService = new PaymentService();
    private TicketService ticketService = new TicketService();


    private int currentUserId = SessionManager.getInstance().getCurrentUser().getId();


    private Event event;

    @FXML
    public void initialize() {

        spinnerTickets.setValueFactory(new IntegerSpinnerValueFactory(1, 10, 1));


        cmbPaymentMethod.getItems().addAll("Credit Card", "PayPal", "Cash");


        cmbTicketType.getItems().addAll("STANDARD", "VIP", "KID");


        spinnerTickets.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice());
    }


    public void setEvent(Event event) {
        this.event = event;
        lblEventName.setText(event.getTitre());
        updateTotalPrice();
    }


    private void updateTotalPrice() {
        if (event != null && spinnerTickets.getValue() != null) {
            double total = event.getPrix() * spinnerTickets.getValue();
            lblTotalPrice.setText(String.format("%.2f€", total));
        }
    }


    @FXML
    private void handleConfirm() {

        if (event == null) {
            showAlert("Error", "No event selected.");
            return;
        }
        int ticketCount = spinnerTickets.getValue();
        if (ticketCount <= 0) {
            showAlert("Error", "Please select at least one ticket.");
            return;
        }
        String paymentMethod = cmbPaymentMethod.getValue();
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            showAlert("Error", "Please select a payment method.");
            return;
        }
        String ticketType = cmbTicketType.getValue();
        if (ticketType == null || ticketType.trim().isEmpty()) {
            showAlert("Error", "Please select a ticket type.");
            return;
        }


        double totalPrice = event.getPrix()* ticketCount;

        Reservation reservation = new Reservation();
        reservation.setUserId(currentUserId);
        reservation.setEventId(event.getId());
        reservation.setTotalPrice(totalPrice);
        reservation.setStatus("PENDING");
        reservationService.ajouter(reservation);

        String enumPaymentMethod;
        switch (paymentMethod) {
            case "Credit Card":
                enumPaymentMethod = "CREDIT_CARD";
                break;
            case "PayPal":
                enumPaymentMethod = "PAYPAL";
                break;
            case "Cash":
                enumPaymentMethod = "CASH";
                break;
            default:
                showAlert("Error", "Invalid payment method selected");
                return;
        }

        Payment payment = new Payment();
        payment.setReservationId(reservation.getId());
        payment.setAmount(totalPrice);
        payment.setStatus("PENDING");
        payment.setPaymentType(Payment.PaymentType.valueOf(enumPaymentMethod));
        payment.setPaymentDate(new Date());
        boolean paymentAdded = paymentService.ajouterId(payment);
        if (!paymentAdded) {
            showAlert("Error", "Failed to add payment.");
            return;
        }


            Ticket ticket = new Ticket();
            ticket.setReservationId(reservation.getId());
            ticket.setTicketType(Ticket.TicketType.valueOf(ticketType));
            ticket.setPrice(event.getPrix());
            ticket.setTicketCount(ticketCount);
            ticketService.ajouter(ticket);


        showAlert("Success", "Reservation, Payment, and Tickets added successfully!");
        clearForm();
    }



    private void clearForm() {
        spinnerTickets.getValueFactory().setValue(1);
        cmbPaymentMethod.setValue(null);
        cmbTicketType.setValue(null);
        lblTotalPrice.setText("");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
