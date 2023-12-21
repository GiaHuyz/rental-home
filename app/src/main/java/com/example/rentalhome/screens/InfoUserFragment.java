package com.example.rentalhome.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rentalhome.R;
import com.example.rentalhome.databinding.FragmentInfoUserBinding;
import com.example.rentalhome.dto.User;

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
    }
}