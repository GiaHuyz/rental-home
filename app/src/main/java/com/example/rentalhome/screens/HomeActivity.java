package com.example.rentalhome.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.rentalhome.R;
import com.example.rentalhome.databinding.ActivityHomeBinding;
import com.example.rentalhome.dto.User;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private User user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = (User) getIntent().getSerializableExtra("USER");
        userId = getIntent().getStringExtra("USER_ID");

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PostRoomActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListRoomActivity.class);
                startActivity(intent);
            }
        });
    }
}