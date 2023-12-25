package com.example.rentalhome.model;

import android.util.Log;

import com.example.rentalhome.contract.NotificationContract;
import com.example.rentalhome.dto.Notification;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class NotificationModel implements NotificationContract.Model {
    private FirebaseFirestore db;

    public NotificationModel() {
        this.db = FirebaseFirestore.getInstance();
    }
    
    @Override
    public void sendNotification(Notification notification, OnListener listener) {
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("userId", notification.getUserId());
        notificationMap.put("message", notification.getMessage());
        notificationMap.put("time", FieldValue.serverTimestamp());
        db.collection("notifications").add(notificationMap)
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    @Override
    public void loadNotifications(String userId, OnLoadListener listener) {
        db.collection("notifications")
                .whereEqualTo("userId", userId)
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            listener.onFailure("Error load notifications");
                            Log.e("NotificationModel.loadNotifications", e.getMessage());
                            return;
                        }

                        ArrayList<Notification> notifications = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Notification notification = snapshot.toObject(Notification.class);
                            if (notification != null) {
                                notification.setId(snapshot.getId());
                                notifications.add(notification);
                            }
                        }
                        listener.onSuccess(notifications);
                    }
                });
    }
}
