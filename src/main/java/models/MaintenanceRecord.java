package models;

import java.util.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MaintenanceRecord {
    private int id;
    private int equipmentId;
    private LocalDate date;
    private String description;
    private String status;

    public MaintenanceRecord(int equipmentId, LocalDate date, String description, String status) {
        this.equipmentId = equipmentId;
        this.date = date;
        this.description = description;
        this.status = status;
    }
    public MaintenanceRecord() {

    }

    public MaintenanceRecord(int id, int equipmentId, LocalDate date, String description, String status) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.date = date;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Record " + id+"[ "+equipmentId+" "+date+" "+description+" "+status+" ]" ;
    }
}
