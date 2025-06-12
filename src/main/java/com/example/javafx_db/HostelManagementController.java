package com.example.javafx_db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HostelManagementController {

    @FXML private TableView<Hostel> hostelTable;
    @FXML private TableColumn<Hostel, String> colName;
    @FXML private TableColumn<Hostel, String> colLocation;
    @FXML private TableColumn<Hostel, String> colPrice;
    @FXML private TableColumn<Hostel, Double> colRating;
    @FXML private TableColumn<Hostel, String> colAddress;
    @FXML private TableColumn<Hostel, String> colContact;
    @FXML private TableColumn<Hostel, String> colGender;
    @FXML private Button btnAdd, btnEdit, btnDelete;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colLocation.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));
        colPrice.setCellValueFactory(data -> {
            String formattedPrice = "$" + String.format("%.2f", data.getValue().getPrice());
            return new javafx.beans.property.SimpleStringProperty(formattedPrice);
        });
        colRating.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getRating()));
        colAddress.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getAddress()));
        colContact.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getContactNumber()));
        colGender.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getGender()));

        loadHostels();

        btnAdd.setOnAction(e -> handleAddHostel());
        btnEdit.setOnAction(e -> handleEditHostel());
        btnDelete.setOnAction(e -> handleDeleteHostel());
    }

    public void loadHostels() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> coll = db.getCollection("Hostels");

        List<Hostel> list = new ArrayList<>();
        for (Document doc : coll.find()) {
            List<Document> rooms = doc.getList("rooms", Document.class);
            double price = extractPrice(rooms);
            Number ratingNumber = doc.get("rating", Number.class);
            double rating = ratingNumber == null ? 0.0 : ratingNumber.doubleValue();

            Hostel hostel = new Hostel(
                    doc.getObjectId("_id"),
                    doc.getString("name"),
                    doc.getString("location"),
                    price,
                    rating,
                    doc.getString("address"),
                    doc.getString("contact_number"),
                    doc.getString("imageUrl"),
                    doc.getList("amenities", String.class),
                    doc.getString("gender")
            );
            list.add(hostel);
        }
        hostelTable.setItems(FXCollections.observableArrayList(list));
    }

    private double extractPrice(List<Document> rooms) {
        if (rooms == null || rooms.isEmpty()) return 0.0;

        return rooms.stream()
                .map(roomDoc -> roomDoc.get("price_per_month", Number.class))
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .min()
                .orElse(0.0);
    }

    private void handleAddHostel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx_db/AddHostelForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Hostel");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh table after adding
            loadHostels();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleEditHostel() {
        Hostel selected = hostelTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a hostel to edit.").showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx_db/EditHostelForm.fxml"));
            Parent root = loader.load();

            EditHostelFormController controller = loader.getController();
            controller.setHostel(selected);

            Stage stage = new Stage();
            stage.setTitle("Edit Hostel");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Reload updated data
            loadHostels();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleDeleteHostel() {
        Hostel selected = hostelTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a hostel to delete.").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete \"" + selected.getName() + "\"?",
                ButtonType.YES, ButtonType.NO);

        confirm.setTitle("Confirm Deletion");
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            MongoDatabase db = MongoDBConnection.getDatabase();
            db.getCollection("Hostels").deleteOne(new Document("_id", selected.getId()));
            loadHostels();
        }
    }

}
