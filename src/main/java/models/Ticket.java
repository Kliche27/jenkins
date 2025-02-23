package models;

public class Ticket {

    private int id;
    private int reservationId;
    private TicketType ticketType;
    private String qrCode;
    private int ticketCount;
    private double price;

    public enum TicketType {
        VIP,
        STANDARD,
        KID
    }

    public Ticket() {}

    public Ticket(int id, int reservationId, String ticketType, double price, int ticketCount) {
        this.id = id;
        this.reservationId = reservationId;
        this.ticketType = TicketType.valueOf(ticketType);
        this.price = price;
        this.ticketCount = ticketCount;
    }

    public Ticket(int reservationId, String ticketType, double price, int ticketCount) {
        this.reservationId = reservationId;
        this.ticketType = TicketType.valueOf(ticketType);
        this.price = price;
        this.ticketCount = ticketCount;
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

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", reservationId=" + reservationId +
                ", ticketType=" + ticketType +
                ", qrCode='" + qrCode + '\'' +
                ", ticketCount=" + ticketCount +
                ", price=" + price +
                '}';
    }
}
