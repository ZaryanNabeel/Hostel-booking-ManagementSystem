package com.example.javafx_db;

import org.bson.types.ObjectId;
import java.util.List;

public class Hostel {
    private final String name;
    private final String location;
    private final Double price;
    private double rating;
    private String address, contact,gender;
    private final String imageUrl;
    private List<String> amenities;
    private ObjectId id;

    public Hostel(ObjectId id, String name, String location, Double price, double rating, String address, String contact, String imageUrl, List<String> amenities,String gender) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
        this.rating = rating;
        this.address = address;
        this.contact = contact;
        this.imageUrl = imageUrl;
        this.amenities = amenities;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getAmenities() {
        return amenities;
    }
    public ObjectId getId() {
        return id;
    }

    public String getContact() {
        return contact;
    }

    public String getContactNumber() {
        return contact;
    }

    public String getGender() {
        return gender;
    }

}
