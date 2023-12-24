package com.example.rentalhome.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rentalhome.adapter.RoomsAdapter;
import com.example.rentalhome.contract.RoomsContract;
import com.example.rentalhome.databinding.FragmentFavortiteBinding;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.presenter.RoomsPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FavortiteFragment extends Fragment implements RoomsContract.View {
    private FragmentFavortiteBinding binding;
    private String userId;
    private RoomsAdapter adapter;
    private RoomsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavortiteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvFavorite.setLayoutManager(new LinearLayoutManager(getActivity()));

        User user = (User) getArguments().getSerializable("USER");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        adapter = new RoomsAdapter(new ArrayList<>(), new RoomsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Rooms room) {
                Intent intent = new Intent(getActivity(), DetailRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Id", room.getRoomId());
                bundle.putString("Rules",room.getRules());
                bundle.putStringArrayList("Images", room.getImages());
                bundle.putLong("Price", room.getPrice());
                bundle.putString("Address", room.getAddress());
                bundle.putStringArrayList("Amenities",room.getAmenities());
                bundle.putInt("Area", room.getArea());
                bundle.putStringArrayList("Surround", room.getSurround());
                bundle.putString("ownerId", room.getOwnerId());
                bundle.putBoolean("Favorite", true);
                bundle.putSerializable("User", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        binding.rvFavorite.setAdapter(adapter);

        presenter = new RoomsPresenter(this);
        presenter.loadFavorite(userId);
    }

    @Override
    public void onRoomsLoaded(ArrayList<Rooms> roomList) {
        adapter.updateRoomList(roomList);
    }

    @Override
    public void onRoomsLoadFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}