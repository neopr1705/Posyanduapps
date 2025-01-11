package com.example.posyanduapps;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.example.posyanduapps.Helper.HeaderIconHelper;

import com.example.posyanduapps.features.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
