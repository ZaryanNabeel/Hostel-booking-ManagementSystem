package com.example.javafx_db;

public class Inquiry {
    private String userId;
    private String hostelId;
    private String message;
    private String date;

    public Inquiry(String userId, String hostelId, String message, String date) {
        this.userId = userId;
        this.hostelId = hostelId;
        this.message = message;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public String getHostelId() {
        return hostelId;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

}
