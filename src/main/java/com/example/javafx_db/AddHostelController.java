package com.example.javafx_db;

import javafx.event.ActionEvent;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.bson.Document;

import java.util.Arrays;

public class AddHostelController {

    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private TextField addressField;
    @FXML private TextField contactField;
    @FXML private TextField imageUrlField;
    @FXML private TextField genderField;
    @FXML private TextField ratingField;
    @FXML private TextField priceField;
    @FXML private TextField amenitiesField;

    @FXML
    private void handleAddHostel() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> coll = db.getCollection("Hostels");

        Document hostel = new Document("name", nameField.getText())
                .append("location", locationField.getText())
                .append("address", addressField.getText())
                .append("contact_number", contactField.getText())
                .append("imageUrl", imageUrlField.getText())
                .append("gender", genderField.getText())
                .append("rating", Double.parseDouble(ratingField.getText()))
                .append("amenities", Arrays.asList(amenitiesField.getText().split(",")));

        coll.insertOne(hostel);

        nameField.getScene().getWindow().hide();
    }
}
