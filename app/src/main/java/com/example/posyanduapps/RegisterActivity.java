package com.example.posyanduapps;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.posyanduapps.Helper.DatabaseHelper;

import java.util.Calendar;

public class RegisterActivity extends Activity {

    private EditText etNamaLengkap, etAlamatLengkap, etTanggalLahir, etUsiaKehamilan,etNomorHp,etUsername,etPassword;
    private Button btnDaftar;
    private ImageView exitbutton;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    private String selectedTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Load layout XML

        initializeViews();
        etTanggalLahir.setOnClickListener(v -> showDatePickerDialog());

        // Set listener untuk tombol daftar
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Set listener untuk tombol keluar
        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Menutup activity
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.CustomDatePicker,
                (view, year, month, dayOfMonth) -> {
                    // Perbarui kalender dengan tanggal yang dipilih
                    calendar.set(year, month, dayOfMonth);
                    selectedTanggal = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etTanggalLahir.setText(selectedTanggal);
                    
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * Menginisialisasi tampilan berdasarkan ID.
     */
    private void initializeViews() {
        etNamaLengkap = findViewById(R.id.etNamaLengkap);
        etAlamatLengkap = findViewById(R.id.etAlamatLengkap);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etUsiaKehamilan = findViewById(R.id.etUsiaKehamilan);
        etNomorHp = findViewById(R.id.etNomorHp);
        btnDaftar = findViewById(R.id.btnDaftar);
        exitbutton = findViewById(R.id.exitbutton);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        dbHelper= new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();  // Memastikan onCreate() dipanggil
    }

    /**
     * Mendaftarkan pengguna dan menampilkan validasi input.
     */
    private void registerUser() {
        String namaLengkap = etNamaLengkap.getText().toString().trim();
        String alamatLengkap = etAlamatLengkap.getText().toString().trim();
        String tanggalLahir = etTanggalLahir.getText().toString().trim();
        String usiaKehamilan = etUsiaKehamilan.getText().toString().trim();
        String nomorHp = etNomorHp.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(namaLengkap) || TextUtils.isEmpty(alamatLengkap) ||
                TextUtils.isEmpty(tanggalLahir) || TextUtils.isEmpty(usiaKehamilan) ||
                TextUtils.isEmpty(nomorHp)) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.insertUser(namaLengkap, alamatLengkap, tanggalLahir, usiaKehamilan, nomorHp, username, password);


        // Pastikan dialog hanya ditampilkan jika activity tidak sedang ditutup
        if (!isFinishing()) {
            if (success) {
                // Jika berhasil, navigasi ke LoginActivity
                new android.app.AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("Informasi")
                        .setMessage("Pendaftaran Berhasil!")
                        .setPositiveButton("Oke", (dialog, which) -> {
                            dialog.dismiss(); // Menutup dialog
                            // Navigasi ke LoginActivity
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Menutup activity saat ini
                        })
                        .show();
            } else {
                // Tampilkan pesan jika pendaftaran gagal
                Toast.makeText(this, "Pendaftaran gagal!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
