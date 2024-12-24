package com.example.posyanduapps.features;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.posyanduapps.R;

public class DetailSubkategoriActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_subkategori);

        // Get data from intent
        String subkategoriNama = getIntent().getStringExtra("subkategori_nama");

        // Set the title manually (TextView as title)
        TextView toolbarTitle = findViewById(R.id.tvTitle);
        toolbarTitle.setText(subkategoriNama);
        // Inisialisasi tombol
        Button btnBack = findViewById(R.id.btnBack);

        // Menambahkan fungsi untuk tombol kembali
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup activity saat tombol ditekan
            }
        });

        // Set content based on the subcategory
        TextView contentText = findViewById(R.id.tvContent);
        contentText.setText(getContentBySubkategori(subkategoriNama));
    }

    private String getContentBySubkategori(String subkategoriNama) {
        switch (subkategoriNama) {
            case "Gizi untuk Ibu Hamil":
                return "Gizi untuk ibu hamil sangat penting untuk kesehatan ibu dan perkembangan janin. Konsumsi makanan yang kaya akan protein, zat besi, dan asam folat.";
            case "Imunisasi":
                return "Imunisasi membantu melindungi bayi dan anak dari penyakit serius. Jadwalkan imunisasi rutin sesuai anjuran tenaga medis.";
            case "Perawatan Bayi":
                return "Perawatan bayi meliputi pemberian ASI eksklusif, menjaga kebersihan, dan perhatian terhadap tanda-tanda kesehatan bayi.";
            case "Kesehatan Anak":
                return "Kesehatan anak melibatkan asupan nutrisi seimbang, aktivitas fisik, dan pemeriksaan kesehatan secara rutin.";
            default:
                return "Informasi tentang subkategori ini belum tersedia.";
        }
    }
}
