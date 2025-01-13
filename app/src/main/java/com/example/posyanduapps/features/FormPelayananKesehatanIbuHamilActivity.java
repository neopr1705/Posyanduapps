package com.example.posyanduapps.features;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.R;

public class FormPelayananKesehatanIbuHamilActivity extends Activity {

    private TextView tvFormDetails, tvIbuHamilTitle, tvPemeriksaanTitle;
    private EditText etHpht, etTimbang, etUkurLengan, etSistolik, etDiastolik;
    private EditText etTinggiRahim, etLetakDenyut, etStatusImunisasi, etKonseling, etSkriningDokter, etTabletTambahDarah;
    private EditText etHb, etGolonganDarah, etProteinUrine, etGulaDarah, etPpia, etTataLaksanaKasus;
    private Button btnSimpan, btnTrimester1, btnTrimester2, btnTrimester3;
    private String selectedTrimester = ""; // To store the selected trimester
    private TextView tvTitle;
    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelayanan_kesehatan_ibu_hamil);

        // Ambil referensi ke layout header
        View headerLayout = findViewById(R.id.header_layout);
        // Inisialisasi HeaderIconHelper
        new HeaderIconHelper(this, headerLayout);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Form Pelayanan Kesahatan Ibu Hamil");
        logout = findViewById(R.id.ivLogout);
        logout.setVisibility(View.GONE);

        // Initialize UI components
        initializeUI();

        // Set up the save button click listener
        setupSaveButton();

        // Set up the trimester button click listeners
        setupTrimesterButtons();
    }

    // Initialize UI components
    private void initializeUI() {
        tvIbuHamilTitle = findViewById(R.id.tvIbuHamilTitle);
        tvPemeriksaanTitle = findViewById(R.id.tvPemeriksaanTitle);

        etHpht = findViewById(R.id.et_hpht);
        btnTrimester1 = findViewById(R.id.btn_trimester1);
        btnTrimester2 = findViewById(R.id.btn_trimester2);
        btnTrimester3 = findViewById(R.id.btn_trimester3);

        etTimbang = findViewById(R.id.et_timbang);
        etUkurLengan = findViewById(R.id.et_ukur_lengan);
        etSistolik = findViewById(R.id.et_sistolik);
        etDiastolik = findViewById(R.id.et_diastolik);
        etTinggiRahim = findViewById(R.id.et_tinggi_rahim);
        etLetakDenyut = findViewById(R.id.et_letak_denyut);
        etStatusImunisasi = findViewById(R.id.et_status_imunisasi);
        etKonseling = findViewById(R.id.et_konseling);
        etSkriningDokter = findViewById(R.id.et_skrining_dokter);
        etTabletTambahDarah = findViewById(R.id.et_tablet_tambah_darah);
        etHb = findViewById(R.id.et_hb);
        etGolonganDarah = findViewById(R.id.et_golongan_darah);
        etProteinUrine = findViewById(R.id.et_protein_urine);
        etGulaDarah = findViewById(R.id.et_gula_darah);
        etPpia = findViewById(R.id.et_ppia);
        etTataLaksanaKasus = findViewById(R.id.et_tata_laksana_kasus);

        btnSimpan = findViewById(R.id.btn_simpan);
    }


    // Set up the Save Button click listener
    private void setupSaveButton() {
        btnSimpan.setOnClickListener(v -> saveFormData());
    }

    // Set up the Trimester button click listeners
    private void setupTrimesterButtons() {
        btnTrimester1.setOnClickListener(v -> selectTrimester("Trimester 1"));
        btnTrimester2.setOnClickListener(v -> selectTrimester("Trimester 2"));
        btnTrimester3.setOnClickListener(v -> selectTrimester("Trimester 3"));
    }

    // Select a trimester and update button colors
    private void selectTrimester(String trimester) {
        selectedTrimester = trimester;

        // Reset button colors
        btnTrimester1.setBackgroundResource(R.drawable.button_base);
        btnTrimester2.setBackgroundResource(R.drawable.button_base);
        btnTrimester3.setBackgroundResource(R.drawable.button_base);

        // Change color for selected trimester button
        if (trimester.equals("Trimester 1")) {
            btnTrimester1.setBackgroundResource(R.drawable.button_selected);
        } else if (trimester.equals("Trimester 2")) {
            btnTrimester2.setBackgroundResource(R.drawable.button_selected);
        } else if (trimester.equals("Trimester 3")) {
            btnTrimester3.setBackgroundResource(R.drawable.button_selected);
        }
    }

    // Save the form data
    private void saveFormData() {
        // Get values from input fields
        String hpht = etHpht.getText().toString();
        String timbang = etTimbang.getText().toString();
        String ukurLengan = etUkurLengan.getText().toString();
        String sistolik = etSistolik.getText().toString();
        String diastolik = etDiastolik.getText().toString();
        String tinggiRahim = etTinggiRahim.getText().toString();
        String letakDenyut = etLetakDenyut.getText().toString();
        String statusImunisasi = etStatusImunisasi.getText().toString();
        String konseling = etKonseling.getText().toString();
        String skriningDokter = etSkriningDokter.getText().toString();
        String tabletTambahDarah = etTabletTambahDarah.getText().toString();
        String hb = etHb.getText().toString();
        String golonganDarah = etGolonganDarah.getText().toString();
        String proteinUrine = etProteinUrine.getText().toString();
        String gulaDarah = etGulaDarah.getText().toString();
        String ppia = etPpia.getText().toString();
        String tataLaksanaKasus = etTataLaksanaKasus.getText().toString();

        // Validation and saving data
        if (hpht.isEmpty() || selectedTrimester.isEmpty()) {
            showToast("Please fill in all the required fields and select a trimester.");
        } else {
            // Show confirmation message
            String message = createConfirmationMessage(hpht, selectedTrimester, timbang, ukurLengan, sistolik, diastolik, tinggiRahim,
                    letakDenyut, statusImunisasi, konseling, skriningDokter, tabletTambahDarah, hb, golonganDarah,
                    proteinUrine, gulaDarah, ppia, tataLaksanaKasus);
            showToast(message);
        }
    }

    // Create a confirmation message to show after saving the form data
    private String createConfirmationMessage(String hpht, String trimester, String timbang, String ukurLengan, String sistolik, String diastolik,
                                             String tinggiRahim, String letakDenyut, String statusImunisasi, String konseling, String skriningDokter,
                                             String tabletTambahDarah, String hb, String golonganDarah, String proteinUrine, String gulaDarah,
                                             String ppia, String tataLaksanaKasus) {
        return "Form Data Saved:\nHPHT: " + hpht +
                "\nTrimester: " + trimester +
                "\nTimbang: " + timbang +
                "\nUkur Lengan: " + ukurLengan +
                "\nTekanan Darah: " + sistolik + "/" + diastolik +
                "\nTinggi Rahim: " + tinggiRahim +
                "\nLetak Denyut Jantung: " + letakDenyut +
                "\nStatus Imunisasi: " + statusImunisasi +
                "\nKonseling: " + konseling +
                "\nSkrining Dokter: " + skriningDokter +
                "\nTablet Tambah Darah: " + tabletTambahDarah +
                "\nHb: " + hb +
                "\nGolongan Darah: " + golonganDarah +
                "\nProtein Urine: " + proteinUrine +
                "\nGula Darah: " + gulaDarah +
                "\nPPIA: " + ppia +
                "\nTata Laksana Kasus: " + tataLaksanaKasus;
    }

    // Show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
