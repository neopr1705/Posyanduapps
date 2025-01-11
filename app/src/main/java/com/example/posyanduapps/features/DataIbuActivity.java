package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.MainActivity;
import com.example.posyanduapps.R;

public class DataIbuActivity extends Activity implements View.OnClickListener {

    private EditText edtNamaIbu, edtUsiaIbu, edtStatusKehamilan;
    private Button btnSimpanData;
    private ImageView ivHome, ivReminder, ivAddAbsensi, ivProfile, ivSettings;
    private Intent intent;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ibu);
        View headerLayout = findViewById(R.id.header_layout);
        // Inisialisasi HeaderIconHelper
        new HeaderIconHelper(this, headerLayout);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getText(R.string.str_DataIbu));

        ivSettings = findViewById(R.id.ivSettings);
        ivSettings.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
        //footer
        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);
        ivAddAbsensi = findViewById(R.id.ivAddAbsensi);
        ivProfile = findViewById(R.id.ivProfile);


        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivAddAbsensi.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
//        edtNamaIbu = findViewById(R.id.edtNamaIbu);
//        edtUsiaIbu = findViewById(R.id.edtUsiaIbu);
//        edtStatusKehamilan = findViewById(R.id.edtStatusKehamilan);
//        btnSimpanData = findViewById(R.id.btnSimpanData);
//
//        btnSimpanData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // Dapatkan aksi klik jika diperlukan
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
//        if (view.getId() == btnSimpanData.getId()) {
//            String namaIbu = edtNamaIbu.getText().toString();
//            String usiaIbu = edtUsiaIbu.getText().toString();
//            String statusKehamilan = edtStatusKehamilan.getText().toString();
//
//            if (!namaIbu.isEmpty() && !usiaIbu.isEmpty() && !statusKehamilan.isEmpty()) {
//                // Simpan data ke database lokal atau sistem
//                simpanDataIbu(namaIbu, usiaIbu, statusKehamilan);
//            } else {
//                Toast.makeText(this, "Harap masukkan semua data", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
//
//    private void simpanDataIbu(String namaIbu, String usiaIbu, String statusKehamilan) {
//        // Logika untuk menyimpan data ibu ke database
//        Toast.makeText(this, "Data Ibu berhasil disimpan", Toast.LENGTH_SHORT).show();
//    }
}
