package models;

import java.util.Date;

public class Payment {
    private int id;
    private int reservationId;
    private double amount;
    private String stripePaymentId;
    private String status;
    private Date paymentDate;
    private PaymentType paymentType;




    public enum PaymentType {
        CREDIT_CARD,
        PAYPAL,
        CASH
    }


    public Payment() {
    }

    public Payment(int reservationId, double amount, String stripePaymentId, String status, Date paymentDate, PaymentType paymentType) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.stripePaymentId = stripePaymentId;
        this.status = status;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
    }
    public Payment(int reservationId, double amount,  String status, Date paymentDate ,PaymentType paymentType) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
    }
    public Payment(int reservationId, double amount,  String status, Date paymentDate ) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;

    }


    public Payment(int id, int reservationId, double amount, String stripePaymentId, String status, Date paymentDate, PaymentType paymentType) {
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.stripePaymentId = stripePaymentId;
        this.status = status;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getReservationId() {
        return reservationId;
    }
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }
    public void setStripePaymentId(String stripePaymentId) {
        this.stripePaymentId = stripePaymentId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", reservationId=" + reservationId +
                ", amount=" + amount +
                ", stripePaymentId='" + stripePaymentId + '\'' +
                ", status='" + status + '\'' +
                ", paymentDate=" + paymentDate +
                ", paymentType=" + paymentType +
                '}';
    }

    public void setReservation(Reservation reservation) {
        this.reservationId = reservation.getId();
    }
}