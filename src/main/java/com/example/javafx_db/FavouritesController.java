package com.example.javafx_db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FavouritesController {

    @FXML private TableView<FavouriteRow> favTable;
    @FXML private TableColumn<FavouriteRow,String> colName;
    @FXML private TableColumn<FavouriteRow,String> colDate;
    @FXML private TableColumn<FavouriteRow,String> colNote;
    @FXML private Button removeFavouriteButton;
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
        loadFavourites();
    }

    private void loadFavourites() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> favColl = db.getCollection("Favourites");
        MongoCollection<Document> hostColl = db.getCollection("Hostels");

        var data = FXCollections.<FavouriteRow>observableArrayList();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        for (Document fav : favColl.find(new Document("user_id", new ObjectId(userId)))) {
            String favId = fav.getObjectId("_id").toHexString();
            ObjectId hid = fav.getObjectId("hostel_id");
            Document hostel = hostColl.find(new Document("_id", hid)).first();
            String name = hostel != null ? hostel.getString("name") : "—";

            Date d = fav.getDate("favorited_at");
            String date = d != null ? fmt.format(d) : "";

            String note = fav.getString("note");
            data.add(new FavouriteRow(favId, name, date, note));
        }
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        favTable.setItems(data);
    }

    @FXML
    private void handleRemoveFavourite() {
        FavouriteRow selected = favTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a favourite to remove.").showAndWait();
            return;
        }
        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> favColl = db.getCollection("Favourites");
        favColl.deleteOne(new Document("_id", new ObjectId(selected.getFavId())));
        favTable.getItems().remove(selected);
    }

    @FXML
    private void handleClose() {
        ((Stage) favTable.getScene().getWindow()).close();
    }

    public static class FavouriteRow {
        private final String favId;
        private final String name;
        private final String date;
        private final String note;

        public FavouriteRow(String favId, String name, String date, String note) {
            this.favId = favId;
            this.name  = name;
            this.date  = date;
            this.note  = note;
        }

        public String getFavId() { return favId; }
        public String getName()    { return name; }
        public String getDate()    { return date; }
        public String getNote()    { return note; }
    }
}
