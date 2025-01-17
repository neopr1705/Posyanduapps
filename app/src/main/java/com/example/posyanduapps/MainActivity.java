package com.example.posyanduapps;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

import com.example.posyanduapps.Helper.HeaderIconHelper;

import com.example.posyanduapps.features.*;

public class MainActivity extends Activity implements View.OnClickListener {

    ImageView ivHome, ivReminder, ivAddAbsensi, ivProfile, ivSettings;
    TextView tvTitle,tvstatusKehamilan,tvTrimester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ambil referensi ke layout header
        View headerLayout = findViewById(R.id.header_layout);
        // Inisialisasi HeaderIconHelper
        new HeaderIconHelper(this, headerLayout);

        tvTitle = findViewById(R.id.tvTitle);
        tvstatusKehamilan = findViewById(R.id.tvKehamilanStatus);
        tvTrimester=findViewById(R.id.tvTrimester);
        tvTitle.setText(getText(R.string.str_Menu));

        // Mengambil currentUser dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("currentUser", null);  // null jika tidak ada
        String currentNama = sharedPreferences.getString("currentNama", null);  // null jika tidak ada
        String currentAlamat = sharedPreferences.getString("currentAlamat", null);  // null jika tidak ada
        String currentNomor = sharedPreferences.getString("currentNomor", null);  // null jika tidak ada
        String currentUsiaKehamilan = sharedPreferences.getString("currentUsiaKehamilan", null);  // null jika tidak ada
        String currentTanggal = sharedPreferences.getString("currentTanggal", null);  // null jika tidak ada

        tvstatusKehamilan.setText("Hallo, " + currentNama + " !");
        tvTrimester.setText("Usia Kehamilanmu " + currentUsiaKehamilan + " Minggu !");

        //footer
        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);

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
            intent = new Intent(this, EdukasiBumilActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}
