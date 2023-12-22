package com.example.rentalhome.presenter;

import com.example.rentalhome.contract.UserContract;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.model.UserModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserPresenter implements UserContract.Presenter {
    private UserContract.View view;
    private UserContract.Model model;

    public UserPresenter(UserContract.View view) {
        this.view = view;
        this.model = new UserModel();
    }

    @Override
    public void onFavoriteClick(String userId, String roomId) {
        model.updateFavoriteList(userId, roomId, new UserContract.Model.OnUpdateFavoriteListener() {
            @Override
            public void onSuccess(AtomicBoolean isAddFavorite) {
                if(isAddFavorite.get()) {
                    view.addFavorite();
                } else {
                    view.unFavorite();
                }
            }

            @Override
            public void onFailure(String error) {
                view.showMessage(error);
            }
        });
    }

    @Override
    public void isFavorite(String userId, String roomId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null && user.getFavorites() != null && user.getFavorites().contains(roomId)) {
                    view.addFavorite();
                } else {
                    view.unFavorite();
                }
            }
        }).addOnFailureListener(e -> view.showMessage("Error: " + e.getMessage()));
    }
}
