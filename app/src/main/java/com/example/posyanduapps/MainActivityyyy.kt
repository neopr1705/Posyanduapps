//package com.example.posyanduapps;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//public class MainActivity extends Activity implements View.OnClickListener {
//
//    // Mendeklarasikan tombol untuk masing-masing fitur
//    Button btnAbsensi;
//    Button btnPengingat;
//    Button btnEdukasi;
//    Button btnDataIbu;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Menghubungkan elemen layout dengan kode
//        btnAbsensi = findViewById(R.id.btnAbsensi);
//        btnPengingat = findViewById(R.id.btnPengingat);
//        btnEdukasi = findViewById(R.id.btnEdukasi);
//        btnDataIbu = findViewById(R.id.btnDataIbu);
//
//        // Menetapkan OnClickListener untuk tombol
//        btnAbsensi.setOnClickListener(this);
//        btnPengingat.setOnClickListener(this);
//        btnEdukasi.setOnClickListener(this);
//        btnDataIbu.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        // Menggunakan Intent untuk membuka aktivitas yang sesuai dengan tombol yang diklik
//        Intent intent;
//
//        // Menentukan aktivitas yang akan dibuka berdasarkan tombol yang diklik
//        if (v.getId() == btnAbsensi.getId()) {
//            intent = new Intent(this, AbsensiActivity.class);
//            startActivity(intent);
//        } else if (v.getId() == btnPengingat.getId()) {
//            intent = new Intent(this, PengingatActivity.class);
//            startActivity(intent);
//        } else if (v.getId() == btnEdukasi.getId()) {
//            intent = new Intent(this, EdukasiBumilActivity.class);
//            startActivity(intent);
//        } else if (v.getId() == btnDataIbu.getId()) {
//            intent = new Intent(this, DataIbuActivity.class);
//            startActivity(intent);
//        }
//    }
//}
