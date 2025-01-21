package com.example.posyanduapps;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.posyanduapps.features.DashboardActivity;
import com.example.posyanduapps.features.MonitorUsersActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    FirebaseDatabase database;
    DatabaseReference nodeDb;

    private String url="https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Load layout XML

        // Initialize views
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginButton);
        btnRegister = findViewById(R.id.registerButton);

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance(url);
        nodeDb = database.getReference("users");

        // Set click listener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Handles user login and authentication.
     */
    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase query to authenticate user
        nodeDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAuthenticated = false;

                // Loop through users node to find matching username and password
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String dbUsername = userSnapshot.child("username").getValue(String.class);
                    String dbPassword = userSnapshot.child("password").getValue(String.class);
                    String dbNamaLengkap = userSnapshot.child("nama_lengkap").getValue(String.class);
                    String dbid = userSnapshot.child("id").getValue(String.class);
                    String dbalamatLengkap = userSnapshot.child("alamat_lengkap").getValue(String.class);
                    String dbnomorhp = userSnapshot.child("nomor_hp").getValue(String.class);
                    String dbusiakehamilan = userSnapshot.child("usia_kehamilan").getValue(String.class);
                    String dbtangglahir = userSnapshot.child("tanggal_lahir").getValue(String.class);
                    String UserBucket = userSnapshot.getKey();//getThebucket of users
                    if (dbUsername != null && dbPassword != null &&
                            dbUsername.equals(username) && dbPassword.equals(password)) {
                        isAuthenticated = true;

                        // Jika autentikasi berhasil
                        if (username.equals("admin")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("currentNama", dbNamaLengkap);  // username yang didapat saat login
                            editor.apply();  // Menyimpan perubahan
                            Intent intent = new Intent(LoginActivity.this, MonitorUsersActivity.class);
                            startActivity(intent);

                        } else {
                            // Menyimpan username saat login
                            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("BucketUser",UserBucket);//bucket dari user instead of id
                            editor.putString("currentUser", dbid);  // username yang didapat saat login
                            editor.putString("currentNama", dbNamaLengkap);  // username yang didapat saat login
                            editor.putString("currentAlamat", dbalamatLengkap);  // username yang didapat saat login
                            editor.putString("currentNomor", dbnomorhp);  // username yang didapat saat login
                            editor.putString("currentUsiaKehamilan", dbusiakehamilan);  // username yang didapat saat login
                            editor.putString("currentTanggal", dbtangglahir);  // username yang didapat saat login
                            editor.putString("currentUsername", username);  // username yang didapat saat login
                            editor.apply();  // Menyimpan perubahan
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                        }
                        finish(); // Close the LoginActivity
                        break;
                    }
                }

                if (!isAuthenticated) {
                    // Jika autentikasi gagal
                    Toast.makeText(LoginActivity.this, "Username atau Password salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Jika terjadi error saat mengakses database
                Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Firebase error: " + databaseError.getMessage());
            }
        });
    }
}
