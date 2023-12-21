package com.example.rentalhome.contract;

import com.example.rentalhome.dto.User;

public interface LoginContract {
    interface View {
        void showErrorMessage(String message);
        void loginSuccess(User user);
    }

    interface Presenter {
        void onLoginClick(String email, String password);
    }

    interface Model {
        void login(String email, String password, OnLoginListener listener);

        interface OnLoginListener {
            void onSuccess(User user);
            void onFailure(String message);
        }
    }
}
