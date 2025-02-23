package services;

import models.Payment;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentService implements BService<Payment> {

    Connection connection = MyDatabase.getInstance().getConnection();


    @Override
    public void ajouter(Payment payment) {
    }

    @Override
    public boolean ajouterId(Payment payment) {
        String req = "INSERT INTO `payment`(`reservationId`, `amount`, `paymentDate`, `status`, `paymentType`) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement rs = connection.prepareStatement(req);
            rs.setInt(1, payment.getReservationId());       // reservationId
            rs.setDouble(2, payment.getAmount());          // amount
            rs.setDate(3, new java.sql.Date(payment.getPaymentDate().getTime())); // paymentDate
            rs.setString(4, payment.getStatus());          // status
            rs.setString(5, payment.getPaymentType().name()); // paymentType
            rs.executeUpdate();
            System.out.println("Payment added successfully!");
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void modifier(Payment payment) {
        String req = "UPDATE `payment` SET `amount` = ?, `status` = ?, `stripePaymentId` = ?, `paymentDate` = ? WHERE `id` = ?";

            try {
                PreparedStatement stmt = connection.prepareStatement(req);
                stmt.setDouble(1, payment.getAmount());
                stmt.setString(2, payment.getStatus());
                stmt.setString(3, payment.getStripePaymentId());
                stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // Update payment date
                stmt.setInt(5, payment.getId());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Payment updated successfully.");
                } else {
                    System.out.println("No payment found with ID: " + payment.getId());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



    @Override
    public void supprimer(Payment payment) {
        String req = "DELETE FROM `payment` WHERE `id` = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            stmt.setInt(1, payment.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Payment deleted successfully.");
            } else {
                System.out.println("No payment found with ID: " + payment.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Payment> rechercher() {
        String req = "SELECT * FROM `payment`";
        List<Payment> payments = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(req);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setReservationId(rs.getInt("reservationId"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setStripePaymentId(rs.getString("stripePaymentId"));
                payment.setStatus(rs.getString("status"));
                payment.setPaymentDate(rs.getTimestamp("paymentDate"));
                payments.add(payment);
            }
            System.out.println(payments);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }
    public void supprimerParReservationId(int reservationId) {
        String req = "DELETE FROM `payment` WHERE `reservationId` = ?";

        try (PreparedStatement stmt = connection.prepareStatement(req)) {
            stmt.setInt(1, reservationId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Payments deleted successfully for reservation ID: " + reservationId);
            } else {
                System.out.println("No payments found for reservation ID: " + reservationId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
