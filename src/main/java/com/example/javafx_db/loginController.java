package com.example.javafx_db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class loginController {

    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button selectAdmin;
    @FXML
    private Button selectUser;
    @FXML
    private TextField usernameField;
    private String role = "user";

    @FXML
    void handleAdminSelect(ActionEvent event) {
        role = "admin";
    }
    @FXML
    void handleUserSelect(ActionEvent event) {
        role = "user";
    }


    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("role=" + role + "  user=" + usernameField.getText() + "  pass=" + passwordField.getText());

        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<org.bson.Document> collection = db.getCollection("Users");

        org.bson.Document user = collection.find(new org.bson.Document("username", username).append("password", password).append("role", role)).first();

        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(role.equals("admin") ? "adminView.fxml" : "userView.fxml"));
                Parent root = loader.load();

                if (role.equals("user")) {
                    userController mainCtrl = loader.getController();
                    String userId = user.getObjectId("_id").toHexString();
                    mainCtrl.setCurrentUserId(userId);
                }

                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username, password, or role.");
        }
    }
}
