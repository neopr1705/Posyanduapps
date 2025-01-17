package com.example.posyanduapps;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.example.posyanduapps.utils.FirebaseManager;
import com.google.firebase.database.DataSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {

    private EditText etNamaLengkap, etAlamatLengkap, etUsiaKehamilan, etNomorHp, etUsername, etPassword;
    private TextView etTanggalLahir;
    private Button btnDaftar;
    private ImageView exitButton;

    private String selectedTanggal;
    private String totalUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        setupListeners();
        fetchTotalUserId();
    }

    private void initializeViews() {
        etNamaLengkap = findViewById(R.id.etNamaLengkap);
        etAlamatLengkap = findViewById(R.id.etAlamatLengkap);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etUsiaKehamilan = findViewById(R.id.etUsiaKehamilan);
        etNomorHp = findViewById(R.id.etNomorHp);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnDaftar = findViewById(R.id.btnDaftar);
        exitButton = findViewById(R.id.exitbutton);
    }

    private void setupListeners() {
        etTanggalLahir.setOnClickListener(v -> showDatePickerDialog());
        btnDaftar.setOnClickListener(v -> registerUser());
        exitButton.setOnClickListener(v -> navigateToLoginAndDecrementId());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.CustomDatePicker,
                (view, year, month, dayOfMonth) -> {
                    selectedTanggal = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etTanggalLahir.setText(selectedTanggal);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void registerUser() {
        String namaLengkap = etNamaLengkap.getText().toString().trim();
        String alamatLengkap = etAlamatLengkap.getText().toString().trim();
        String tanggalLahir = etTanggalLahir.getText().toString().trim();
        String usiaKehamilan = etUsiaKehamilan.getText().toString().trim() + " minggu";
        String nomorHp = "62" + etNomorHp.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (areInputsValid(namaLengkap, alamatLengkap, tanggalLahir, usiaKehamilan, nomorHp)) {
            saveUserToFirebase(namaLengkap, alamatLengkap, tanggalLahir, usiaKehamilan, nomorHp, username, password);
        }
    }

    private boolean areInputsValid(String... inputs) {
        for (String input : inputs) {
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void saveUserToFirebase(String namaLengkap, String alamatLengkap, String tanggalLahir, String usiaKehamilan, String nomorHp, String username, String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", totalUserId);
        user.put("nama_lengkap", namaLengkap);
        user.put("alamat_lengkap", alamatLengkap);
        user.put("tanggal_lahir", tanggalLahir);
        user.put("usia_kehamilan", usiaKehamilan);
        user.put("nomor_hp", nomorHp);
        user.put("username", username);
        user.put("password", password);

        FirebaseManager.writeData("users/" + username, user, new FirebaseManager.FirebaseCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(RegisterActivity.this, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }

            @Override
            public void onError(String error) {
                decrementTotalUserId();
                Toast.makeText(RegisterActivity.this, "Gagal mendaftarkan pengguna: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTotalUserId() {
        FirebaseManager.readData("TotalUser", new FirebaseManager.FirebaseCallback<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                int currentTotal = snapshot.getValue(Integer.class) != null ? snapshot.getValue(Integer.class) : 0;
                totalUserId = String.valueOf(currentTotal + 1);
                FirebaseManager.writeData("TotalUser", currentTotal + 1, new FirebaseManager.FirebaseCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.d("RegisterActivity", "TotalUser updated successfully.");
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("RegisterActivity", "Failed to update TotalUser: " + error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                Log.e("RegisterActivity", "Error fetching TotalUser: " + error);
            }
        });
    }

    private void decrementTotalUserId() {
        FirebaseManager.readData("TotalUser", new FirebaseManager.FirebaseCallback<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                int currentTotal = snapshot.getValue(Integer.class) != null ? snapshot.getValue(Integer.class) : 1;
                FirebaseManager.writeData("TotalUser", currentTotal - 1, new FirebaseManager.FirebaseCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.d("RegisterActivity", "TotalUser decremented successfully.");
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("RegisterActivity", "Failed to decrement TotalUser: " + error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                Log.e("RegisterActivity", "Error fetching TotalUser: " + error);
            }
        });
    }

    private void navigateToLoginAndDecrementId() {
        decrementTotalUserId();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        finish();

    }
}
