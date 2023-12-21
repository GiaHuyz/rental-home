package com.example.rentalhome.contract;

import com.example.rentalhome.dto.Comment;

import java.util.List;

public interface CommentContract {
    interface View {
        void showErrorMessage(String message);
        void display(List<Comment> commentList);
    }

    interface Presenter {
        void onPostComment(Comment comment);
        void loadComments(String roomId);
    }

    interface Model {
        void postRoom(Comment comment, OnListener listener);
        void loadComments(String roomId, OnLoadListener listener);

        interface OnListener {
            void onFailure(String message);
        }

        interface OnLoadListener {
            void onSuccess(List<Comment> commentList);
            void onFailure(String message);
        }
    }
}
