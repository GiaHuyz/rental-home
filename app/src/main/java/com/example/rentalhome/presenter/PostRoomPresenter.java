package com.example.rentalhome.presenter;

import android.app.ProgressDialog;
import android.content.Context;
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
    private Context context;

    public PostRoomPresenter(PostRoomContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new PostRoomModel();
    }

    @Override
    public void onLoginClick(Rooms room, ArrayList<Uri> uris) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait while processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        model.postRoom(room, uris, new PostRoomContract.Model.OnLoginListener() {
            @Override
            public void onSuccess(String message) {
                progressDialog.dismiss();
                view.showSuccessMessage(message);
            }

            @Override
            public void onFailure(String message) {
                progressDialog.dismiss();
                view.showErrorMessage(message);
            }
        });
    }
}
