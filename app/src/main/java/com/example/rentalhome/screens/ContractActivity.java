package com.example.rentalhome.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.rentalhome.R;
import com.example.rentalhome.contract.NotificationContract;
import com.example.rentalhome.databinding.ActivityContractBinding;
import com.example.rentalhome.dto.Notification;
import com.example.rentalhome.presenter.NotificationPresenter;
import com.example.rentalhome.service.StripeService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContractActivity extends AppCompatActivity implements NotificationContract.View {
    private ActivityContractBinding binding;
    private String roomId, ownerId, userIdB;
    StripeService stripeService;
    NotificationPresenter notificationPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContractBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle b = getIntent().getExtras();

        String nameA = b.getString("nameA");
        String phoneA = b.getString("phoneA");
        String emailA = b.getString("emailA");
        String address = b.getString("address");
        String fee = b.getString("fee");
        String nameB = b.getString("nameB");
        String phoneB = b.getString("phoneB");
        String emailB = b.getString("emailB");

        if(!b.getBoolean("show")) {
            roomId = b.getString("roomId");
            ownerId = b.getString("ownerId");
            userIdB = b.getString("userIdB");
            stripeService = new StripeService(this);
            notificationPresenter = new NotificationPresenter(this);
        } else {
            binding.btnSubmit.setVisibility(View.GONE);
        }


        binding.edtongba.setText(nameA);
        binding.edtSDT.setText(phoneA);
        binding.edtemail.setText(emailA);
        binding.edtchusohuu.setText(address);
        binding.edtongba2.setText(nameB);
        binding.edtemail2.setText(emailB);
        binding.edtSDT2.setText(phoneB);
        binding.edttaidc.setText(address);
        binding.edttrahangthang.setText(fee);
        binding.tvBenThue.setText(nameB);
        binding.tvBenChoThue.setText(nameA);
        
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stripeService.startTransactionProcess(Long.parseLong(fee), new StripeService.PaymentSheetResultListener() {
                    @Override
                    public void onPaymentSuccess() {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> contractData = new HashMap<>();
                        contractData.put("nameA", nameA);
                        contractData.put("phoneA", phoneA);
                        contractData.put("emailA", emailA);
                        contractData.put("fee", Long.parseLong(fee));
                        contractData.put("nameB", nameB);
                        contractData.put("phoneB", phoneB);
                        contractData.put("emailB", emailB);

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("contract", contractData);
                        updateData.put("status", "rented");
                        updateData.put("currentTenant", userIdB);

                        db.collection("rooms").document(roomId).update(updateData)
                                .addOnSuccessListener(unused -> {
                                    notificationPresenter.sendNotification(new Notification(ownerId,
                                            String.format("%s, %s, %s đã thanh toán và xác nhận thuê nhà %s", nameB, phoneB, emailB, address)));
                                    notificationPresenter.sendNotification(new Notification(userIdB,
                                            String.format("Bạn đã thanh toán và xác nhận thuê nhà %s", address)));
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                });
                    }
                });
            }
        });
    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void loadNotification(List<Notification> notificationList) {

    }
}