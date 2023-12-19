package com.example.rentalhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.rentalhome.screens.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //screen wait for 2 second and then Lanch LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        },2000);
    }
}