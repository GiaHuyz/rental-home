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

    interface ViewDelete {
        void onRoomDeleted(String message);
        void onRoomDeleteFailure(String message);
    }

    interface Presenter {
        void loadRooms(String city, String district, @Nullable Integer price, @Nullable List<String> amenities);
        void loadFavorite(String userId);
        void loadRoomsByOwnerId(String ownerId);
        void deleteRoom(String roomId);
    }

    interface Model {
        void getRooms(String city, String district, @Nullable Integer price, @Nullable List<String> amenities, OnRoomsLoadListener listener);
        void getFavorite(String userId, OnRoomsLoadListener listener);
        void getRoomByOwnerId(String ownerId, OnRoomsLoadListener listener);
        void deleteRoom(String roomId, OnRoomDeletedListener listener);

        interface OnRoomsLoadListener {
            void onRoomsLoaded(ArrayList<Rooms> roomList);
            void onRoomsLoadFailure(String message);
        }

        interface OnRoomDeletedListener {
            void onDeleted(String message);
            void onFailure(String message);
        }
    }
}
