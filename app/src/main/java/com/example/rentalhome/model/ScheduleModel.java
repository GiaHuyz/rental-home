package com.example.rentalhome.model;

import com.example.rentalhome.contract.ScheduleContract;
import com.example.rentalhome.dto.Schedule;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ScheduleModel implements ScheduleContract.Model {

    private FirebaseFirestore db;

    public ScheduleModel() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void createSchedule(Schedule schedule, OnScheduleListener listener) {
        db.collection("schedules").add(schedule)
                .addOnSuccessListener(documentReference -> listener.onSuccess("Schedule added successfully"))
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    @Override
    public void getSchedules(String roomId, OnLoadSchedulesListener listener) {
        db.collection("schedules")
                .whereEqualTo("roomId", roomId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            listener.onFailure(e.getMessage());
                            return;
                        }

                        List<Schedule> schedules = new ArrayList<>();
                        if (queryDocumentSnapshots != null) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                Schedule schedule = snapshot.toObject(Schedule.class);
                                if (schedule != null) {
                                    schedule.setScheduleId(snapshot.getId());
                                    schedules.add(schedule);
                                }
                            }
                        }
                        listener.onSuccess(schedules);
                    }
                });
    }
}
