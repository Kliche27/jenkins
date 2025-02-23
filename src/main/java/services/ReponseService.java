package services;

import models.Reponse;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements AService<Reponse> {

    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(Reponse reponse) {
        String req = "INSERT INTO `reponse` (`idRec`, `messageRec`, `message`, `dateRep`, `etat`) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reponse.getIdRec());
            ps.setString(2, reponse.getMessageRec());
            ps.setString(3, reponse.getMessage());
            ps.setDate(4, new Date(reponse.getDateRep().getTime())); // Convert java.util.Date to java.sql.Date
            ps.setString(5, reponse.getEtat().name()); // Store Etat enum as string
            ps.executeUpdate();
            System.out.println("Réponse ajoutée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Reponse reponse) {
        String req = "UPDATE `reponse` SET idRec=?, messageRec=?, message=?, dateRep=?, etat=? WHERE id=?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reponse.getIdRec());
            ps.setString(2, reponse.getMessageRec());
            ps.setString(3, reponse.getMessage());
            ps.setDate(4, new Date(reponse.getDateRep().getTime())); // Update dateRep
            ps.setString(5, reponse.getEtat().name()); // Update Etat enum as string
            ps.setInt(6, reponse.getId()); // Condition WHERE for the correct record
            ps.executeUpdate();
            System.out.println("Réponse modifiée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Reponse reponse) {
        String req = "DELETE FROM `reponse` WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reponse.getId());
            ps.executeUpdate();
            System.out.println("Réponse supprimée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reponse> rechercher() {
        String req = "SELECT * FROM `reponse`";
        List<Reponse> reponses = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reponse reponse = new Reponse();
                reponse.setId(rs.getInt("id"));
                reponse.setIdRec(rs.getInt("idRec"));
                reponse.setMessageRec(rs.getString("messageRec"));
                reponse.setMessage(rs.getString("message"));
                reponse.setDateRep(rs.getDate("dateRep")); // Retrieve dateRep
                reponse.setEtat(Etat.valueOf(rs.getString("etat"))); // Convert String to Enum
                reponses.add(reponse);
            }
            System.out.println(reponses);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reponses;
    }
}
