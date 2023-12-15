package com.example.rentalhome.presenter;

import com.example.rentalhome.contract.SignupContract;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.model.SignupModel;

public class SignupPresenter implements SignupContract.Presenter {
    private SignupContract.View view;
    private SignupContract.Model model;

    public SignupPresenter(SignupContract.View view) {
        this.view = view;
        this.model = new SignupModel();
    }

    @Override
    public void onSignUpButtonClick(User user, String password) {
        model.signup(user, password, new SignupContract.Model.OnSignupListener() {
            @Override
            public void onProgressStart() {
                view.showProgressBar();
            }

            @Override
            public void onProgressEnd() {
                view.hideProgressBar();
            }

            @Override
            public void onSuccess() {
                view.showSuccessMessage();
            }

            @Override
            public void onFailure(String message) {
                view.showErrorMessage(message);
            }
        });
    }
}
