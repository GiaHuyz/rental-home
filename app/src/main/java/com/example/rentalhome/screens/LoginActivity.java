package com.example.rentalhome.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rentalhome.contract.LoginContract;
import com.example.rentalhome.databinding.ActivityLoginBinding;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.presenter.LoginPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private ActivityLoginBinding binding;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new LoginPresenter(this);

        binding.btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        binding.btnSignin.setOnClickListener(view ->
                presenter.onLoginClick(binding.edtEmail.getText().toString(), binding.edtPass.getText().toString())
        );
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccess(User user) {
        Intent intent = new Intent(this, NavActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", user);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}