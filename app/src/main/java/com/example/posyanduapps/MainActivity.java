package com.example.posyanduapps;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import com.example.posyanduapps.features.*;

public class MainActivity extends Activity implements View.OnClickListener {

    ImageView ivHome, ivReminder, ivAddAbsensi, ivProfile, ivSettings;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getText(R.string.str_Menu));

        //footer
        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);
        ivAddAbsensi = findViewById(R.id.ivAddAbsensi);
        ivProfile = findViewById(R.id.ivProfile);
        ivSettings = findViewById(R.id.ivSettings);

        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivAddAbsensi.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);

        ImageView ivHome = findViewById(R.id.ivHome);
        ivHome.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam


    }

    @Override
    public void onClick(View v) {
        // Menggunakan Intent untuk membuka aktivitas yang sesuai dengan tombol yang diklik
        Intent intent;

        if(v.getId() == ivHome.getId()){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (v.getId() == ivAddAbsensi.getId()) {
            intent = new Intent(this, AbsensiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (v.getId() == ivReminder.getId()) {
            intent = new Intent(this, PengingatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (v.getId() == ivSettings.getId()) {
            intent = new Intent(this, DataIbuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (v.getId() == ivProfile.getId()) {
            intent = new Intent(this, EdukasiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}
