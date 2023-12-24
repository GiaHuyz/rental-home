package com.example.rentalhome.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProvincesService {
    @GET("?depth=2")
    Call<List<City>> getCities();
}
