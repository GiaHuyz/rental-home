package com.example.rentalhome.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rentalhome.adapter.RoomsAdapter;
import com.example.rentalhome.contract.RoomsContract;
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

        User user = (User) getIntent().getSerializableExtra("USER");

        adapter = new RoomsAdapter(this, new ArrayList<>(), new RoomsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Rooms room) {
                Intent intent = new Intent(ListRoomActivity.this, DetailRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Id", room.getRoomId());
                bundle.putString("Rules",room.getRules());
                bundle.putStringArrayList("Images", room.getImages());
                bundle.putLong("Price", room.getPrice());
                bundle.putString("Address", room.getAddress());
                bundle.putStringArrayList("Amenities",room.getAmenities());
                bundle.putInt("Area", room.getArea());
                bundle.putStringArrayList("Surround", room.getSurround());
                bundle.putString("Phone", room.getPhone());
                bundle.putString("ownerId", room.getOwnerId());
                bundle.putSerializable("User", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
