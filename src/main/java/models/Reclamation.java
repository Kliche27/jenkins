package models;

import services.Rate;

import java.util.Date;

public class Reclamation {
    private int id;
    private int idUser;
    private int idEvent;
    private String subject;
    private String email;
    private Date dateReclamation;
    private Rate rate;

    public Reclamation(int idUser, int idEvent, String subject, String email, Date dateReclamation, Rate rate) {
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.subject = subject;
        this.email = email;
        this.dateReclamation = dateReclamation;
        this.rate = rate;
    }

    public Reclamation() {
    }

    public Reclamation(int id, int idUser, int idEvent, String subject, String email, Date dateReclamation, Rate rate) {
        this.id = id;
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.subject = subject;
        this.email = email;
        this.dateReclamation = dateReclamation;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateReclamation() {
        return dateReclamation;
    }

    public void setDateReclamation(Date dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idEvent=" + idEvent +
                ", subject='" + subject + '\'' +
                ", email='" + email + '\'' +
                ", dateReclamation=" + dateReclamation +
                ", rate=" + rate +
                '}';
    }
}