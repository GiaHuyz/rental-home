package com.example.rentalhome.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rentalhome.R;
import com.example.rentalhome.adapter.NotificationAdapter;
import com.example.rentalhome.contract.NotificationContract;
import com.example.rentalhome.databinding.FragmentNotificationBinding;
import com.example.rentalhome.dto.Notification;
import com.example.rentalhome.presenter.NotificationPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements NotificationContract.View {
    private FragmentNotificationBinding binding;
    private NotificationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NotificationPresenter presenter = new NotificationPresenter(this);
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificationAdapter(getContext(), new ArrayList<>());

        binding.rvNotifications.setAdapter(adapter);
        presenter.loadNotifications(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void loadNotification(List<Notification> notificationList) {
        adapter.updateNotificationList(notificationList);
    }
}