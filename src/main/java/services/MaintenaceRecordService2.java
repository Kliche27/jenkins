package services;

import com.sun.tools.javac.Main;
import models.Equipment;
import models.MaintenanceRecord;
import utils.MyDatabase;
import java.time.LocalDate;
import java.time.LocalTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MaintenaceRecordService2 implements AService<MaintenanceRecord>{
    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(MaintenanceRecord record) {
        String query = "INSERT INTO maintenancerecord (id,equipmentId,date,description,status) VALUES (?,?,?,?,?)";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, record.getId());
            ps.setInt(2, record.getEquipmentId());
            ps.setDate(3,java.sql.Date.valueOf(record.getDate()));
            ps.setString(4, record.getDescription());
            ps.setString(5, record.getStatus());
            ps.executeUpdate();
    }catch(SQLException e){
        e.printStackTrace();}
    }

    @Override
    public void modifier(MaintenanceRecord record) {
        String req = "UPDATE maintenancerecord SET equipmentId=?,date=?,description=?,status=? WHERE id=?";
        try{
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, record.getEquipmentId());
            ps.setObject(2, record.getDate());
            ps.setString(3, record.getDescription());
            ps.setString(4, record.getStatus());
            ps.setInt(5, record.getId());
            ps.executeUpdate();
            System.out.println("Record modifié");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(MaintenanceRecord record) {
        String req = "DELETE FROM maintenancerecord WHERE id=?";
        try{
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, record.getId());
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<MaintenanceRecord> rechercher() {
        String req = "SELECT * FROM maintenancerecord";
        List<MaintenanceRecord> records = new ArrayList<>();
        try{
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                MaintenanceRecord mr = new MaintenanceRecord();
                mr.setId(rs.getInt("id"));
                mr.setEquipmentId(rs.getInt("equipmentId"));
                mr.setDate(rs.getObject("date",LocalDate.class));
                mr.setDescription(rs.getString("description"));
                mr.setStatus(rs.getString("status"));
                records.add(mr);
            }
            System.out.println(records);
        }catch(Exception e){
            e.printStackTrace();
        }
        return records;
    }


}
