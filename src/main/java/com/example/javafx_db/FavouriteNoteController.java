package com.example.javafx_db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class FavouriteNoteController {

    @FXML private TextArea noteField;

    private String userId;
    private ObjectId hostelId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setHostelId(ObjectId hostelId) {
        this.hostelId = hostelId;
    }

    @FXML
    public void handleDone() {
        String note = noteField.getText().trim();

        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> favs = db.getCollection("Favourites");

        Document fav = new Document("user_id", new ObjectId(userId))
                .append("hostel_id", hostelId)
                .append("note", note)
                .append("favorited_at", new Date());

        favs.insertOne(fav);

        Stage stage = (Stage) noteField.getScene().getWindow();
        stage.close();
    }
}
