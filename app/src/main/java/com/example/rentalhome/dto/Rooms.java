package com.example.rentalhome.dto;

import java.util.ArrayList;

public class Rooms {
    private String roomId;
    private String ownerId;
    private ArrayList<String> images;
    private long price;
    private int area;
    private String city;
    private String district;
    private String address;
    private ArrayList<String> amenities;
    private ArrayList<String> surround;
    private String status;
    private ArrayList<String> currentTenants;
    private ArrayList<String> viewings;
    private String rules;
    public Rooms() {
    }

    public Rooms(String ownerId, long price, String address, String city, String district, ArrayList<String> amenities, String status, ArrayList<String> surround, String rules, int area) {
        this.ownerId = ownerId;
        this.price = price;
        this.city = city;
        this.district = district;
        this.address = address;
        this.amenities = amenities;
        this.status = status;
        this.surround = surround;
        this.rules = rules;
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
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

    public ArrayList<String> getSurround() {
        return surround;
    }

    public void setSurround(ArrayList<String> surround) {
        this.surround = surround;
    }

    public ArrayList<String> getViewings() {
        return viewings;
    }

    public void setViewings(ArrayList<String> viewings) {
        this.viewings = viewings;
    }
}
