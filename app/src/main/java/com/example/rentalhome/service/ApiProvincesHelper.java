package com.example.rentalhome.service;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvincesHelper {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void loadCities(String baseUrl, Context context, Spinner spinnerCity, Spinner spinnerDistrict) {
        ProvincesService apiService = getClient(baseUrl).create(ProvincesService.class);

        apiService.getCities().enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.isSuccessful()) {
                    ArrayAdapter<City> cityAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, response.body());
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCity.setAdapter(cityAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = (City) parent.getItemAtPosition(position);
                loadDistricts(context, selectedCity, spinnerDistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private static void loadDistricts(Context context, City selectedCity, Spinner spinnerDistrict) {
        ArrayAdapter<District> districtAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, selectedCity.getDistricts());
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);
    }
}
