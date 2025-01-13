package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.FormDataIbuAdapter;
import com.example.posyanduapps.models.FormDataIbu;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.MainActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.SubkategoriAdapter;
import com.example.posyanduapps.models.Subkategori;

import java.util.ArrayList;
import java.util.List;

public class DataIbuActivity extends Activity implements View.OnClickListener {
    private RecyclerView recyclerViewDataIbu;
    private FormDataIbuAdapter adapter;
    private List<FormDataIbu> formDataIbuList;

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

        recyclerViewDataIbu = findViewById(R.id.recyclerViewDataIbu);
        recyclerViewDataIbu.setLayoutManager(new LinearLayoutManager(this));

        ivSettings.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam

        // Initialize adapter and data

        // Sample data for FormDataIbu
        List<FormDataIbu> formDataIbuList = generateFormDataIbuList();


        adapter = new FormDataIbuAdapter(formDataIbuList);
        recyclerViewDataIbu.setAdapter(adapter);
    }

    // Fungsi untuk menghasilkan sample data FormDataIbu
    public List<FormDataIbu> generateFormDataIbuList() {
        List<FormDataIbu> formDataIbuList = new ArrayList<>();

        // Menambahkan data sample berdasarkan tabel Pelayanan Kesehatan Ibu dan Anak
        formDataIbuList.add(new FormDataIbu(
                "1",
                "Form Pelayanan Kesehatan Ibu Hamil",
                "Form ini berisi informasi pemeriksaan kesehatan untuk ibu hamil pada Trimester 1, 2, dan 3",
                R.drawable.ic_launcher_foreground
        ));
        formDataIbuList.add(new FormDataIbu(
                "2",
                "Form Pelayanan Kesehatan Ibu Bersalin",
                "Form ini berisi informasi tentang pelayanan kesehatan untuk ibu yang sedang bersalin",
                R.drawable.ic_launcher_foreground
        ));
        formDataIbuList.add(new FormDataIbu(
                "3",
                "Form Pelayanan Kesehatan Ibu Nifas",
                "Form ini berisi pemeriksaan kesehatan ibu nifas selama 42 hari setelah persalinan",
                R.drawable.ic_launcher_foreground
        ));
        formDataIbuList.add(new FormDataIbu(
                "4",
                "Form Pelayanan Kesehatan Bayi Baru Lahir",
                "Form ini berisi pelayanan kesehatan untuk bayi baru lahir (neonatus) usia 0-28 hari",
                R.drawable.ic_launcher_foreground
        ));

        return formDataIbuList;
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
