package com.skool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler splashHandler = new Handler();

        Runnable runnable = new Runnable() {
@Override
public void run() {
            Intent intent = new Intent(SplashActivity.this, StudentSignInActivity.class);
            startActivity(intent);
            finish();
        }
        };

        splashHandler.postDelayed(runnable, 1000);
    }
}