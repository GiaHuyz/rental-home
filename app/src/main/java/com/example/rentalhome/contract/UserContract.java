package com.example.rentalhome.contract;

import java.util.concurrent.atomic.AtomicBoolean;

public interface UserContract {
    interface View {
        void addFavorite();
        void unFavorite();
        void showMessage(String message);
    }

    interface Presenter {
        void onFavoriteClick(String userId, String roomId);
        void isFavorite(String userId, String roomId);
    }

    interface Model {
        void updateFavoriteList(String userId, String roomId, OnUpdateFavoriteListener listener);

        interface OnUpdateFavoriteListener {
            void onSuccess(AtomicBoolean isAddFavorite);
            void onFailure(String error);
        }
    }
}
