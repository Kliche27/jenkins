package controllers;

import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import models.EventSpeaker;

public class EventSpeakerListCell extends ListCell<EventSpeaker> {

    private final VBox vbox;
    private final Label nomLabel;
    private final Label prenomLabel;
    private final Label descriptionLabel;
    private final Label statusLabel;

    public EventSpeakerListCell() {
        vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);

        nomLabel = new Label();
        nomLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nomLabel.setTextFill(Color.DARKBLUE);

        prenomLabel = new Label();
        prenomLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        prenomLabel.setTextFill(Color.DARKGREEN);

        descriptionLabel = new Label();
        descriptionLabel.setFont(Font.font("Arial", 12));
        descriptionLabel.setWrapText(true);

        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusLabel.setTextFill(Color.RED);

        vbox.getChildren().addAll(nomLabel, prenomLabel, descriptionLabel, statusLabel);
    }

    @Override
    protected void updateItem(EventSpeaker speaker, boolean empty) {
        super.updateItem(speaker, empty);

        if (empty || speaker == null) {
            setText(null);
            setGraphic(null);
        } else {
            nomLabel.setText("👤 Nom: " + speaker.getNom());
            prenomLabel.setText("👤 Prénom: " + speaker.getPrenom());
            descriptionLabel.setText("📝 Description: " + speaker.getDescription());
            statusLabel.setText("📌 Statut: " + speaker.getStatus());

            setGraphic(vbox);
        }
    }
}
