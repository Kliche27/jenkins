package services;

import models.EventSpeaker;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEventSpeaker implements IService<EventSpeaker> {
    private Connection connection;

    public ServiceEventSpeaker() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(EventSpeaker eventSpeaker) throws SQLException {
        String sql = "INSERT INTO `eventspeaker`(`nom`, `prenom`, `description`, `status`) " +
                "VALUES ('" + eventSpeaker.getNom() + "', '" + eventSpeaker.getPrenom() + "', '" +
                eventSpeaker.getDescription() + "', '" + eventSpeaker.getStatus() + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void modifier(EventSpeaker eventSpeaker) throws SQLException {
        String sql = "UPDATE `eventspeaker` SET `nom`='" + eventSpeaker.getNom() + "', `prenom`='" +
                eventSpeaker.getPrenom() + "', `description`='" + eventSpeaker.getDescription() +
                "', `status`='" + eventSpeaker.getStatus() + "' WHERE id=" + eventSpeaker.getId();
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String deleteRelationSql = "DELETE FROM evenement WHERE eventspeakerId = ?";
        String deleteSpeakerSql = "DELETE FROM eventspeaker WHERE id = ?";

        try (PreparedStatement relationStmt = connection.prepareStatement(deleteRelationSql);
             PreparedStatement speakerStmt = connection.prepareStatement(deleteSpeakerSql)) {

            // Suppression des relations associées
            relationStmt.setInt(1, id);
            relationStmt.executeUpdate();

            // Suppression du speaker
            speakerStmt.setInt(1, id);
            speakerStmt.executeUpdate();
        }
    }


    @Override
    public List<EventSpeaker> afficher() throws SQLException {
        List<EventSpeaker> eventSpeakers = new ArrayList<>();
        String sql = "SELECT * FROM `eventspeaker`";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            eventSpeakers.add(new EventSpeaker(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("description"),
                    rs.getString("status")
            ));
        }
        return eventSpeakers;
    }
}
