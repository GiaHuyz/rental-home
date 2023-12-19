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
import com.example.rentalhome.presenter.PostRoomPresenter;

import java.util.ArrayList;

public class PostRoomActivity extends AppCompatActivity implements PostRoomContract.View {
    private PostRoomDetailBinding binding;
    private PostRoomPresenter presenter;

    private static final int PICK_IMAGE_REQUEST = 1;
    private ArrayList<Uri> imageUris;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PostRoomDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new PostRoomPresenter(this);

        binding.btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
                }
            } else if (data.getData() != null) {
                // Nếu người dùng chỉ chọn một ảnh
                Uri imageUri = data.getData();
                addImageToLinearLayout(imageUri);
            }
        }
    }

    private void addImageToLinearLayout(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        imageView.setImageURI(imageUri);
        binding.imagesContainer.addView(imageView);
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
