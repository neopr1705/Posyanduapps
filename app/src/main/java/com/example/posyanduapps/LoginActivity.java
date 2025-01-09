package com.example.posyanduapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.features.MonitorUsersActivity;

public class LoginActivity extends Activity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Load layout XML

        // Initialize views
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginButton);
        dbHelper = new DatabaseHelper(this); // Database helper instance
        btnRegister = findViewById(R.id.registerButton);
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

    private void registerUser(){
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

        // Call the authenticateUser method from DatabaseHelper
        if (username.equalsIgnoreCase("admin")&&password.equalsIgnoreCase("admin")) {
            // Jika autentikasi berhasil
            Intent intent = new Intent(LoginActivity.this, MonitorUsersActivity.class);
            startActivity(intent);
            finish(); // Close the LoginActivity
        } else if (dbHelper.authenticateUser(username, password)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the LoginActivity

        } else {
            // Jika autentikasi gagal
            Toast.makeText(LoginActivity.this, "Username dan Password salah", Toast.LENGTH_SHORT).show();
        }
        {
//                   // Log username and password input
//            Log.d("LoginDebug", "Input Username: " + username);
//            Log.d("LoginDebug", "Input Password: " + password);
//
//            // Query the database to check if the username exists
//            Log.d("DatabaseQuery", "Querying database for user: " + username);
//
//            databaseReference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.d("DatabaseQuery", "Query Success! DataSnapshot: " + dataSnapshot.getValue());
//
//                    // Check if username exists
//                    if (dataSnapshot.exists()) {
//                        Log.d("DatabaseQuery", "User exists: " + username);
//
//                        // Get stored password
//                        String storedPassword = dataSnapshot.child("password").getValue(String.class);
//                        Log.d("DatabaseQuery", "Stored Password: " + storedPassword);
//
//                        // Validate password
//                        if (storedPassword != null && storedPassword.equals(password)) {
//                            Log.d("DatabaseQuery", "Login Successful");
//                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            finish(); // Close LoginActivity
//                        } else {
//                            Log.d("DatabaseQuery", "Invalid Password");
//                            Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Log.d("DatabaseQuery", "Username not found");
//                        Toast.makeText(LoginActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.d("DatabaseQuery", "Query Failed: " + databaseError.getMessage());
//                }
//            });
//
//
//            Log.d("LoginDebug", "end of validateLogin");

        }

    }



}
