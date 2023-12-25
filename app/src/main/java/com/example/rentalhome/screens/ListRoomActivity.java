package com.example.rentalhome.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rentalhome.adapter.RoomsAdapter;
import com.example.rentalhome.contract.RoomsContract;
import com.example.rentalhome.databinding.ListRoomSearchBinding;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.presenter.RoomsPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ListRoomActivity extends AppCompatActivity implements RoomsContract.View {
    private ListRoomSearchBinding binding;
    private RoomsAdapter adapter;
    private RoomsPresenter presenter;
    private Integer price;
    private List<String> amenities;
    private boolean manage;
    private String city, district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ListRoomSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvHomes.setLayoutManager(new LinearLayoutManager(this));

        Bundle b = getIntent().getExtras();
        User user = (User) b.getSerializable("USER");
        city = b.getString("CITY");
        district = b.getString("DISTRICT");
        manage = b.getBoolean("Manage");

        if(TextUtils.isEmpty(b.getString("PRICE"))) {
            price = null;
        } else {
            price = Integer.parseInt(b.getString("PRICE"));
        }

        amenities = b.getStringArrayList("AMENITIES");

        adapter = new RoomsAdapter(new ArrayList<>(), new RoomsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Rooms room) {
                Intent intent = new Intent(ListRoomActivity.this, DetailRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Room", room);
                bundle.putSerializable("User", user);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        binding.rvHomes.setAdapter(adapter);

        presenter = new RoomsPresenter(this);

        if(manage) {
            presenter.getMyRooms(FirebaseAuth.getInstance().getCurrentUser().getUid());
        } else {
            presenter.loadRooms(city, district, price, amenities);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("DELETED_ROOM_ID")) {
                String deletedRoomId = data.getStringExtra("DELETED_ROOM_ID");
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    if (adapter.getItem(i).getRoomId().equals(deletedRoomId)) {
                        adapter.removeAt(i);
                        break;
                    }
                }
            }
        }
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
