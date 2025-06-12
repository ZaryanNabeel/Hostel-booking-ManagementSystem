package com.example.javafx_db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoClient client;
    private static MongoDatabase database;

    public static void connect() {
        String uri = "mongodb://localhost:27017";
        client = MongoClients.create(uri);
        database = client.getDatabase("Hostel_finder");
    }

    // Getting the database object to perform operations
    public static MongoDatabase getDatabase() {
        return database;
    }

    public static void close() {
        if (client != null) {
            client.close();
        }
    }
}
