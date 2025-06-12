package com.example.javafx_db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class InquiryNoteController {
    @FXML private TextArea txtMessage;

    private ObjectId userId;
    private ObjectId hostelId;

    public void setUserId(String id) {
        this.userId = new ObjectId(id);
    }

    public void setHostelId(ObjectId id) {
        this.hostelId = id;
    }

    @FXML
    private void handleSend() {
        String message = txtMessage.getText().trim();
        if (message.isEmpty()) return;

        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> inquiries = db.getCollection("Inquiries");

        Document doc = new Document("user_id", userId).append("hostel_id", hostelId).append("message", message).append("date", LocalDate.now().toString());
        inquiries.insertOne(doc);

        ((Stage) txtMessage.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        ((Stage) txtMessage.getScene().getWindow()).close();
    }
}
