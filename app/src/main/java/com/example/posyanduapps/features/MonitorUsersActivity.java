package com.example.posyanduapps.features;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.posyanduapps.LoginActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.adapters.UserAdapter;
import android.app.Activity;
import android.widget.TextView;

public class MonitorUsersActivity extends Activity {
    private DatabaseHelper databaseHelper;
    private ListView userListView;
    private UserAdapter userAdapter;
    private TextView tvTitle;
    private Button btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_user);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("MONITOR USER");

        databaseHelper = new DatabaseHelper(this);
        userListView = findViewById(R.id.userListView);
        btnKembali = findViewById(R.id.btnKembali);

        btnKembali.setOnClickListener(v -> {
            Intent intent = new Intent(MonitorUsersActivity.this,  LoginActivity.class);
            startActivity(intent);
            finish();
        });

        loadUsers();
    }

    private void loadUsers() {
        Cursor cursor = databaseHelper.getAllUsers();
        userAdapter = new UserAdapter(this, cursor);
        userListView.setAdapter(userAdapter);
    }
}
