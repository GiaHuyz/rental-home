package com.example.rentalhome.contract;

import android.net.Uri;

import com.example.rentalhome.dto.Rooms;

import java.util.ArrayList;

public interface PostRoomContract {
    interface View {
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void onLoginClick(Rooms room, ArrayList<Uri> uris);
    }

    interface Model {
        void postRoom(Rooms room, ArrayList<Uri> uris, OnLoginListener listener);

        interface OnLoginListener {
            void onSuccess(String message);
            void onFailure(String message);
        }
    }
}
