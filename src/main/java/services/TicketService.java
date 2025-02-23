package services;

import models.Reservation;
import models.Ticket;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

public class TicketService implements BService<Ticket> {
    Connection connection = MyDatabase.getInstance().getConnection();
    ReservationService reservationService = new ReservationService();

    @Override
    public boolean ajouterId(Ticket ticket) {
        return FALSE;
    }

    @Override
    public void ajouter(Ticket ticket) {
        Reservation reservation = reservationService.getReservationById(ticket.getReservationId());
        if (reservation != null) {
            ticket.setPrice(reservation.getTotalPrice());
        }
        String req = "INSERT INTO ticket(id, reservationId, ticketType, qrCode,TicketCount, price) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, ticket.getId());
            ps.setInt(2, ticket.getReservationId());
            ps.setString(3, ticket.getTicketType().name());
            ps.setString(4, ticket.getQrCode());
            ps.setInt(5, ticket.getTicketCount());
            ps.setDouble(6, ticket.getPrice());
            ps.executeUpdate();
            System.out.println("Ticket added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Ticket ticket) {
        // Update by ticket id
        String req = "UPDATE ticket SET reservationId=?, ticketType=?, qrCode=?, price=? WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, ticket.getReservationId());
            ps.setString(2, ticket.getTicketType().name());
            ps.setString(3, ticket.getQrCode());
            ps.setDouble(4, ticket.getPrice());
            ps.setInt(5, ticket.getId());
            ps.executeUpdate();
            System.out.println("Ticket updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void modifierByReservationId(int reservationId, int ticketCount, Double price ,String ticketType) {
        String sql = "UPDATE ticket SET TicketCount = ?, ticketType = ?  , price = ? WHERE reservationId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ticketCount);
            ps.setString(2, ticketType);
            ps.setDouble(3, price);
            ps.setInt(4,reservationId);
            int rows = ps.executeUpdate();
            System.out.println("Ticket updated, rows affected: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Ticket ticket) {
        String req = "DELETE FROM ticket WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, ticket.getId());
            ps.executeUpdate();
            System.out.println("Ticket deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ticket> rechercher() {
        String req = "SELECT * FROM ticket";
        List<Ticket> tickets = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setReservationId(rs.getInt("reservationId"));
                ticket.setTicketType(Ticket.TicketType.valueOf(rs.getString("ticketType")));
                ticket.setQrCode(rs.getString("qrCode"));
                ticket.setPrice(rs.getDouble("price"));
                // If ticketCount is stored in your table, you can set it as well:
                // ticket.setTicketCount(rs.getInt("ticketCount"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
}
