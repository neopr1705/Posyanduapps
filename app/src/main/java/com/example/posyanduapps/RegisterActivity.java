package com.example.posyanduapps;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.posyanduapps.Helper.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {

    private EditText etNamaLengkap, etAlamatLengkap,  etUsiaKehamilan,etNomorHp,etUsername,etPassword;
    private TextView etTanggalLahir,tvTotalUser;
    private Button btnDaftar;
    private ImageView exitbutton;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    private String selectedTanggal;
    private String TotalUser;
    private String url="https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";
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
                decrementUid();
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
        getUid();
    }

    /**
     * Mendaftarkan pengguna dan menampilkan validasi input.
     */
    private void registerUser() {
        String namaLengkap = etNamaLengkap.getText().toString().trim();
        String alamatLengkap = etAlamatLengkap.getText().toString().trim();
        String tanggalLahir = etTanggalLahir.getText().toString().trim();
        String usiaKehamilan = etUsiaKehamilan.getText().toString().trim()+" minggu";
        String nomorHp = "62"+etNomorHp.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(namaLengkap) || TextUtils.isEmpty(alamatLengkap) ||
                TextUtils.isEmpty(tanggalLahir) || TextUtils.isEmpty(usiaKehamilan) ||
                TextUtils.isEmpty(nomorHp)) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = insertUser(namaLengkap, alamatLengkap, tanggalLahir, usiaKehamilan, nomorHp, username, password);


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

    private boolean insertUser(String namaLengkap, String alamatLengkap, String tanggalLahir, String usiaKehamilan, String nomorHp, String username, String password) {
        // Referensi ke node "users"
        DatabaseReference usersRef = FirebaseDatabase.getInstance(url)
                .getReference("users");
        // Membuat objek user baru dengan struktur sesuai
        String userId = usersRef.push().getKey(); // Generate unique key untuk user
        if (userId == null) {
            Toast.makeText(this, "Gagal membuat user ID.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Membuat objek user
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("id", TotalUser);
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("nama_lengkap", namaLengkap);
        newUser.put("alamat_lengkap", alamatLengkap);
        newUser.put("tanggal_lahir", tanggalLahir);
        newUser.put("usia_kehamilan", usiaKehamilan);
        newUser.put("nomor_hp", nomorHp);

        // Melakukan insert ke Firebase
        usersRef.child(userId).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "User berhasil ditambahkan!", Toast.LENGTH_SHORT).show();


                    } else {
                        decrementUid();
                        Toast.makeText(this, "Gagal menambahkan user: " + task.getException(), Toast.LENGTH_SHORT).show();


                    }
                });
        return true;
    }

    public void getUid() {
        DatabaseReference currentID = FirebaseDatabase.getInstance(url)
                .getReference("TotalUser");

        currentID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    // Ambil nilai TotalUser saat ini
                    int currentTotal = snapshot.getValue(Integer.class) != null
                            ? snapshot.getValue(Integer.class)
                            : 0;

                    // Tambahkan 1 (increment)
                    int newTotal = currentTotal + 1;
                    TotalUser = ""+newTotal;
                    // Simpan nilai baru ke database
                    currentID.setValue(newTotal);
                } catch (Exception e) {
                    Log.d("Error", "Error processing TotalUser: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Can't connect to database: " + error.getMessage());
            }
        });
    }

    public void decrementUid() {
        DatabaseReference currentID = FirebaseDatabase.getInstance(url)
                .getReference("TotalUser");

        currentID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    // Ambil nilai TotalUser saat ini
                    int currentTotal = snapshot.getValue(Integer.class) != null
                            ? snapshot.getValue(Integer.class)
                            : 0;

                    // Tambahkan 1 (increment)
                    int newTotal = currentTotal - 1;
                    currentID.setValue(newTotal);

                } catch (Exception e) {
                    Log.d("Error", "Error processing TotalUser: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", "Can't connect to database: " + error.getMessage());
            }
        });
    }


}
