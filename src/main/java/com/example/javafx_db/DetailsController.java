package com.example.javafx_db;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class DetailsController {

    @FXML private Label lblName;
    @FXML private Label lblLocation;
    @FXML private Label lblPrice;
    @FXML private Label lblRating;
    @FXML private Label lblContact;
    @FXML private Label lblAddress;
    @FXML private ImageView imageView;
    @FXML private Label lblDescription;
    private Hostel currentHostel;
    private String currentUserId;

    public void setUserId(String userId) {
        this.currentUserId = userId;
    }

    public void setHostel(Hostel h) {
        this.currentHostel = h;

        lblName.setText(h.getName());
        lblLocation.setText(h.getLocation());
        lblPrice.setText("$" + h.getPrice());
        lblRating.setText("⭐ " + h.getRating());
        lblContact.setText(h.getContact());
        lblAddress.setText(h.getAddress());
        String url = h.getImageUrl();
        if (url != null && !url.isEmpty()) {
            Image img = new Image(getClass().getResourceAsStream(url));
            imageView.setImage(img);
        } else {
            imageView.setImage(new Image("defaultImage.png"));
        }

        if (h.getAmenities() != null && !h.getAmenities().isEmpty()) {
            lblDescription.setText(String.join(", ", h.getAmenities()));
        } else {
            lblDescription.setText("Amenities: Not specified.");
        }
    }

    @FXML
    public void handleAddToFavourites() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("favouriteNoteView.fxml"));
            Parent root = loader.load();

            FavouriteNoteController controller = loader.getController();
            controller.setUserId(currentUserId);
            controller.setHostelId(currentHostel.getId());

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Add Note to Favourite");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendMessage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InquiryNoteView.fxml"));
            Parent root = loader.load();

            InquiryNoteController ctrl = loader.getController();
            ctrl.setUserId(currentUserId);
            ctrl.setHostelId(currentHostel.getId());

            Stage stage = new Stage();
            stage.setTitle("Send Inquiry");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
