package com.example.javafx_db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;
import org.bson.types.ObjectId;

public class InquiryView {

    @FXML
    private TableView<Inquiry> inquiryTable;
    @FXML
    private TableColumn<Inquiry, String> userIdCol;
    @FXML
    private TableColumn<Inquiry, String> hostelIdCol;
    @FXML
    private TableColumn<Inquiry, String> messageCol;
    @FXML
    private TableColumn<Inquiry, String> dateCol;
    private MongoDatabase database;

    public InquiryView() {
        this.database = MongoDBConnection.getDatabase();
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        hostelIdCol.setCellValueFactory(new PropertyValueFactory<>("hostelId"));
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        loadInquiries();
    }

    public void loadInquiries() {
        MongoCollection<Document> inquiriesCollection = database.getCollection("Inquiries");
        MongoCursor<Document> cursor = inquiriesCollection.find().iterator();

        ObservableList<Inquiry> inquiries = FXCollections.observableArrayList();

        while (cursor.hasNext()) {
            Document inquiryDocument = cursor.next();
            ObjectId userId = inquiryDocument.getObjectId("user_id");
            ObjectId hostelId = inquiryDocument.getObjectId("hostel_id");
            String message = inquiryDocument.getString("message");
            String date = inquiryDocument.getString("date");

            inquiries.add(new Inquiry(userId.toString(), hostelId.toString(), message, date));
        }
        inquiryTable.setItems(inquiries);
    }
}
