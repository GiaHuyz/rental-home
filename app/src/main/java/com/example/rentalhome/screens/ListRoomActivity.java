package com.example.rentalhome.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rentalhome.adapter.RoomsAdapter;
import com.example.rentalhome.contract.RoomsContract;
import com.example.rentalhome.databinding.ActivityHomeBinding;
import com.example.rentalhome.databinding.ListRoomSearchBinding;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.presenter.RoomsPresenter;

import java.util.ArrayList;

public class ListRoomActivity extends AppCompatActivity implements RoomsContract.View {
    private ListRoomSearchBinding binding;
    private RoomsAdapter adapter;
    private RoomsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ListRoomSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvHomes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomsAdapter(new ArrayList<>());
        binding.rvHomes.setAdapter(adapter);

        presenter = new RoomsPresenter(this);
        presenter.loadRooms();
    }

    @Override
    public void onRoomsLoaded(ArrayList<Rooms> roomList) {
        adapter.updateRoomList(roomList);
    }

    @Override
    public void onRoomsLoadFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
