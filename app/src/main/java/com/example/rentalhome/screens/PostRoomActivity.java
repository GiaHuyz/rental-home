package com.example.rentalhome.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentalhome.contract.PostRoomContract;
import com.example.rentalhome.databinding.PostRoomDetailBinding;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.presenter.PostRoomPresenter;
import com.example.rentalhome.service.ApiProvincesHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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

        ApiProvincesHelper.loadCities("https://provinces.open-api.vn/api/", this, binding.spinnerCity, binding.spinnerDistrict);
        presenter = new PostRoomPresenter(this, this);

        binding.edtFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    binding.edtFee.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[₫,]", "");

                    if(!cleanString.isEmpty()){
                        long price = Long.parseLong(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("vn").setRegion("VN").build()).format(price);

                        current = formatted;
                        binding.edtFee.setText(formatted);
                        binding.edtFee.setSelection(formatted.length());
                    }

                    binding.edtFee.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                if(!validateInput()) {
                    return;
                }
                Rooms rooms = new Rooms(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        Long.parseLong(binding.edtFee.getText().toString().replaceAll("[₫,]", "")),
                        binding.edtAddress.getText().toString(),
                        binding.spinnerCity.getSelectedItem().toString(),
                        binding.spinnerDistrict.getSelectedItem().toString(),
                        getAmenities(),
                        "available",
                        getSurround(),
                        binding.edtRules.getText().toString(),
                        Integer.parseInt(binding.edtArea.getText().toString()));

                presenter.onLoginClick(rooms, imageUris);
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

    private boolean validateInput() {
        if(imageUris.isEmpty()) {
            Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        String address = binding.edtAddress.getText().toString();
        if (address.isEmpty()) {
            binding.edtAddress.setError("Address is required");
            return false;
        }

        if(getAmenities().isEmpty()) {
            Toast.makeText(this, "Amenities is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        String area = binding.edtArea.getText().toString();
        if (area.isEmpty()) {
            binding.edtArea.setError("Area is required");
            return false;
        }

        String fee = binding.edtFee.getText().toString();
        if (fee.isEmpty()) {
            binding.edtFee.setError("Fee is required");
            return false;
        }

        if(Long.parseLong(binding.edtFee.getText().toString().replaceAll("[₫,]", "")) < 500000) {
            binding.edtFee.setError("Fee at least 500.000₫");
            return false;
        }

        if(getSurround().isEmpty()) {
            Toast.makeText(this, "Surround is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        String rules = binding.rules.getText().toString();
        if (rules.isEmpty()) {
            binding.rules.setError("Rules is required");
            return false;
        }

        return true;
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
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        finish();
    }
}
