package com.example.rentalhome.model;

import com.example.rentalhome.contract.UserContract;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserModel implements UserContract.Model {
    @Override
    public void updateFavoriteList(String userId, String roomId, OnUpdateFavoriteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("users").document(userId);
        AtomicBoolean isAdded = new AtomicBoolean(true);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> favoriteRoomIds = (List<String>) documentSnapshot.get("favorites");
                if (favoriteRoomIds == null) {
                    favoriteRoomIds = new ArrayList<>();
                }

                if (favoriteRoomIds.contains(roomId)) {
                    favoriteRoomIds.remove(roomId);
                    isAdded.set(false);
                } else {
                    favoriteRoomIds.add(roomId);
                }

                userRef.update("favorites", favoriteRoomIds)
                        .addOnSuccessListener(aVoid -> listener.onSuccess(isAdded))
                        .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
            }
        }).addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }
}
