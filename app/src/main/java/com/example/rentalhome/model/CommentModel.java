package com.example.rentalhome.model;

import android.util.Log;

import com.example.rentalhome.contract.CommentContract;
import com.example.rentalhome.dto.Comment;
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

public class CommentModel implements CommentContract.Model {
    private FirebaseFirestore db;

    public CommentModel() {
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void postRoom(Comment comment, OnListener listener) {
        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("roomId", comment.getRoomId());
        commentMap.put("userId", comment.getUserId());
        commentMap.put("name", comment.getName());
        commentMap.put("comment", comment.getComment());
        commentMap.put("time", FieldValue.serverTimestamp());

        db.collection("comments").add(commentMap).addOnFailureListener(e -> {
            listener.onFailure("Error adding comment: " + e.getMessage());
        });
    }

    @Override
    public void loadComments(String roomId, OnLoadListener listener) {
        db.collection("comments")
                .whereEqualTo("roomId", roomId)
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            listener.onFailure("Error load comments");
                            Log.e("CommentModel.loadComments", e.getMessage());
                            return;
                        }

                        ArrayList<Comment> comments = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Comment cmt = snapshot.toObject(Comment.class);
                            if (cmt != null) {
                                comments.add(cmt);
                            }
                        }
                        listener.onSuccess(comments);
                    }
                });
    }
}
