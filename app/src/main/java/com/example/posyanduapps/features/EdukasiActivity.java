package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posyanduapps.MainActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.SubkategoriAdapter;
import com.example.posyanduapps.models.Subkategori;

import java.util.ArrayList;
import java.util.List;

public class EdukasiActivity extends Activity implements View.OnClickListener {

    private RecyclerView recyclerViewSubkategori;
    private SubkategoriAdapter subkategoriAdapter;
    private ImageView ivHome, ivReminder, ivAddAbsensi, ivProfile, ivSettings;
    private Intent intent;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edukasi);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getText(R.string.str_EduGizi));
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
        recyclerViewSubkategori = findViewById(R.id.recyclerViewSubkategori);
        recyclerViewSubkategori.setLayoutManager(new GridLayoutManager(this, 2));
        ivProfile.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
        // Initialize adapter and data
        subkategoriAdapter = new SubkategoriAdapter(getSubkategoriData());
        recyclerViewSubkategori.setAdapter(subkategoriAdapter);
    }

    private List<Subkategori> getSubkategoriData() {
        List<Subkategori> subkategoriList = new ArrayList<>();
        subkategoriList.add(new Subkategori("PERAWATAN SEHARI-HARI IBU HAMIL", R.drawable.foreground));
        subkategoriList.add(new Subkategori("YANG HARUS DIHINDARI IBU HAMIL ", R.drawable.ic_launcher_foreground));
        subkategoriList.add(new Subkategori("PORSI MAKAN DAN MINUM IBU HAMIL", R.drawable.ic_launcher_foreground));
        subkategoriList.add(new Subkategori("AKTIVITAS FISIK DAN LATIHAN FISIK IBU HAMIL", R.drawable.ic_launcher_foreground));
        subkategoriList.add(new Subkategori("TANDA BAHAYA KEHAMILAN", R.drawable.ic_launcher_foreground));
        subkategoriList.add(new Subkategori("MASALAH LAIN PADA KEHAMILAN", R.drawable.ic_launcher_foreground));
        subkategoriList.add(new Subkategori("PERSIAPAN MELAHIRKAN", R.drawable.ic_launcher_foreground));
        subkategoriList.add(new Subkategori("TANDA AWAL PERSALINAN", R.drawable.ic_launcher_foreground));
        // Add more categories as needed
        return subkategoriList;
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
    }
}
