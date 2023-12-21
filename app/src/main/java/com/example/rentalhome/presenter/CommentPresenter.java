package com.example.rentalhome.presenter;

import com.example.rentalhome.contract.CommentContract;
import com.example.rentalhome.contract.RoomsContract;
import com.example.rentalhome.dto.Comment;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.model.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class CommentPresenter implements CommentContract.Presenter {

    private CommentContract.View view;
    private CommentContract.Model model;

    public CommentPresenter(CommentContract.View view) {
        this.view = view;
        model = new CommentModel();
    }

    @Override
    public void onPostComment(Comment comment) {
        model.postRoom(comment, new CommentContract.Model.OnListener() {
            @Override
            public void onFailure(String message) {
                view.showErrorMessage(message);
            }
        });
    }

    @Override
    public void loadComments(String roomId) {
        model.loadComments(roomId, new CommentContract.Model.OnLoadListener() {
            @Override
            public void onSuccess(List<Comment> commentList) {
                view.display(commentList);
            }

            @Override
            public void onFailure(String message) {
                view.showErrorMessage(message);
            }
        });
    }
}
