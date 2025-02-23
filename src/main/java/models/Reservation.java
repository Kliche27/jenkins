package models;

import java.util.Date;

public class Reservation {
    private int id;
    private int userId;
    private int eventId;
    private double totalPrice;
    private Status status;
    private Payment payment;
    private Date createdAt;

    private String eventName;
    private Date eventDate;
    private int ticketCount;
    private String ticketType;

    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELED
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public Reservation() {
        this.createdAt = new Date();
    }

    public Reservation(int id, int userId, int eventId, double totalPrice, String status) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.totalPrice = totalPrice;
        this.status = Status.valueOf(status);
        this.createdAt = new Date();
    }

    public Reservation(int userId, int eventId, double totalPrice, String status) {
        this.userId = userId;
        this.eventId = eventId;
        this.totalPrice = totalPrice;
        this.status = Status.valueOf(status);
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
