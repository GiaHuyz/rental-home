package com.example.rentalhome.presenter;

import com.example.rentalhome.contract.LoginContract;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.model.LoginModel;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private LoginContract.Model model;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.model = new LoginModel();
    }

    @Override
    public void onLoginClick(String email, String password) {
        model.login(email, password, new LoginContract.Model.OnLoginListener() {
            @Override
            public void onSuccess(User user) {
                view.loginSuccess(user);
            }

            @Override
            public void onFailure(String message) {
                view.showErrorMessage(message);
            }
        });
    }
}
