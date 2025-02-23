package services;

import models.User;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements Service<User> {

    Connection connection = MyDatabase.getInstance().getConnection();


    @Override
    public void ajouter(User user) {

        String req = "INSERT INTO `user`( `nom`, `prenom`, `email`, `password`, `role`) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1,user.getNom());
            ps.setString(2,user.getPrenom());
            ps.setString(3,user.getEmail());
            ps.setString(4,user.getPassword());
            ps.setString(5,user.getRole());
            ps.executeUpdate();
            System.out.println("User ajooute");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //    @Override
    public void modifier(User user) {
        String req = "UPDATE `user` SET nom=?,prenom=?,email =?, password =? , role =? WHERE id =?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1,user.getNom());
            ps.setString(2,user.getPrenom());
            ps.setString(3,user.getEmail());
            ps.setString(4,user.getPassword());
            ps.setString(5,user.getRole());
            ps.setInt(6,user.getId());
            ps.executeUpdate();
            System.out.println("user modifié");

        }
        catch (SQLException e) {

        }

    }
    public void desactiver(User user) {
        String req = "UPDATE `user` SET  state =? WHERE id =?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1,"desactive");
            ps.setInt(2,user.getId());
            ps.executeUpdate();
            System.out.println("user desactive");

        }
        catch (SQLException e) {

        }

    }



    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM `user` WHERE id =?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1,id);
            ps.executeUpdate();
            System.out.println("user suprimé");
        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }
    //
    @Override
    public List<User> rechercher() {
        String req = "SELECT * FROM `user`";
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setState(rs.getString("state"));

                users.add(user);

            }
            System.out.println(users);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  users;

    }
    public User rechercherParEmail(String email) {
        String req = "SELECT * FROM `user` WHERE email = ?";
        User user = null;

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, email);  // Set email parameter in the query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  // If a user is found
                user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setState(rs.getString("state"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user; // Returns null if no user is found
    }

}
