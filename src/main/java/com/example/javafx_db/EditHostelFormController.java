package com.example.javafx_db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditHostelFormController {

    private Hostel hostel;

    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private TextField priceField;
    @FXML private TextField ratingField;
    @FXML private TextField contactField;
    @FXML private TextField addressField;
    @FXML private TextField imageUrlField;
    @FXML private TextField amenitiesField;
    @FXML private TextField genderField;

    public void setHostel(Hostel hostel) {
        this.hostel = hostel;

        // Pre-fill all fields from Hostel model
        nameField.setText(hostel.getName());
        locationField.setText(hostel.getLocation());
        priceField.setText(String.valueOf(hostel.getPrice()));
        ratingField.setText(String.valueOf(hostel.getRating()));
        contactField.setText(hostel.getContact());
        addressField.setText(hostel.getAddress());
        imageUrlField.setText(hostel.getImageUrl());
        genderField.setText(hostel.getGender());

        List<String> am = hostel.getAmenities();
        if (am != null && !am.isEmpty()) {
            amenitiesField.setText(String.join(",", am));
        }
    }

    @FXML
    private void handleSave() {
        String name    = nameField.getText();
        String location= locationField.getText();
        double price   = Double.parseDouble(priceField.getText());
        double rating  = Double.parseDouble(ratingField.getText());
        String contact = contactField.getText();
        String address = addressField.getText();
        String imageUrl= imageUrlField.getText();
        String gender  = genderField.getText();

        // Parse comma-separated amenities back into a List<String>
        List<String> amenities = Arrays.stream(amenitiesField.getText().split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        // Building the $set document
        Document setDoc = new Document("name",            name)
                .append("location",       location)
                .append("price",          price)
                .append("rating",         rating)
                .append("contact_number", contact)
                .append("address",        address)
                .append("imageUrl",       imageUrl)
                .append("gender",         gender)
                .append("amenities",      amenities);

        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> coll = db.getCollection("Hostels");
        coll.updateOne(
                new Document("_id", hostel.getId()),
                new Document("$set", setDoc)
        );

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
