package com.example.rentalhome.model;

import com.example.rentalhome.contract.RoomsContract;
import com.example.rentalhome.dto.Rooms;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class RoomsModel implements RoomsContract.Model{
    @Override
    public void getRooms(OnRoomsLoadListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            listener.onRoomsLoadFailure(e.getMessage());
                            return;
                        }

                        ArrayList<Rooms> rooms = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            rooms.add(snapshot.toObject(Rooms.class));
                        }
                        listener.onRoomsLoaded(rooms);
                    }
                });
    }
}
