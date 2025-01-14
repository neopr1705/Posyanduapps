package com.example.posyanduapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Durasi splash screen (acak antara 1-3 detik)
        int timeout = (int) (Math.random() * 2000) + 1000;

        // Berpindah ke LoginActivity setelah timeout
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish(); // Menutup SplashScreenActivity
        }, timeout);
    }
}
