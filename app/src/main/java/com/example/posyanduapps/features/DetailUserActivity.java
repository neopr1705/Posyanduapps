package com.example.posyanduapps.features;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.posyanduapps.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DetailUserActivity extends Activity {
    private TextView tvUserDetails;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        tvUserDetails = findViewById(R.id.tvUserDetails);

        // Mengambil ID pengguna yang dipilih
        userId = getIntent().getStringExtra("userId");

        // Ambil data pengguna dari Firebase
        if (userId != null) {
            fetchUserDataFromFirebase(userId);
        }
    }

    private void fetchUserDataFromFirebase(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("data_user");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Ambil seluruh data yang ada di data_user
                    StringBuilder userDetails = new StringBuilder();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        userDetails.append(childSnapshot.getKey()).append(": ").append(childSnapshot.getValue()).append("\n");
                    }
                    tvUserDetails.setText(userDetails.toString());
                } else {
                    tvUserDetails.setText("Data tidak ditemukan");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tvUserDetails.setText("Terjadi kesalahan saat mengambil data");
            }
        });
    }
}
