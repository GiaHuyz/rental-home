package com.example.rentalhome.dto;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String name;
    private String email;
    private String phone;
    private List<String> favorites;

    public User() {
    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }
}
