package controllers;
import models.Event;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

public class EvenementListCell extends ListCell<Event> {
    final VBox vbox;
    final Label titreLabel;
    final Label descriptionLabel;
    final Label dateLabel;
    final Label prixLabel;
    final Label lieuLabel;
    final Label placesLabel;

    public EvenementListCell() {
        vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);

        titreLabel = new Label();
        titreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titreLabel.setTextFill(Color.DARKBLUE);

        descriptionLabel = new Label();
        descriptionLabel.setFont(Font.font("Arial", 12));

        dateLabel = new Label();
        prixLabel = new Label();
        lieuLabel = new Label();
        placesLabel = new Label();

        vbox.getChildren().addAll(titreLabel, descriptionLabel, dateLabel, prixLabel, lieuLabel, placesLabel);
    }

    @Override
    protected void updateItem(Event Event, boolean empty) {
        super.updateItem(Event, empty);

        if (empty || Event == null) {
            setText(null);
            setGraphic(null);
        } else {
            titreLabel.setText("📅 " + Event.getTitre());
            descriptionLabel.setText("📝 " + Event.getDescription());
            dateLabel.setText("📆 Date: " + Event.getDateEvent());
            prixLabel.setText("💰 Prix: " + Event.getPrix() + "€");
            lieuLabel.setText("📍 Lieu: " + Event.getLieu());
            placesLabel.setText("🎟 Places disponibles: " + Event.getnbPlace());

            setGraphic(vbox);
        }
    }
}
