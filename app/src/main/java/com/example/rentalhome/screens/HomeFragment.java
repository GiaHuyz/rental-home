package com.example.rentalhome.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.rentalhome.databinding.FragmentHomeBinding;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.service.ApiProvincesHelper;
import com.example.rentalhome.service.City;
import com.example.rentalhome.service.District;
import com.example.rentalhome.service.ProvincesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ApiProvincesHelper.loadCities("https://provinces.open-api.vn/api/", getContext(), binding.spinnerCity, binding.spinnerDistrict);
        user = (User) getArguments().getSerializable("USER");

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostRoomActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER", user);
                bundle.putString("CITY", binding.spinnerCity.getSelectedItem().toString());
                bundle.putString("DISTRICT", binding.spinnerDistrict.getSelectedItem().toString());
                bundle.putString("PRICE", binding.edtPriceSearch.getText().toString());
                bundle.putStringArrayList("AMENITIES", getAmenities());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private ArrayList<String> getAmenities() {
        ArrayList<String> selectedItems = new ArrayList<>();

        if (binding.cbAir.isChecked()) {
            selectedItems.add(binding.cbAir.getText().toString());
        }
        if (binding.cbBath.isChecked()) {
            selectedItems.add(binding.cbBath.getText().toString());
        }
        if (binding.cbBed.isChecked()) {
            selectedItems.add(binding.cbBed.getText().toString());
        }
        if (binding.cbCook.isChecked()) {
            selectedItems.add(binding.cbCook.getText().toString());
        }
        if (binding.cbFridge.isChecked()) {
            selectedItems.add(binding.cbFridge.getText().toString());
        }
        if (binding.cbInternet.isChecked()) {
            selectedItems.add(binding.cbInternet.getText().toString());
        }
        if (binding.cbParking.isChecked()) {
            selectedItems.add(binding.cbParking.getText().toString());
        }
        if (binding.cbSecurity.isChecked()) {
            selectedItems.add(binding.cbSecurity.getText().toString());
        }
        if (binding.cbWM.isChecked()) {
            selectedItems.add(binding.cbWM.getText().toString());
        }

        return selectedItems;
    }
}