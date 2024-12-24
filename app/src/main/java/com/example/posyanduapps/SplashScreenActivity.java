package com.example.posyanduapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.posyanduapps.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.graphics.Matrix;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Waktu tunggu splash screen (acak antara 1-3 detik)
        int timeout = (int) (Math.random() * 2000) + 1000;

        // Handler untuk menjalankan kode setelah timeout
        new Handler().postDelayed(() -> {
            // Intent ke MainActivity
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Menutup SplashScreenActivity agar tidak bisa kembali ke sini
        }, timeout);
    }
}
