package com.example.rentalhome.model;

import com.example.rentalhome.contract.LoginContract;
import com.example.rentalhome.dto.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginModel implements LoginContract.Model {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    public LoginModel() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void login(String email, String password, OnLoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        firestore.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        User user = documentSnapshot.toObject(User.class);
                                        listener.onSuccess(user);
                                    } else {
                                        listener.onFailure("User not found in Firestore");
                                    }
                                })
                                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }
}
