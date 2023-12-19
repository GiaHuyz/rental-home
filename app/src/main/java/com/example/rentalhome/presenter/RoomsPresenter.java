package com.example.rentalhome.presenter;

import com.example.rentalhome.contract.RoomsContract;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.model.RoomsModel;

import java.util.ArrayList;

public class RoomsPresenter implements RoomsContract.Presenter {
    private RoomsContract.View view;
    private RoomsContract.Model model;

    public RoomsPresenter(RoomsContract.View view) {
        this.view = view;
        this.model = new RoomsModel();
    }

    @Override
    public void loadRooms() {
        model.getRooms(new RoomsContract.Model.OnRoomsLoadListener() {
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
