package services;

import models.Reclamation;
import models.Reponse;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements AService<Reclamation> {

    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(Reclamation reclamation) {
        String req = "INSERT INTO `reclamation` (`idUser`, `idEvent`, `subject`, `email`, `dateReclamation`, `rate`) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reclamation.getIdUser());
            ps.setInt(2, reclamation.getIdEvent());
            ps.setString(3, reclamation.getSubject());
            ps.setString(4, reclamation.getEmail());
            ps.setDate(5, new Date(reclamation.getDateReclamation().getTime())); // Convert java.util.Date to java.sql.Date
            ps.setInt(6, reclamation.getRate().getValue()); // Store Rate enum value as integer
            ps.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Reclamation reclamation) {
        String req = "UPDATE `reclamation` SET idUser=?, idEvent=?, subject=?, email=?, dateReclamation=?, rate=? WHERE id=?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reclamation.getIdUser());
            ps.setInt(2, reclamation.getIdEvent());
            ps.setString(3, reclamation.getSubject());
            ps.setString(4, reclamation.getEmail());
            ps.setDate(5, new Date(reclamation.getDateReclamation().getTime())); // Update dateReclamation
            ps.setInt(6, reclamation.getRate().getValue()); // Store Rate enum value as integer
            ps.setInt(7, reclamation.getId()); // Condition WHERE for the correct record
            ps.executeUpdate();
            System.out.println("Réclamation modifiée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Reclamation reclamation) {
        String req = "DELETE FROM `reclamation` WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reclamation.getId());
            ps.executeUpdate();
            System.out.println("Réclamation supprimée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reclamation> rechercher() {
        String req = "SELECT * FROM `reclamation`";
        List<Reclamation> reclamations = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setIdUser(rs.getInt("idUser"));
                reclamation.setIdEvent(rs.getInt("idEvent"));
                reclamation.setSubject(rs.getString("subject"));
                reclamation.setEmail(rs.getString("email"));
                reclamation.setDateReclamation(rs.getDate("dateReclamation")); // Retrieve dateReclamation
                reclamation.setRate(Rate.fromValue(rs.getInt("rate"))); // Convert int to Enum with fromValue()
                reclamations.add(reclamation);
            }
            System.out.println(reclamations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamations;
    }
}
