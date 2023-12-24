package com.example.rentalhome.contract;

import com.example.rentalhome.dto.Notification;

import java.util.List;

public interface NotificationContract {
    interface View {
        void showErrorMessage(String message);
        void loadNotification(List<Notification> notificationList);
    }

    interface Presenter {
        void sendNotification(Notification notification);
        void loadNotifications(String userId);
    }

    interface Model {
        void sendNotification(Notification notification, OnListener listener);
        void loadNotifications(String userId, OnLoadListener listener);

        interface OnListener {
            void onFailure(String message);
        }

        interface OnLoadListener {
            void onSuccess(List<Notification> notificationList);
            void onFailure(String message);
        }
    }
}
