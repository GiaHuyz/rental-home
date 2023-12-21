package com.example.rentalhome.contract;

import com.example.rentalhome.dto.Rooms;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public interface RoomsContract {
    interface View {
        void onRoomsLoaded(ArrayList<Rooms> roomList);
        void onRoomsLoadFailure(String message);
    }

    interface Presenter {
        void loadRooms(String address, @Nullable Integer price, @Nullable List<String> amenities);
    }

    interface Model {
        void getRooms(String address, @Nullable Integer price, @Nullable List<String> amenities, OnRoomsLoadListener listener);

        interface OnRoomsLoadListener {
            void onRoomsLoaded(ArrayList<Rooms> roomList);
            void onRoomsLoadFailure(String message);
        }
    }
}
