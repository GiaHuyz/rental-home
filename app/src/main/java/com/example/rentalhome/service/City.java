package com.example.rentalhome.service;

import java.util.List;

public class City {
    private String name;
    private int code;
    private List<District> districts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<District> getDistricts() {
        return districts;
    }

    @Override
    public String toString() {
        return name;
    }
}
