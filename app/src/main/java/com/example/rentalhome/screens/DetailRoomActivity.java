package com.example.rentalhome.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.widget.Toast;

import com.example.rentalhome.adapter.CommentsAdapter;
import com.example.rentalhome.adapter.ImagesAdapter;
import com.example.rentalhome.contract.CommentContract;
import com.example.rentalhome.contract.NotificationContract;
import com.example.rentalhome.contract.RoomsContract;
import com.example.rentalhome.contract.UserContract;
import com.example.rentalhome.databinding.DetailRoomBinding;
import com.example.rentalhome.dto.Comment;
import com.example.rentalhome.dto.Notification;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.presenter.CommentPresenter;
import com.example.rentalhome.presenter.NotificationPresenter;
import com.example.rentalhome.presenter.RoomsPresenter;
import com.example.rentalhome.presenter.UserPresenter;
import com.example.rentalhome.service.StripeService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailRoomActivity extends AppCompatActivity implements CommentContract.View, UserContract.View, RoomsContract.ViewDelete, NotificationContract.View {
    private DetailRoomBinding binding;
    private String roomId;
    private String userId;
    private User user;
    private CommentPresenter commentPresenter;
    private UserPresenter userPresenter;
    private CommentsAdapter commentsAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userPresenter = new UserPresenter(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            return;
        }

        Rooms room = (Rooms) bundle.getSerializable("Room");
        user = (User) bundle.getSerializable("User");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(room.getOwnerId().equals(userId)) {
            binding.btnFav.setVisibility(View.GONE);
            binding.btnCall.setVisibility(View.GONE);
            binding.btnCheckout.setVisibility(View.GONE);
            binding.btnDeposit.setVisibility(View.GONE);
            binding.btnRemove.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(room.getCurrentTenant())) {
            binding.btnFav.setVisibility(View.GONE);
            binding.btnSchedule.setVisibility(View.GONE);
            binding.btnDeposit.setVisibility(View.GONE);
            binding.btnContract.setVisibility(View.VISIBLE);
            binding.tvInforTenant.setVisibility(View.VISIBLE);
            binding.btnRemoveTenant.setVisibility(View.VISIBLE);
            binding.tvInforTenant.setText(room.getCurrentTenant());
            db.collection("users").document(room.getCurrentTenant()).get().addOnSuccessListener(queryDocumentSnapshots -> {
               User tenant = queryDocumentSnapshots.toObject(User.class);
               binding.tvInforTenant.setText("Tenant: " + tenant.getName() + ", " + tenant.getPhone() + ", " + tenant.getEmail());
            });
        }

        roomId = room.getRoomId();
        String rules = room.getRules();
        ArrayList<String> images = room.getImages();
        long price = room.getPrice();
        String address = room.getAddress();
        ArrayList<String> amenities = room.getAmenities();
        int area = room.getArea();
        ArrayList<String> surround = room.getSurround();
        String ownerId = room.getOwnerId();

        userPresenter.isFavorite(userId, roomId);

        ImagesAdapter adapter = new ImagesAdapter(images);
        binding.viewPagerImages.setAdapter(adapter);

        binding.tvAmenities.setText(TextUtils.join(", ", amenities));
        binding.tvRules.setText(rules);
        binding.tvPrice.setText("Price: " + NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("vn").setRegion("VN").build()).format(price));
        binding.tvAddres.setText(address);
        binding.tvArea.setText(area + " m\u00B2");

        switch (surround.size()) {
            case 1:
                binding.tvA1.setText(surround.get(0));
                binding.tvA2.setText("");
                binding.tvA3.setText("");
                break;
            case 2:
                binding.tvA1.setText(surround.get(0));
                binding.tvA2.setText(surround.get(1));
                binding.tvA3.setText("");
                break;
            case 3:
                binding.tvA1.setText(surround.get(0));
                binding.tvA2.setText(surround.get(1));
                binding.tvA3.setText(surround.get(2));
                break;
        }

        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DetailRoomActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailRoomActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    makePhoneCall();
                }
            }
        });

        binding.btnPostCm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(binding.edtComment.getText())) {
                    return;
                }

                Comment comment = new Comment();
                comment.setComment(binding.edtComment.getText().toString());
                comment.setRoomId(roomId);
                comment.setUserId(userId);
                comment.setName(user.getName());

                commentPresenter.onPostComment(comment);
                binding.edtComment.setText("");
            }
        });

        binding.btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailRoomActivity.this, ScheduleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("roomId", roomId);
                bundle.putString("address", address);
                bundle.putBoolean("isOwner", userId.equals(ownerId));
                bundle.putString("ownerId", ownerId);
                bundle.putString("userName", user.getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPresenter.onFavoriteClick(userId, roomId);
            }
        });

        binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailRoomActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa phòng này không?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                RoomsPresenter roomsPresenter = new RoomsPresenter(DetailRoomActivity.this);
                                roomsPresenter.deleteRoom(roomId);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        StripeService stripeService = new StripeService(this);
        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(room.getStatus().equals("rented")) {
                    stripeService.startTransactionProcess(room.getPrice(), new StripeService.PaymentSheetResultListener() {
                        @Override
                        public void onPaymentSuccess() {
                            NotificationPresenter notificationPresenter = new NotificationPresenter(DetailRoomActivity.this);
                            notificationPresenter.sendNotification(new Notification(ownerId,
                                    String.format("%s, %s, %s đã thanh toán tiền thuê nhà %s",
                                            user.getName(), user.getPhone(), user.getEmail(), address)));
                            notificationPresenter.sendNotification(new Notification(userId,
                                    String.format("Bạn đã thanh toán tiền thuê nhà %s", address)));
                        }
                    });
                } else if (room.getStatus().equals("deposited")) {
                    stripeService.startTransactionProcess(room.getPrice(), new StripeService.PaymentSheetResultListener() {
                        @Override
                        public void onPaymentSuccess() {
                            NotificationPresenter notificationPresenter = new NotificationPresenter(DetailRoomActivity.this);
                            notificationPresenter.sendNotification(new Notification(ownerId,
                                    String.format("%s, %s, %s đã thanh toán tiền thuê còn lại cho nhà %s",
                                            user.getName(), user.getPhone(), user.getEmail(), address)));
                            notificationPresenter.sendNotification(new Notification(userId,
                                    String.format("Bạn đã thanh toán tiền thuê còn lại cho nhà %s", address)));
                        }
                    });
                } else {
                    Intent intent = new Intent(DetailRoomActivity.this, ContractActivity.class);
                    db = FirebaseFirestore.getInstance();

                    db.collection("users").document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    User owner = documentSnapshot.toObject(User.class);
                                    Bundle b = new Bundle();
                                    b.putString("roomId", roomId);
                                    b.putString("ownerId", ownerId);
                                    b.putString("nameA", owner.getName());
                                    b.putString("phoneA", owner.getPhone());
                                    b.putString("emailA", owner.getEmail());
                                    b.putString("address", address);
                                    b.putString("fee", String.valueOf(price));
                                    b.putString("nameB", user.getName());
                                    b.putString("phoneB", user.getPhone());
                                    b.putString("emailB", user.getEmail());
                                    b.putString("userIdB", userId);
                                    intent.putExtras(b);
                                    startActivityForResult(intent, 2);
                                }
                            });
                }
            }
        });

        binding.btnContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailRoomActivity.this, ContractActivity.class);
                db.collection("users").document(userId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                User owner = documentSnapshot.toObject(User.class);
                                Bundle b = new Bundle();
                                b.putBoolean("show", true);
                                b.putString("nameA", owner.getName());
                                b.putString("phoneA", owner.getPhone());
                                b.putString("emailA", owner.getEmail());
                                b.putString("address", address);
                                b.putString("fee", String.valueOf(price));
                                b.putString("nameB", user.getName());
                                b.putString("phoneB", user.getPhone());
                                b.putString("emailB", user.getEmail());
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        });
            }
        });

        binding.btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(room.getStatus().equals("available")) {
                    stripeService.startTransactionProcess(room.getPrice() * 20 / 100, new StripeService.PaymentSheetResultListener() {
                        @Override
                        public void onPaymentSuccess() {
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("status", "deposited");
                            updateData.put("currentTenant", userId);
                            db.collection("rooms").document(roomId).update(updateData)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(DetailRoomActivity.this, "Bạn đã đặt cọc thành công", Toast.LENGTH_SHORT).show();
                                        NotificationPresenter notificationPresenter = new NotificationPresenter(DetailRoomActivity.this);
                                        notificationPresenter.sendNotification(new Notification(userId,
                                                String.format("Bạn đã đặt cọc thành công nhà %s", address)));
                                        notificationPresenter.sendNotification(new Notification(ownerId,
                                                String.format("%s, %s, %s đã đặt cọc nhà %s",
                                                        user.getName(), user.getPhone(), user.getEmail(), address)));
                                    })

                                    .addOnFailureListener(e ->
                                            Toast.makeText(DetailRoomActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    });
                }
            }
        });

        binding.btnRemoveTenant.setOnClickListener(v -> {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("status", "available");
            updateData.put("currentTenant", FieldValue.delete());
            db.collection("rooms").document(roomId).update(updateData);
        });

        binding.rvComment.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(new ArrayList<>(), ownerId);
        binding.rvComment.setAdapter(commentsAdapter);

        commentPresenter = new CommentPresenter(this);
        commentPresenter.loadComments(roomId);
    }

    private void makePhoneCall() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + user.getPhone()));
            startActivity(callIntent);
        } catch (SecurityException e) {
            Log.d("ERROR_CALL", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            binding.btnFav.setVisibility(View.GONE);
            binding.btnCheckout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadNotification(List<Notification> notificationList) {

    }

    @Override
    public void display(List<Comment> commentList) {
        commentsAdapter.updateCommentList(commentList);
    }

    @Override
    public void addFavorite() {
        binding.btnFav.setText("UNFAVORITE");
    }

    @Override
    public void unFavorite() {
        binding.btnFav.setText("FAVORITE");
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoomDeleted(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("DELETED_ROOM_ID", roomId);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onRoomDeleteFailure(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
