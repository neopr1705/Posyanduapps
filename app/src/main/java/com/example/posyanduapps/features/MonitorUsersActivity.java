package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.posyanduapps.LoginActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.UserAdapter;
import com.example.posyanduapps.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class MonitorUsersActivity extends Activity {
    private DatabaseReference databaseReference;
    private ListView userListView;
    private UserAdapter userAdapter;
    private TextView tvTitle;
    private Button btnKembali;
    private ArrayList<User> userList = new ArrayList<>();
    private String url="https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_user);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("MONITOR USER");

        userListView = findViewById(R.id.userListView);
        btnKembali = findViewById(R.id.btnKembali);

        btnKembali.setOnClickListener(v -> {
            Intent intent = new Intent(MonitorUsersActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Inisialisasi Firebase Database
        databaseReference = FirebaseDatabase.getInstance(url).getReference("users");

        loadUsers();
    }

    private void loadUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            userList.add(user);
                        } else {
                            Log.e("MonitorUsersActivity", "Invalid user data: " + snapshot.toString());
                        }
                    } catch (Exception e) {
                        Log.e("MonitorUsersActivity", "Error parsing user: " + e.getMessage());
                    }
                }
                userAdapter = new UserAdapter(MonitorUsersActivity.this, userList);
                userListView.setAdapter(userAdapter);            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}
