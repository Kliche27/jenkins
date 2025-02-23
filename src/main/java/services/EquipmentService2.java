package services;

import models.Equipment;
import utils.MyDatabase;
import services.AService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentService2 implements AService<Equipment> {
    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(Equipment equipment) {
        String req = "INSERT INTO Equipment (name, type, status, quantity, description) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, equipment.getName());
            ps.setString(2, equipment.getType());
            ps.setString(3, equipment.getStatus());
            ps.setInt(4, equipment.getQuantity());
            ps.setString(5, equipment.getDescription());
            ps.executeUpdate();
            System.out.println("Equipement ajouté");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Equipment findByName(String name) {
        String req = "SELECT * FROM Equipment WHERE name = ?";
        Equipment equipment = null;

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                equipment = new Equipment();
                equipment.setId(rs.getInt("id"));
                equipment.setName(rs.getString("name"));
                equipment.setType(rs.getString("type"));
                equipment.setStatus(rs.getString("status"));
                equipment.setQuantity(rs.getInt("quantity"));
                equipment.setDescription(rs.getString("description"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return equipment;
    }


    @Override
    public void modifier(Equipment equipment) {

        String req = "UPDATE Equipment SET name=?, type=?, status=?, quantity=?, description=? WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, equipment.getName());
            ps.setString(2, equipment.getType());
            ps.setString(3, equipment.getStatus());
            ps.setInt(4, equipment.getQuantity());
            ps.setString(5, equipment.getDescription());
            ps.setInt(6, equipment.getId());
            ps.executeUpdate();
            System.out.println("Equipement modifié");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Equipment equipment) {
        String req = "DELETE FROM Equipment WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, equipment.getId());
            ps.executeUpdate();
            System.out.println("Equipment supprimé");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Equipment> rechercher() {
        String req = "SELECT * FROM Equipment";
        List<Equipment> equipments = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setId(rs.getInt("id"));
                equipment.setName(rs.getString("name"));
                equipment.setType(rs.getString("type"));
                equipment.setStatus(rs.getString("status"));
                equipment.setQuantity(rs.getInt("quantity"));
                equipment.setDescription(rs.getString("description"));
                equipments.add(equipment);
            }
            System.out.println(equipments);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return equipments;
    }


}
