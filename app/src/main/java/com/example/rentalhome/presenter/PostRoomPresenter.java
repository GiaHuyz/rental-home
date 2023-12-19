package com.example.rentalhome.presenter;

import com.example.rentalhome.contract.PostRoomContract;
import com.example.rentalhome.contract.SignupContract;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.model.PostRoomModel;
import com.example.rentalhome.model.SignupModel;

public class PostRoomPresenter implements PostRoomContract.Presenter {
    private PostRoomContract.View view;
    private PostRoomContract.Model model;

    public PostRoomPresenter(PostRoomContract.View view) {
        this.view = view;
        this.model = new PostRoomModel();
    }
    @Override
    public void onLoginClick(Rooms room) {
        model.postRoom(room, new PostRoomContract.Model.OnLoginListener() {
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
