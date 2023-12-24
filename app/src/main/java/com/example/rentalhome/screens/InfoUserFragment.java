package com.example.rentalhome.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rentalhome.R;
import com.example.rentalhome.adapter.RoomsAdapter;
import com.example.rentalhome.databinding.FragmentInfoUserBinding;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.presenter.RoomsPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class InfoUserFragment extends Fragment {
    private FragmentInfoUserBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInfoUserBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = (User) getArguments().getSerializable("USER");

        binding.edtEmail.setText(user.getEmail());
        binding.edtName.setText(user.getName());
        binding.edtPhone.setText(user.getPhone());
        binding.btnRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER", user);
                bundle.putBoolean("Manage", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}