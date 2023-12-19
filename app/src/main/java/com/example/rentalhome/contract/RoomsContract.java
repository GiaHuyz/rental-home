package com.example.rentalhome.contract;

import com.example.rentalhome.dto.Rooms;

import java.util.ArrayList;

public interface RoomsContract {
    interface View {
        void onRoomsLoaded(ArrayList<Rooms> roomList);
        void onRoomsLoadFailure(String message);
    }

    interface Presenter {
        void loadRooms();
    }

    interface Model {
        void getRooms(OnRoomsLoadListener listener);

        interface OnRoomsLoadListener {
            void onRoomsLoaded(ArrayList<Rooms> roomList);
            void onRoomsLoadFailure(String message);
        }
    }
}
