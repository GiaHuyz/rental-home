package com.example.rentalhome.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.example.rentalhome.contract.SignupContract;
import com.example.rentalhome.databinding.ActivitySignUpBinding;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.presenter.SignupPresenter;

public class SignUpActivity extends AppCompatActivity implements SignupContract.View {
    private ActivitySignUpBinding binding;
    private SignupPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new SignupPresenter(this);

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString();
                String name = binding.edtName.getText().toString();
                String phone = binding.edtPhone.getText().toString();
                String pass = binding.edtPass.getText().toString();
                String cpass = binding.edtCPass.getText().toString();
                if (TextUtils.isEmpty(email) ||
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showToast("Invalid Email");
                } else if (TextUtils.isEmpty(name)) {
                    showToast("Invalid Name");
                } else if (TextUtils.isEmpty(phone) ||
                        phone.length() != 10) {
                    showToast("Invalid Phone, Phone must be 10 digits");
                } else if (TextUtils.isEmpty(pass) ||
                        TextUtils.isEmpty(cpass) ||
                        !pass.equals(cpass)) {
                    showToast("Invalid Password");
                } else {
                    presenter.onSignUpButtonClick(new User(name, email, phone), pass);
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String message) {
        showToast(message);
    }

    @Override
    public void showSuccessMessage() {
        showToast("Signup success");
        finish();
    }

    @Override
    public void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }
}