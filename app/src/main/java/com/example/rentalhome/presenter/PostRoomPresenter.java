package com.example.rentalhome.presenter;

import android.net.Uri;

import com.example.rentalhome.contract.PostRoomContract;
import com.example.rentalhome.contract.SignupContract;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.model.PostRoomModel;
import com.example.rentalhome.model.SignupModel;

import java.util.ArrayList;

public class PostRoomPresenter implements PostRoomContract.Presenter {
    private PostRoomContract.View view;
    private PostRoomContract.Model model;

    public PostRoomPresenter(PostRoomContract.View view) {
        this.view = view;
        this.model = new PostRoomModel();
    }

    @Override
    public void onLoginClick(Rooms room, ArrayList<Uri> uris) {
        model.postRoom(room, uris, new PostRoomContract.Model.OnLoginListener() {
            @Override
            public void onSuccess(String message) {
                view.showSuccessMessage(message);
            }

            @Override
            public void onFailure(String message) {
                view.showErrorMessage(message);
            }
        });
    }
}
