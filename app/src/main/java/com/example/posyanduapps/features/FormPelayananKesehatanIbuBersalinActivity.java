package com.example.posyanduapps.features;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.R;

public class FormPelayananKesehatanIbuBersalinActivity extends Activity {

    private CheckBox cbInisiasiMenyusuDini;
    private EditText etTempatPersalinan, etFasilitasKesehatan, etRujukan;
    private Button btnSaveIbuBersalin;
    private TextView tvTitle;
    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelayanan_kesehatan_ibu_bersalin);
        View headerLayout = findViewById(R.id.header_layout);
        // Inisialisasi HeaderIconHelper
        new HeaderIconHelper(this, headerLayout);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Form Pelayanan Kesahatan Ibu Hamil");
        logout = findViewById(R.id.ivLogout);
        logout.setVisibility(View.GONE);
        // Initialize UI components
        cbInisiasiMenyusuDini = findViewById(R.id.cbInisiasiMenyusuDini);
        etTempatPersalinan = findViewById(R.id.etTempatPersalinan);
        etFasilitasKesehatan = findViewById(R.id.etFasilitasKesehatan);
        etRujukan = findViewById(R.id.etRujukan);
        btnSaveIbuBersalin = findViewById(R.id.btnSaveIbuBersalin);

        // Set listener for the save button
        btnSaveIbuBersalin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user has filled out the fields
                String tempatPersalinan = etTempatPersalinan.getText().toString();
                String fasilitasKesehatan = etFasilitasKesehatan.getText().toString();
                String rujukan = etRujukan.getText().toString();

                if (tempatPersalinan.isEmpty() || fasilitasKesehatan.isEmpty() || rujukan.isEmpty()) {
                    Toast.makeText(FormPelayananKesehatanIbuBersalinActivity.this,
                            "Pastikan semua data terisi", Toast.LENGTH_SHORT).show();
                } else {
                    // If all fields are filled, show a success message
                    String message = "Data berhasil disimpan";
                    if (cbInisiasiMenyusuDini.isChecked()) {
                        message += "\nInisiasi Menyusu Dini dicatat";
                    }
                    Toast.makeText(FormPelayananKesehatanIbuBersalinActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
