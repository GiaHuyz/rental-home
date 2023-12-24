package com.example.rentalhome.presenter;


import com.example.rentalhome.contract.NotificationContract;
import com.example.rentalhome.dto.Notification;
import com.example.rentalhome.model.NotificationModel;

import java.util.List;

public class NotificationPresenter implements NotificationContract.Presenter {
    private NotificationContract.View view;
    private NotificationContract.Model model;

    public NotificationPresenter(NotificationContract.View view) {
        this.view = view;
        model = new NotificationModel();
    }

    @Override
    public void sendNotification(Notification notification) {
        model.sendNotification(notification, new NotificationContract.Model.OnListener() {
            @Override
            public void onFailure(String message) {
                view.showErrorMessage(message);
            }
        });
    }

    @Override
    public void loadNotifications(String userId) {
        model.loadNotifications(userId, new NotificationContract.Model.OnLoadListener() {
            @Override
            public void onSuccess(List<Notification> notificationList) {
                view.loadNotification(notificationList);
            }

            @Override
            public void onFailure(String message) {
                view.showErrorMessage(message);
            }
        });
    }
}
