package com.example.rentalhome.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentalhome.contract.PostRoomContract;
import com.example.rentalhome.databinding.PostRoomDetailBinding;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.presenter.PostRoomPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class PostRoomActivity extends AppCompatActivity implements PostRoomContract.View {
    private PostRoomDetailBinding binding;
    private PostRoomPresenter presenter;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ArrayList<Uri> imageUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PostRoomDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new PostRoomPresenter(this);

        binding.btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rooms rooms = new Rooms(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        Long.parseLong(binding.edtFee.getText().toString()),
                        binding.edtAddress.getText().toString(), getAmenities(),
                        "available", getSurround(), binding.edtRules.getText().toString(),
                        Integer.parseInt(binding.edtArea.getText().toString()), binding.edtPhone.getText().toString());

                presenter.onLoginClick(rooms, imageUris);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                // Nếu người dùng chọn nhiều ảnh
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    addImageToLinearLayout(imageUri);
                    imageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                // Nếu người dùng chỉ chọn một ảnh
                Uri imageUri = data.getData();
                addImageToLinearLayout(imageUri);
                imageUris.add(imageUri);
            }
        }
    }

    private void addImageToLinearLayout(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
        layoutParams.setMargins(20, 0, 0, 0);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(imageUri);
        binding.imagesContainer.addView(imageView);
    }

    private ArrayList<String> getAmenities() {
        ArrayList<String> selectedItems = new ArrayList<>();

        if (binding.cbAir.isChecked()) {
            selectedItems.add(binding.cbAir.getText().toString());
        }
        if (binding.cbBath.isChecked()) {
            selectedItems.add(binding.cbBath.getText().toString());
        }
        if (binding.cbBed.isChecked()) {
            selectedItems.add(binding.cbBed.getText().toString());
        }
        if (binding.cbCook.isChecked()) {
            selectedItems.add(binding.cbCook.getText().toString());
        }
        if (binding.cbFridge.isChecked()) {
            selectedItems.add(binding.cbFridge.getText().toString());
        }
        if (binding.cbInternet.isChecked()) {
            selectedItems.add(binding.cbInternet.getText().toString());
        }
        if (binding.cbParking.isChecked()) {
            selectedItems.add(binding.cbParking.getText().toString());
        }
        if (binding.cbSecurity.isChecked()) {
            selectedItems.add(binding.cbSecurity.getText().toString());
        }
        if (binding.cbWM.isChecked()) {
            selectedItems.add(binding.cbWM.getText().toString());
        }

        return selectedItems;
    }

    private ArrayList<String> getSurround() {
        ArrayList<String> selectedItems = new ArrayList<>();

        if (binding.cbHospital.isChecked()) {
            selectedItems.add(binding.cbHospital.getText().toString());
        }
        if (binding.cbSchool.isChecked()) {
            selectedItems.add(binding.cbSchool.getText().toString());
        }
        if (binding.cbSuperMarket.isChecked()) {
            selectedItems.add(binding.cbSuperMarket.getText().toString());
        }

        return selectedItems;
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
