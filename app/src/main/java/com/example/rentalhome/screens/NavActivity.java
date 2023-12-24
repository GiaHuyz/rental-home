package com.example.rentalhome.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.rentalhome.R;
import com.example.rentalhome.databinding.ActivityNavBinding;
import com.example.rentalhome.dto.User;
import com.google.firebase.auth.FirebaseAuth;

public class NavActivity extends AppCompatActivity {
    private ActivityNavBinding binding;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = (User) getIntent().getSerializableExtra("USER");

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.user:
                    replaceFragment(new InfoUserFragment());
                    break;
                case R.id.favorite:
                    replaceFragment(new FavortiteFragment());
                    break;
                case R.id.notify:
                    replaceFragment(new NotificationFragment());
                    break;
                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", user);

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}