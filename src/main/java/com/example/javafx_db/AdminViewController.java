package com.example.javafx_db;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class AdminViewController {

    @FXML private Text welcomeMessage;
    @FXML private StackPane contentArea;

    @FXML
    private void handleLogout(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx_db/Login.fxml"));
            Parent root = loader.load();

            // Dynamically get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void loadHostelManagement(MouseEvent event) {
        loadIntoContent("HostelManagement.fxml");
    }

    @FXML
    private void loadInquiries(MouseEvent event) {
        loadIntoContent("InquiryView.fxml");
    }

    @FXML
    private void loadDashboard(MouseEvent event) {
        loadIntoContent("AdminDashboard.fxml");
    }


    private void loadIntoContent(String fxmlName) {
        try {
            URL url = Objects.requireNonNull(
                    getClass().getResource("/com/example/javafx_db/" + fxmlName),
                    "FXML not found: " + fxmlName
            );
            Parent view = FXMLLoader.load(url);
            Group wrapper = new Group(view);
            contentArea.getChildren().setAll(wrapper);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR,
                    "Failed to load " + fxmlName + ":\n" + e.getMessage()
            ).showAndWait();
        }
    }
}
