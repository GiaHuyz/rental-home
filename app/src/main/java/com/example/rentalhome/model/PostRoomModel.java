package com.example.rentalhome.model;

import android.net.Uri;
import android.util.Log;

import com.example.rentalhome.contract.PostRoomContract;
import com.example.rentalhome.dto.Rooms;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostRoomModel implements PostRoomContract.Model {
    private FirebaseFirestore db;

    public PostRoomModel() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void postRoom(Rooms room, ArrayList<Uri> uris, OnLoginListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newRoomRef = db.collection("rooms").document();

        Map<String, Object> roomMap = new HashMap<>();
        roomMap.put("ownerId", room.getOwnerId());
        roomMap.put("city", room.getCity());
        roomMap.put("district", room.getDistrict());
        roomMap.put("address", String.format("%s, %s, %s", room.getCity(), room.getDistrict(), room.getAddress()));
        roomMap.put("price", room.getPrice());
        roomMap.put("amenities", room.getAmenities());
        roomMap.put("surround", room.getSurround());
        roomMap.put("status", room.getStatus());
        roomMap.put("currentTenants", room.getCurrentTenants());
        roomMap.put("viewings", room.getViewings());
        roomMap.put("area", room.getArea());
        roomMap.put("rules", room.getRules());

        newRoomRef.set(roomMap).addOnSuccessListener(aVoid -> {
            uploadImagesAndSetUrls(uris, newRoomRef, listener);
        }).addOnFailureListener(e -> {
            listener.onFailure("Error adding room: " + e.getMessage());
        });
    }

    private void uploadImagesAndSetUrls(ArrayList<Uri> uris, DocumentReference roomRef, OnLoginListener listener) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        ArrayList<String> imageUrls = new ArrayList<>();

        for (int i = 0; i < uris.size(); i++) {
            Uri uri = uris.get(i);
            StorageReference fileRef = storageRef.child("images/" + roomRef.getId() + "/" + uri.getLastPathSegment());
            UploadTask uploadTask = fileRef.putFile(uri);

            int finalI = i;
            uploadTask.addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                imageUrls.add(downloadUri.toString());

                if (finalI == uris.size() - 1) {
                    roomRef.update("images", imageUrls)
                            .addOnSuccessListener(aVoid -> listener.onSuccess("Added successfully"))
                            .addOnFailureListener(e -> listener.onFailure("Error upload image: " + e.getMessage()));
                }
            }).addOnFailureListener(e -> {
                listener.onFailure("Error uploading image: " + e.getMessage());
                Log.d("ERROR_UPLOAD_IMG", e.getMessage());
            })).addOnFailureListener(e -> {
                listener.onFailure("Error uploading image: " + e.getMessage());
                Log.d("ERROR_UPLOAD_IMG", e.getMessage());
            });
        }
    }
}
