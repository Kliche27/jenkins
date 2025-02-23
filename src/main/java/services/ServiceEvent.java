package services;

import models.Event;
import models.User;
import utils.MyDatabase;
import utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvent implements IService<Event> {
    private Connection connection;

    public ServiceEvent() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Event event) throws SQLException {
        // Récupérer l'id du dernier organisateur ajouté

        User currentUser = SessionManager.getInstance().getCurrentUser();
        int idOrganisateur = currentUser.getId();

        int organisateurId = 1; // Valeur par défaut
        // Récupérer l'id du dernier eventspeaker avec status 'dispo'
        String getEventspeakerIdSql = "SELECT id FROM eventspeaker WHERE status = 'dispo' ORDER BY id DESC LIMIT 1";
        Statement stmt2 = connection.createStatement();
        ResultSet rs2 = stmt2.executeQuery(getEventspeakerIdSql);
        if (!rs2.next()) {

            throw new SQLException("Aucun eventspeaker disponible");
        }
        int eventspeakerId = rs2.getInt("id");

        // Maintenant, utilise organisateurId et eventspeakerId récupérés dans la requête INSERT
        String sql = "INSERT INTO `evenement`(`titre`, `description`, `dateEvent`, `prix`, `lieu`, `nbPlace`, `organisateurId`, `eventspeakerId`) " +
                "VALUES ('" + event.getTitre() + "', '" + event.getDescription() + "', '" +
                event.getDateEvent().toString() + "', " + event.getPrix() + ", '" +
                event.getLieu() + "', " + event.getnbPlace() + ", " + idOrganisateur + ", " + eventspeakerId + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        String updateSpeakerSql = "UPDATE eventspeaker SET status = 'non dispo' WHERE id = " + eventspeakerId;
        Statement stmt3 = connection.createStatement();
        stmt3.executeUpdate(updateSpeakerSql);;
    }

    @Override
    public void modifier(Event event) throws SQLException {
        // Récupérer l'id du dernier organisateur ajouté
        String getOrganisateurIdSql = "SELECT id FROM user ORDER BY id DESC LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(getOrganisateurIdSql);

        int organisateurId = 1; // Valeur par défaut
        if (rs.next()) {
            organisateurId = rs.getInt("id");  // Récupérer l'id de l'organisateur
        }

        // Récupérer l'id du dernier eventspeaker avec status 'dispo'
        String getEventspeakerIdSql = "SELECT id FROM eventspeaker WHERE status = 'dispo' ORDER BY id DESC LIMIT 1";
        Statement stmt2 = connection.createStatement();
        ResultSet rs2 = stmt2.executeQuery(getEventspeakerIdSql);

        int eventspeakerId = 1; // Valeur par défaut
        if (rs2.next()) {
            eventspeakerId = rs2.getInt("id");  // Récupérer l'id de l'eventspeaker
        }

        String sql = "UPDATE `evenement` SET `titre`='" + event.getTitre() + "', `description`='" +
                event.getDescription() + "', `dateEvent`='" + event.getDateEvent().toString() +
                "', `prix`=" + event.getPrix() + ", `lieu`='" + event.getLieu() +
                "', `nbPlace`=" + event.getnbPlace() + ", `organisateurId`=" + organisateurId +
                ", `eventspeakerId`=" + eventspeakerId +
                " WHERE id=" + event.getId();
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void supprimer(int id) throws SQLException {
        // Utiliser un PreparedStatement pour éviter les injections SQL
        String deleteEventSql = "DELETE FROM `evenement` WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteEventSql)) {
            // Définir l'ID dans la requête préparée
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            // Traiter les erreurs SQL
            throw new SQLException("Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
    }


    @Override
    public List<Event> afficher() throws SQLException {
        List<Event> evenement = new ArrayList<>();
        String sql = "SELECT * FROM `evenement`";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            evenement.add(new Event(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getTimestamp("dateEvent").toLocalDateTime(),
                    rs.getDouble("prix"),
                    rs.getString("lieu"),
                    rs.getInt("nbPlace")
            ));
        }
        return evenement;
    }
    public Event getEventById(int eventId) {
        String sql = "SELECT * FROM evenement WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    return new Event(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getTimestamp("dateEvent").toLocalDateTime(),
                            rs.getDouble("prix"),
                            rs.getString("lieu"),
                            rs.getInt("nbPlace")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
