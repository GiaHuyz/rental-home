package com.example.rentalhome.contract;

import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.dto.User;

public interface PostRoomContract {
    interface View {
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void onLoginClick(Rooms room);
    }

    interface Model {
        void postRoom(Rooms room, OnLoginListener listener);

        interface OnLoginListener {
            void onSuccess(String message);
            void onFailure(String message);
        }
    }
}
