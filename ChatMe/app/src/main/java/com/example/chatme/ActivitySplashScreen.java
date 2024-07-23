package com.example.chatme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("LOGIN", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

                if (isLoggedIn) {
                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityMenu.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityLogin.class);
                    startActivity(intent);
                }
                finish();
            }
        },3000);
    }
}
