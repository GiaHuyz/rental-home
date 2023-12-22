package com.example.rentalhome.screens;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
import com.example.rentalhome.contract.UserContract;
import com.example.rentalhome.databinding.DetailRoomBinding;
import com.example.rentalhome.dto.Comment;
import com.example.rentalhome.dto.User;
import com.example.rentalhome.presenter.CommentPresenter;
import com.example.rentalhome.presenter.UserPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailRoomActivity extends AppCompatActivity implements CommentContract.View, UserContract.View {
    private DetailRoomBinding binding;
    private String roomId;
    private String userId;
    private User user;

    private CommentPresenter commentPresenter;
    private UserPresenter userPresenter;
    private CommentsAdapter commentsAdapter;

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

        roomId = bundle.getString("Id");
        String rules = bundle.getString("Rules");
        ArrayList<String> images = bundle.getStringArrayList("Images");
        long price = bundle.getLong("Price");
        String address = bundle.getString("Address");
        ArrayList<String> amenities = bundle.getStringArrayList("Amenities");
        int area = bundle.getInt("Area");
        ArrayList<String> surround = bundle.getStringArrayList("Surround");
        String ownerId = bundle.getString("ownerId");

        user = (User) bundle.getSerializable("User");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userPresenter.isFavorite(userId, roomId);

        ImagesAdapter adapter = new ImagesAdapter(images);
        binding.viewPagerImages.setAdapter(adapter);

        binding.tvAmenities.setText(TextUtils.join(", ", amenities));
        binding.tvRules.setText(rules);
        binding.tvPrice.setText(String.valueOf(price));
        binding.tvAddres.setText(address);
        binding.tvArea.setText(String.valueOf(area));

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

        binding.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPresenter.onFavoriteClick(userId, roomId);
            }
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
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
}
