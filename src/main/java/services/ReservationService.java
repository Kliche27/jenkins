package services;

import models.Reservation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

public class ReservationService  {

    Connection connection = MyDatabase.getInstance().getConnection();

  //  @Override
    public void ajouter(Reservation reservation) {
        String sql = "INSERT INTO reservation(userId, eventId, totalPrice, status) VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, reservation.getUserId());
            ps.setInt(2, reservation.getEventId());
            ps.setDouble(3, reservation.getTotalPrice());
            ps.setString(4, reservation.getStatus().name());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        reservation.setId(rs.getInt(1));
                        System.out.println("Reservation inserted. Generated ID: " + reservation.getId());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  //  @Override
    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = """
        SELECT
        r.*,
        e.titre AS eventName,
        e.dateEvent AS eventDate,
        (SELECT COUNT(*) FROM ticket t WHERE t.reservationId = r.id) AS ticketCount,
        (SELECT ticketType FROM ticket t WHERE t.reservationId = r.id LIMIT 1) AS ticketType
             FROM reservation r
             JOIN evenement e ON r.eventId = e.id
             WHERE r.userId = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setUserId(rs.getInt("userId"));
                    reservation.setEventId(rs.getInt("eventId"));
                    reservation.setTotalPrice(rs.getDouble("totalPrice"));
                    reservation.setStatus(rs.getString("status")); // e.g. "PENDING"
                    reservation.setCreatedAt(rs.getTimestamp("createdAt"));
                    reservation.setEventName(rs.getString("eventName"));
                    reservation.setEventDate(rs.getTimestamp("eventDate"));
                    reservation.setTicketCount(rs.getInt("ticketCount"));
                    reservation.setTicketType(rs.getString("ticketType"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }




 //   @Override
    public boolean ajouterId(Reservation reservation) {
        return FALSE;
    }

    public void modifier(Reservation reservation) {
        String sql = "UPDATE reservation SET totalPrice = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, reservation.getTotalPrice());
            ps.setInt(2, reservation.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


  //  @Override
    public void supprimer(Reservation reservation) {
        String req = "DELETE FROM `reservation` WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, reservation.getId());
            ps.executeUpdate();
            System.out.println("Reservation deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Delete associated payments
        PaymentService paymentService = new PaymentService();
        paymentService.supprimerParReservationId(reservation.getId());
    }

  //  @Override
    public List<Reservation> rechercher() {
        String req = "SELECT * FROM `reservation`";
        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setUserId(rs.getInt("userId"));
                reservation.setEventId(rs.getInt("eventId"));
                reservation.setTotalPrice(rs.getDouble("totalPrice"));
                reservation.setStatus(rs.getString("status"));
                reservations.add(reservation);
            }
            System.out.println("Reservations: " + reservations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public Reservation getReservationById(int reservationId) {
        String req = "SELECT * FROM `reservation` WHERE id=?";
        Reservation reservation = null;

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, reservationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setUserId(rs.getInt("userId"));
                    reservation.setEventId(rs.getInt("eventId"));
                    reservation.setTotalPrice(rs.getDouble("totalPrice"));
                    reservation.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }
}
