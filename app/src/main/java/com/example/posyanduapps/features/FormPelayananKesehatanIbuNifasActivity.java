package com.example.posyanduapps.features;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.R;

public class FormPelayananKesehatanIbuNifasActivity extends Activity {

    private CheckBox cbPeriksaPayudara, cbPeriksaPerdarahan, cbPeriksaJalanLahir;
    private CheckBox cbVitaminA, cbKBPascaPersalinan, cbKonseling, cbTataLaksanaKasus;
    private Button btnSaveIbuNifas;
    private TextView tvTitle;
    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelayanan_kesehatan_ibu_nifas);
        View headerLayout = findViewById(R.id.header_layout);
        // Inisialisasi HeaderIconHelper
        new HeaderIconHelper(this, headerLayout);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Form Pelayanan Kesahatan Ibu Hamil");
        logout = findViewById(R.id.ivLogout);
        logout.setVisibility(View.GONE);
        // Initialize UI components
        cbPeriksaPayudara = findViewById(R.id.cbPeriksaPayudara);
        cbPeriksaPerdarahan = findViewById(R.id.cbPeriksaPerdarahan);
        cbPeriksaJalanLahir = findViewById(R.id.cbPeriksaJalanLahir);
        cbVitaminA = findViewById(R.id.cbVitaminA);
        cbKBPascaPersalinan = findViewById(R.id.cbKBPascaPersalinan);
        cbKonseling = findViewById(R.id.cbKonseling);
        cbTataLaksanaKasus = findViewById(R.id.cbTataLaksanaKasus);
        btnSaveIbuNifas = findViewById(R.id.btnSaveIbuNifas);

        // Set listener for the save button
        btnSaveIbuNifas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collect selected checkboxes
                StringBuilder selectedServices = new StringBuilder();
                if (cbPeriksaPayudara.isChecked()) selectedServices.append("Periksa Payudara (ASI)\n");
                if (cbPeriksaPerdarahan.isChecked()) selectedServices.append("Periksa Perdarahan\n");
                if (cbPeriksaJalanLahir.isChecked()) selectedServices.append("Periksa Jalan Lahir\n");
                if (cbVitaminA.isChecked()) selectedServices.append("Vitamin A\n");
                if (cbKBPascaPersalinan.isChecked()) selectedServices.append("KB Pasca Persalinan\n");
                if (cbKonseling.isChecked()) selectedServices.append("Konseling\n");
                if (cbTataLaksanaKasus.isChecked()) selectedServices.append("Tata Laksana Kasus\n");

                // If no services are selected, show a warning message
                if (selectedServices.length() == 0) {
                    Toast.makeText(FormPelayananKesehatanIbuNifasActivity.this,
                            "Pastikan semua data terisi", Toast.LENGTH_SHORT).show();
                } else {
                    // Show success message with selected services
                    Toast.makeText(FormPelayananKesehatanIbuNifasActivity.this,
                            "Data berhasil disimpan:\n" + selectedServices.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
