package com.example.javafx_db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class userController {
    @FXML private TextField locationField;
    @FXML private Slider costSlider;
    @FXML private Slider ratingSlider;
    @FXML private CheckBox singleCheck, doubleCheck, tripleCheck;
    @FXML private Button searchButton, logoutButton, viewFavouritesButton, showDetailsButton;
    @FXML private RadioButton radioMale, radioFemale;
    @FXML private TableView<Hostel> resultTable;
    @FXML private TableColumn<Hostel, String> colHostelName, colAddress, colRating;
    private String selectedGender = "";
    private String currentUserId;

    @FXML
    public void initialize() {

        ToggleGroup genderGroup = new ToggleGroup();
        radioMale.setToggleGroup(genderGroup);
        radioFemale.setToggleGroup(genderGroup);

        colHostelName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        resultTable.setPlaceholder(new Label("No matching hostels found."));

        searchButton.setOnAction(e -> handleSearch());
        viewFavouritesButton.setOnAction(e -> openFavouritesWindow());
        showDetailsButton.setOnAction(e -> handleShowDetails());
    }

    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }

    @FXML
    private void handleGenderSelect() {
        if (radioMale.isSelected()) {
            selectedGender = "Male";
        } else if (radioFemale.isSelected()) {
            selectedGender = "Female";
        }
    }

    @FXML
    private void handleSearch() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> coll = db.getCollection("Hostels");
        Document filter = new Document();

        // Location
        String city = locationField.getText().trim();
        if (!city.isEmpty()) filter.append("location", city);

        // Rating
        filter.append("rating", new Document("$gte", ratingSlider.getValue()));

        // Gender
        if (!selectedGender.isEmpty()) filter.append("gender", selectedGender);

        // Roomtype and Cost
        List<String> types = new ArrayList<>();
        if (singleCheck.isSelected()) types.add("Single");
        if (doubleCheck.isSelected()) types.add("Double");
        if (tripleCheck.isSelected()) types.add("Triple");

        double maxCost = costSlider.getValue();
        if (!types.isEmpty()) {
            Document roomMatch = new Document("type", new Document("$in", types)).append("price_per_month", new Document("$lte", maxCost));
            filter.append("rooms", new Document("$elemMatch", roomMatch));
        } else {
            filter.append("rooms.price_per_month", new Document("$lte", maxCost));
        }

        // Query and map to model
        FindIterable<Document> docs = coll.find(filter);
        var list = FXCollections.<Hostel>observableArrayList();

        for (Document d : docs) {
            String name = d.getString("name");
            String location = d.getString("city");
            String address = d.getString("address");
            double rating = d.get("rating", Number.class).doubleValue();
            String url = d.getString("imageUrl");
            String contact = d.getString("contact_number");
            List<String> amenities = d.getList("amenities", String.class);
            ObjectId id = d.getObjectId("_id");
            String gender = d.getString("gender");

            double price = 0;

            // Find the first matching room's price
            List<Document> rooms = d.getList("rooms", Document.class);
            if (rooms != null && !rooms.isEmpty()) {
                Document roomDoc = rooms.get(0);
                price = roomDoc.get("price_per_month", Number.class).doubleValue();
            }
            list.add(new Hostel(id, name, city, price, rating, address, contact, url, amenities, gender));
        }
        resultTable.setItems(list);
        }

    private void openFavouritesWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FavouritesView.fxml"));
            Parent root = loader.load();

            FavouritesController ctrl = loader.getController();
            ctrl.setUserId(currentUserId);

            Stage dialog = new Stage();
            dialog.setTitle("Your Favourites");
            dialog.initOwner(viewFavouritesButton.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleShowDetails() {
        Hostel selected = resultTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a hostel first.").showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detailsView.fxml"));
            Parent root = loader.load();
            DetailsController ctrl = loader.getController();
            ctrl.setHostel(selected);
            ctrl.setUserId(currentUserId); 
            Stage stage = new Stage();
            stage.setTitle(selected.getName() + " Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogout() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

