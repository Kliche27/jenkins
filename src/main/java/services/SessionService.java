package services;
import models.Session;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionService implements Service<Session> {
    Connection connection = MyDatabase.getInstance().getConnection();


    @Override
    public void ajouter(Session session) {

        String req = "INSERT INTO `session`( `id`, `user_id`, `login_time`, `logout_time`, `is_active`) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1,session.getId());
            ps.setInt(2,session.getUserId());
            ps.setTimestamp(3,session.getLogin_time());
            ps.setTimestamp(4,session.getLogout_time());
            ps.setBoolean(5,session.getIs_active());
            ps.executeUpdate();
            System.out.println("Session ajooute");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void modifier(Session session) {
        String req = "UPDATE `session` SET logout_time=?,is_active=?  WHERE id =?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setTimestamp(1,session.getLogout_time());
            ps.setBoolean(2,session.getIs_active());
            ps.setString(3,session.getId());
            ps.executeUpdate();
            System.out.println("Session modifié");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM `session` WHERE id =?";
        try{
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1,id);
            ps.executeUpdate();
            System.out.println("Session supprimer");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public List<Session> rechercher(){
        String req = "SELECT * FROM `session`";
        List<Session> sessions = new ArrayList<Session>();
        try{
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String id = rs.getString("id");
                int userId = rs.getInt("user_id");
                Timestamp login_time = rs.getTimestamp("login_time");
                Timestamp logout_time = rs.getTimestamp("logout_time");
                boolean is_active = rs.getBoolean("is_active");
                Session session = new Session(id, userId, login_time, logout_time, is_active);
                sessions.add(session);
                System.out.println(session);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return sessions;
    }
    public List<Session> rechercherSessionPerUser(int userId){
        String req = "SELECT * FROM `session` WHERE `user_id`=?";
        List<Session> sessions = new ArrayList<Session>();
        try{
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String id = rs.getString("id");
                Timestamp login_time = rs.getTimestamp("login_time");
                Timestamp logout_time = rs.getTimestamp("logout_time");
                boolean is_active = rs.getBoolean("is_active");
                Session session = new Session(id, userId, login_time, logout_time, is_active);
                sessions.add(session);
                System.out.println(session);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return sessions;
    }
}
