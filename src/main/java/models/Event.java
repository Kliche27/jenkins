package models;

import java.time.LocalDateTime;

public class Event {
    private int id;
    private String titre;
    private String description;
    private LocalDateTime dateEvent;
    private double prix;
    private String lieu;
    private int nbPlace;

    public Event() {
    }

    public Event(int id, String titre, String description, LocalDateTime dateEvent, double prix, String lieu, int nbPlace) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateEvent = dateEvent;
        this.prix = prix;
        this.lieu = lieu;
        this.nbPlace = nbPlace;

    }

    public Event(String titre, String description, LocalDateTime dateEvent, double prix, String lieu, int nbPlace) {
        this.titre = titre;
        this.description = description;
        this.dateEvent = dateEvent;
        this.prix = prix;
        this.lieu = lieu;
        this.nbPlace = nbPlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(LocalDateTime dateEvent) {
        this.dateEvent = dateEvent;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getnbPlace() {
        return nbPlace;
    }

    public void setnbPlace(int nbPlace) {
        this.nbPlace = nbPlace;
    }


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateEvent=" + dateEvent +
                ", prix=" + prix +
                ", lieu='" + lieu + '\'' +
                ", nbPlace=" + nbPlace +
                '}';
    }
}
