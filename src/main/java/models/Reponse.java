package models;

import services.Etat;

import java.util.Date;

public class Reponse {
    private int id;
    private int idRec;
    private String messageRec;
    private String message;
    private Date dateRep;
    private Etat etat;

    public Reponse(int idRec, String messageRec, String message, Date dateRep, Etat etat) {
        this.idRec = idRec;
        this.messageRec = messageRec;
        this.message = message;
        this.dateRep = dateRep;
        this.etat = etat;
    }

    public Reponse(int id, int idRec, String messageRec, String message, Date dateRep, Etat etat) {
        this.id = id;
        this.idRec = idRec;
        this.messageRec = messageRec;
        this.message = message;
        this.dateRep = dateRep;
        this.etat = etat;
    }

    public Reponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRec() {
        return idRec;
    }

    public void setIdRec(int idRec) {
        this.idRec = idRec;
    }

    public String getMessageRec() {
        return messageRec;
    }

    public void setMessageRec(String messageRec) {
        this.messageRec = messageRec;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateRep() {
        return dateRep;
    }

    public void setDateRep(Date dateRep) {
        this.dateRep = dateRep;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "ReponseReclamation{" +
                "idRep=" + id +
                ", idRec=" + idRec +
                ", messageRec='" + messageRec + '\'' +
                ", message='" + message + '\'' +
                ", dateRep=" + dateRep +
                ", etat=" + etat +
                '}';
    }
}
