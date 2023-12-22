package com.example.rentalhome.model;

import com.example.rentalhome.contract.RoomsContract;
import com.example.rentalhome.dto.Rooms;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class RoomsModel implements RoomsContract.Model{

    private FirebaseFirestore db;

    public RoomsModel() {
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void getRooms(String address, @Nullable Integer price, @Nullable List<String> amenities, OnRoomsLoadListener listener) {
        Query query = db.collection("rooms").whereEqualTo("address", address);

        if (price != null) {
            query = query.whereLessThanOrEqualTo("price", price);
        }

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<Rooms> rooms = new ArrayList<>();
            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                Rooms room = snapshot.toObject(Rooms.class);
                if (room != null) {
                    room.setRoomId(snapshot.getId());

                    if (amenities == null || amenities.isEmpty() || room.getAmenities().containsAll(amenities)) {
                        rooms.add(room);
                    }
                }
            }
            listener.onRoomsLoaded(rooms);
        }).addOnFailureListener(e -> listener.onRoomsLoadFailure(e.getMessage()));
    }

    @Override
    public void getFavorite(String userId, OnRoomsLoadListener listener) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> favoriteRoomIds = (List<String>) documentSnapshot.get("favorites");
                        if (favoriteRoomIds != null && !favoriteRoomIds.isEmpty()) {
                            loadFavoriteRooms(favoriteRoomIds, listener);
                        } else {
                            listener.onRoomsLoaded(new ArrayList<>());
                        }
                    }
                })
                .addOnFailureListener(e -> listener.onRoomsLoadFailure(e.getMessage()));
    }

    private void loadFavoriteRooms(List<String> roomIds, OnRoomsLoadListener listener) {
        db.collection("rooms")
                .whereIn(FieldPath.documentId(), roomIds)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Rooms> rooms = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        Rooms room = snapshot.toObject(Rooms.class);
                        if (room != null) {
                            room.setRoomId(snapshot.getId());
                            rooms.add(room);
                        }
                    }
                    listener.onRoomsLoaded(rooms);
                })
                .addOnFailureListener(e -> listener.onRoomsLoadFailure(e.getMessage()));
    }
}
