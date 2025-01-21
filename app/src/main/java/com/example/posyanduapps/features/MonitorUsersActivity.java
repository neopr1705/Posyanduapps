package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.LoginActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.UserAdapter;
import com.example.posyanduapps.models.User;
import com.example.posyanduapps.utils.FirebaseManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class MonitorUsersActivity extends Activity  implements View.OnClickListener{
    private DatabaseReference databaseReference;
    private ListView userListView;
    private UserAdapter userAdapter;
    private TextView tvTitle;
    private ImageView ivLogout, ivHome, ivReminder, ivProfile, ivSettings;
    private ArrayList<User> userList = new ArrayList<>();
    private String url="https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_user);
        SharedPreferences sharedPreferences = getSharedPreferences("Option", MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentOption", 4);  // username yang didapat saat login
        editor.apply();  // Menyimpan perubahan
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("MONITOR USER (Admin)");
        ivLogout = findViewById(R.id.ivLogout);

        initializeview();



        userListView = findViewById(R.id.userListView);
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                // Ambil data pengguna yang dipilih berdasarkan posisi
                User selectedUser = userList.get(position);

                // Ambil ID pengguna yang dipilih
                String userId = selectedUser.getId();

                // Arahkan ke DetailUserActivity dan kirimkan ID pengguna
                Intent intent = new Intent(MonitorUsersActivity.this, DetailUserActivity.class);
                intent.putExtra("userId", userId);  // Mengirimkan ID user
                startActivity(intent);
            }
        });

        ivLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MonitorUsersActivity.this, LoginActivity.class);
            Toast.makeText(this, "Logout Berhasil!", Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        });


        loadUsers();
    }

    public void initializeview(){
        ImageView choice = findViewById(R.id.ivChoice);
        choice.setVisibility(View.GONE);
        TextView tvChoice = findViewById(R.id.tvchoice);
        tvChoice.setVisibility(View.GONE);
        ivHome = findViewById(R.id.ivHome);
        ivHome.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
        ivReminder = findViewById(R.id.ivReminder);
        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setImageResource(R.drawable.baseline_assignment_add_24);
        ivSettings = findViewById(R.id.ivSettings);
        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);

    }

    private void loadUsers() {
        FirebaseManager.readData("users", new FirebaseManager.FirebaseCallback<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                userAdapter = new UserAdapter(MonitorUsersActivity.this, userList);
                userListView.setAdapter(userAdapter);
            }

            @Override
            public void onError(String error) {
                Log.e("MonitorUsersActivity", "Error loading users: " + error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == ivHome.getId()){
            intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        else if (v.getId() == ivReminder.getId()) {
            intent = new Intent(this, PengingatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        else if (v.getId() == ivSettings.getId()) {
            intent = new Intent(this, DataIbuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        else if (v.getId() == ivProfile.getId()) {
            intent = new Intent(this, AbsensiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }
}
