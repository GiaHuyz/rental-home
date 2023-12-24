package com.example.rentalhome.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.rentalhome.R;
import com.example.rentalhome.databinding.ActivityContractBinding;

public class ContractActivity extends AppCompatActivity {
    private ActivityContractBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContractBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle b = getIntent().getExtras();
        String nameA = b.getString("nameA");
        String phoneA = b.getString("phoneA");
        String emailA = b.getString("emailA");
        String address = b.getString("address");
        String fee = b.getString("fee");
        String nameB = b.getString("nameB");
        String phoneB = b.getString("phoneB");
        String emailB = b.getString("emailB");

        binding.edtongba.setText(nameA);
        binding.edtSDT.setText(phoneA);
        binding.edtemail.setText(emailA);
        binding.edtchusohuu.setText(address);
        binding.edtongba2.setText(nameB);
        binding.edtemail2.setText(emailB);
        binding.edtSDT2.setText(phoneB);
        binding.edttaidc.setText(address);
        binding.edttrahangthang.setText(fee);
    }
}