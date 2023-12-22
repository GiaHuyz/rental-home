package com.example.rentalhome.presenter;

import com.example.rentalhome.contract.RoomsContract;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.model.RoomsModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class RoomsPresenter implements RoomsContract.Presenter {
    private RoomsContract.View view;
    private RoomsContract.Model model;

    public RoomsPresenter(RoomsContract.View view) {
        this.view = view;
        this.model = new RoomsModel();
    }

    @Override
    public void loadRooms(String address, @Nullable Integer price, @Nullable List<String> amenities) {
        model.getRooms(address, price, amenities, new RoomsContract.Model.OnRoomsLoadListener() {
            @Override
            public void onRoomsLoaded(ArrayList<Rooms> roomList) {
                view.onRoomsLoaded(roomList);
            }

            @Override
            public void onRoomsLoadFailure(String message) {
                view.onRoomsLoadFailure(message);
            }
        });
    }

    @Override
    public void loadFavorite(String userId) {
        model.getFavorite(userId, new RoomsContract.Model.OnRoomsLoadListener() {
            @Override
            public void onRoomsLoaded(ArrayList<Rooms> roomList) {
                view.onRoomsLoaded(roomList);
            }

            @Override
            public void onRoomsLoadFailure(String message) {
                view.onRoomsLoadFailure(message);
            }
        });
    }
}
