module com.example.javafx_db {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires java.desktop;

    opens com.example.javafx_db to javafx.fxml;
    exports com.example.javafx_db;
}
