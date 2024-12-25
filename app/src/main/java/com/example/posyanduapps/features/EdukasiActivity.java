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
        recyclerViewSubkategori.setLayoutManager(new GridLayoutManager(this, 1));
        ivProfile.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
        // Initialize adapter and data
        subkategoriAdapter = new SubkategoriAdapter(getSubkategoriData());
        recyclerViewSubkategori.setAdapter(subkategoriAdapter);
    }

    private List<Subkategori> getSubkategoriData() {
        List<Subkategori> subkategoriList = new ArrayList<>();
        subkategoriList.add(new Subkategori("PERAWATAN SEHARI-HARI IBU HAMIL",
                "Panduan lengkap perawatan sehari-hari ibu hamil untuk menjaga kesehatan dan kenyamanan selama masa kehamilan",
                R.drawable.perawatan_sehari_hari));
        subkategoriList.add(new Subkategori("YANG HARUS DIHINDARI IBU HAMIL",
                "Hal-hal yang perlu dihindari oleh ibu hamil demi menjaga kesehatan ibu dan janin selama masa kehamilan",
                R.drawable.warning_100dp_e8eaed_fill0_wght400_grad0_opsz48));
        subkategoriList.add(new Subkategori("PORSI MAKAN DAN MINUM IBU HAMIL",
                "Panduan tentang porsi makan dan minum yang seimbang untuk ibu hamil agar mendapatkan nutrisi yang cukup",
                R.drawable.ic_makan_edukasi));
        subkategoriList.add(new Subkategori("AKTIVITAS FISIK DAN LATIHAN FISIK IBU HAMIL",
                "Jenis aktivitas fisik dan latihan yang aman untuk ibu hamil guna menjaga kebugaran tubuh dan persiapan persalinan",
                R.drawable.aktivitas));
        subkategoriList.add(new Subkategori("TANDA BAHAYA KEHAMILAN",
                "Tanda-tanda bahaya yang perlu segera diperhatikan oleh ibu hamil agar dapat mendapatkan penanganan medis tepat waktu",
                R.drawable.tanda_bahaya));
        subkategoriList.add(new Subkategori("MASALAH LAIN PADA KEHAMILAN",
                "Masalah-masalah kesehatan lain yang sering terjadi selama kehamilan dan cara mengatasinya untuk kesehatan ibu dan bayi",
                R.drawable.masalah_lain));
        subkategoriList.add(new Subkategori("PERSIAPAN MELAHIRKAN",
                "Panduan persiapan melahirkan untuk ibu hamil agar proses persalinan berjalan lancar dan meminimalkan risiko komplikasi",
                R.drawable.persiapan_melahirkan));
        subkategoriList.add(new Subkategori("TANDA AWAL PERSALINAN",
                "Tanda-tanda awal persalinan yang harus dikenali oleh ibu hamil untuk mempersiapkan diri menghadapi kelahiran",
                R.drawable.tandawal));

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
