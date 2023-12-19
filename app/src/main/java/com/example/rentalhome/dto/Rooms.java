package com.example.rentalhome.dto;

import java.util.ArrayList;

public class Rooms {
    private String roomId;
    private String ownerId;
    private ArrayList<String> images;
    private long price;
    private String address;
    private ArrayList<String> amenities;
    private String status;
    private ArrayList<String> currentTenants;
    private ArrayList<String> reviews;
    private ArrayList<String> viewings;

    public Rooms() {
    }

    public Rooms(String roomId, String ownerId, ArrayList<String> images, long price, String address, ArrayList<String> amenities, String status, ArrayList<String> currentTenants, ArrayList<String> reviews, ArrayList<String> viewings) {
        this.roomId = roomId;
        this.ownerId = ownerId;
        this.images = images;
        this.price = price;
        this.address = address;
        this.amenities = amenities;
        this.status = status;
        this.currentTenants = currentTenants;
        this.reviews = reviews;
        this.viewings = viewings;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getCurrentTenants() {
        return currentTenants;
    }

    public void setCurrentTenants(ArrayList<String> currentTenants) {
        this.currentTenants = currentTenants;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getViewings() {
        return viewings;
    }

    public void setViewings(ArrayList<String> viewings) {
        this.viewings = viewings;
    }
}
