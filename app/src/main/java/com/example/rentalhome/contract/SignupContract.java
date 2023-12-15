package com.example.rentalhome.contract;

import com.example.rentalhome.dto.User;

public interface SignupContract {
    interface View {
        void showErrorMessage(String message);
        void showSuccessMessage();
        void showProgressBar();
        void hideProgressBar();
    }

    interface Presenter {
        void onSignUpButtonClick(User user, String password);
    }

    interface Model {
        void signup(User user, String password, OnSignupListener listener);

        interface OnSignupListener {
            void onSuccess();
            void onFailure(String message);
            void onProgressStart();
            void onProgressEnd();
        }
    }
}
