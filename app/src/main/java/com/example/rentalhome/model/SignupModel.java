package com.example.rentalhome.model;

import com.example.rentalhome.contract.SignupContract;
import com.example.rentalhome.dto.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupModel implements SignupContract.Model {
    @Override
    public void signup(User user, String password, OnSignupListener listener) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        listener.onProgressStart();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    String userId = authResult.getUser().getUid();

                    firestore.collection("users")
                            .document(userId)
                            .set(user)
                            .addOnSuccessListener(aVoid -> {
                                listener.onSuccess();
                                listener.onProgressEnd();
                            })
                            .addOnFailureListener(e -> {
                                listener.onFailure(e.getMessage());
                                listener.onProgressEnd();
                            });
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e.getMessage());
                    listener.onProgressEnd();
                });
    }
}
