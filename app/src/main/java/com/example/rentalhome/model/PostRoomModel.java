package com.example.rentalhome.model;

import com.example.rentalhome.contract.PostRoomContract;
import com.example.rentalhome.dto.Rooms;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostRoomModel implements PostRoomContract.Model {
    private FirebaseFirestore db;

    public PostRoomModel() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void postRoom(Rooms room, OnLoginListener listener) {
        Map<String, Object> roomMap = new HashMap<>();
        roomMap.put("roomId", room.getRoomId());
        roomMap.put("ownerId", room.getOwnerId());
        roomMap.put("images", room.getImages());
        roomMap.put("price", room.getPrice());
        roomMap.put("address", room.getAddress());
        roomMap.put("amenities", room.getAmenities());
        roomMap.put("status", room.getStatus());
        roomMap.put("currentTenants", room.getCurrentTenants());
        roomMap.put("reviews", room.getReviews());
        roomMap.put("viewings", room.getViewings());

        db.collection("rooms")
                .add(roomMap)
                .addOnSuccessListener(documentReference -> {
                    listener.onSuccess("Room added successfully");
                })
                .addOnFailureListener(e -> {
                    listener.onFailure("Error adding room: " + e.getMessage());
                });
    }
}
