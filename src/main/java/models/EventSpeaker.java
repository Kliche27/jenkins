package models;

public class EventSpeaker {
    private int id;
    private String nom;
    private String prenom;
    private String description;
    private String status;

    // Constructeur par défaut
    public EventSpeaker() {
    }

    // Constructeur avec tous les paramètres
    public EventSpeaker(int id, String nom, String prenom, String description, String status) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.description = description;
        this.status = status;
    }

    // Constructeur sans id (utilisé pour la création d'un nouveau speaker)
    public EventSpeaker(String nom, String prenom, String description, String status) {
        this.nom = nom;
        this.prenom = prenom;
        this.description = description;
        this.status = status;
    }

    // Getter et Setter pour id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter et Setter pour nom
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter et Setter pour prenom
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    // Getter et Setter pour description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter et Setter pour status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Méthode toString pour afficher les informations du speaker
    @Override
    public String toString() {
        return "EventSpeaker{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
