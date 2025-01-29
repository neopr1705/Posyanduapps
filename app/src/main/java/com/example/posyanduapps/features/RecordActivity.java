package com.example.posyanduapps.features;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.R;
import com.example.posyanduapps.RegisterAdminActivity;
import com.example.posyanduapps.adapters.KesehatanAdapter;
import com.example.posyanduapps.models.Kesehatan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordActivity extends Activity  implements View.OnClickListener {

    private Spinner userDropdown;
    private TextView userId, userName, userUsername, userPhone, userAddress, userBirthDate, userPregnancyAge;
    private TextView Kategori;
    private Button editButton, deleteButton;

    private Button bayiButton;
    private Button lansiaButton;
    private Button bumilButton;
    private Button deleteUser;
    private ImageView ivLogout, ivHome, ivReminder, ivProfile, ivSettings, ivChart;
    private DatabaseReference databaseReference;
    private Map<String, Map<String, String>> userDataMap = new HashMap<>();
    private ArrayList<String> userIds = new ArrayList<>();
    private ArrayList<String> spinnerData = new ArrayList<>();
    private String selectedUserId;

    String url = "https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private int option;
    private TextView tvTitle;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initializeview();
        SharedPreferences sharedPreferences = getSharedPreferences("Option", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentOption", 4);  // username yang didapat saat login
        editor.apply();  // Menyimpan perubahan
        option = sharedPreferences.getInt("currentOption", 0);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("MONITOR USER (Admin)");
        ivLogout = findViewById(R.id.ivLogout);
        // Initialize views
        Kategori = findViewById(R.id.tvKategori);
        bayiButton = findViewById(R.id.bayiButton);
        lansiaButton = findViewById(R.id.lansiaButton);
        bumilButton = findViewById(R.id.bumilButton);
        userDropdown = findViewById(R.id.userDropdown);
        userId = findViewById(R.id.userId);
        userName = findViewById(R.id.userName);
        userUsername = findViewById(R.id.userUsername);
        userPhone = findViewById(R.id.userPhone);
        userAddress = findViewById(R.id.userAddress);
        userBirthDate = findViewById(R.id.userBirthDate);
        userPregnancyAge = findViewById(R.id.userPregnancyAge);
        ivChart = findViewById(R.id.ivChart);
        ivChart.setImageResource(R.drawable.superadmin);
        deleteUser = findViewById(R.id.adminDeleteUser);
        new HeaderIconHelper(this, findViewById(R.id.header_layout));
        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance(url).getReference("users");
        // Load users from Firebase
        loadUsers();

        // Set up dropdown item selection
        userDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClearListViewData();
                selectedUserId = userIds.get(position);
                displayUserData(selectedUserId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action required
            }
        });
        button_listener();
    }

    private void button_listener() {
        // Set listener untuk tombol bayi
        bayiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadDataBayi(userId.getText().toString());

            }
        });

        // Set listener untuk tombol lansia
        lansiaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadDataLansia(userId.getText().toString());
            }
        });

        // Set listener untuk tombol ibu hamil
        bumilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadDataIbuHamil(userId.getText().toString());
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(userDropdown.getSelectedItem().toString());
            }
        });
    }

    private void loadUsers() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    userDataMap.clear();
                    userIds.clear();

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String id = userSnapshot.getKey();
                        String nama = userSnapshot.child("nama_lengkap").getValue(String.class);
                        String roles = userSnapshot.child("roles").getValue(String.class);
                        if (roles != null) {
                            if (!roles.equalsIgnoreCase("admin")) {
                                String dataList = id + " - " + nama;
                                Map<String, String> userData = (Map<String, String>) userSnapshot.getValue();
                                userIds.add(id);
                                spinnerData.add(dataList);
                                userDataMap.put(id, userData);
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    userDropdown.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "No users found in database.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to load users: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataBayi(String userId) {
        Log.d("debug_old", userId);
        String[] parts = userId.split(": ");
        String uid = parts[1].trim();
        Log.d("debug_new", uid);  // Output: 3
        // Arahkan ke path bayi di Firebase
        databaseReference = FirebaseDatabase.getInstance(url).getReference("users").child(uid).child("data_user").child("bayi");
        Log.d("debug_path", "Database path: " + databaseReference.toString());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    Map<String, Object> kesehatanData = (Map<String, Object>) dataSnapshot.getValue();
                    List<Kesehatan> kesehatanList = new ArrayList<>();

                    for (Map.Entry<String, Object> entry : kesehatanData.entrySet()) {
                        Map<String, String> detail = (Map<String, String>) entry.getValue();

                        String nama = detail.get("nama");
                        String beratBadan = detail.get("berat_badan");
                        String tinggiBadan = detail.get("tinggi_badan");
                        String statusVaksin = detail.get("status_obat_vaksin");
                        String riwayatPenyakit = detail.get("riwayat_penyakit");
                        String lingkarKepala = detail.get("lingkar_kepala");
                        String lingkarLengan = detail.get("lingkar_lengan");
                        String lingkarperut = detail.get("lingkar_perut");
                        String tanggal = detail.get("tanggal");
                        String jam = detail.get("jam");

                        Kesehatan kesehatan = new Kesehatan(nama, beratBadan, tinggiBadan, statusVaksin, riwayatPenyakit, lingkarKepala, lingkarLengan, lingkarperut, tanggal, jam);
                        kesehatanList.add(kesehatan);
                    }

                    // Menetapkan adapter untuk ListView
                    KesehatanAdapter adapter = new KesehatanAdapter(getApplicationContext(), kesehatanList);
                    ListView listView = findViewById(R.id.listViewKesehatan);
                    listView.setAdapter(adapter);
                    Kategori.setText("Detail User - Bayi");
                } else {
                    // Jika data bayi kosong, tampilkan Toast
                    Toast.makeText(getApplicationContext(), "Belum ada data bayi", Toast.LENGTH_SHORT).show();
                    Log.d("Debug", "Belum ada data bayi di Firebase");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tangani kesalahan
                Log.e("debug", "Error loading bayi data", databaseError.toException());
            }
        });
    }


    private void loadDataLansia(String userId) {
        Log.d("debug_old", userId);
        String[] parts = userId.split(": ");
        String uid = parts[1].trim();
        Log.d("debug_new", uid);  // Output: 3
        // Arahkan ke path lansia di Firebase
        databaseReference = FirebaseDatabase.getInstance(url).getReference("users").child(uid).child("data_user").child("lansia");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    Map<String, Object> kesehatanData = (Map<String, Object>) dataSnapshot.getValue();
                    List<Kesehatan> kesehatanList = new ArrayList<>();

                    for (Map.Entry<String, Object> entry : kesehatanData.entrySet()) {
                        Map<String, String> detail = (Map<String, String>) entry.getValue();

                        String nama = detail.get("nama");
                        String beratBadan = detail.get("berat_badan");
                        String tinggiBadan = detail.get("tinggi_badan");
                        String statusVaksin = detail.get("status_obat_vaksin");
                        String riwayatPenyakit = detail.get("riwayat_penyakit");
                        String lingkarKepala = detail.get("lingkar_kepala");
                        String lingkarLengan = detail.get("lingkar_lengan");
                        String lingkarperut = detail.get("lingkar_perut");
                        String tanggal = detail.get("tanggal");
                        String jam = detail.get("jam");

                        Kesehatan kesehatan = new Kesehatan(nama, beratBadan, tinggiBadan, statusVaksin, riwayatPenyakit, lingkarKepala, lingkarLengan, lingkarperut, tanggal, jam);
                        kesehatanList.add(kesehatan);
                    }

                    // Menetapkan adapter untuk ListView
                    KesehatanAdapter adapter = new KesehatanAdapter(getApplicationContext(), kesehatanList);
                    ListView listView = findViewById(R.id.listViewKesehatan);
                    listView.setAdapter(adapter);
                    Kategori.setText("Detail User - Lansia");
                } else {
                    // Jika data bayi kosong, tampilkan Toast
                    Toast.makeText(getApplicationContext(), "Belum ada data lansia", Toast.LENGTH_SHORT).show();
                    Log.d("Debug", "Belum ada data bayi di Firebase");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tangani kesalahan
                Log.e("debug", "Error loading lansia data", databaseError.toException());
            }
        });
    }

    public void initializeview() {
        ImageView choice = findViewById(R.id.ivChoice);
        choice.setVisibility(View.GONE);
        TextView tvChoice = findViewById(R.id.tvchoice);
        tvChoice.setVisibility(View.GONE);
        ivChart = findViewById(R.id.ivChart);
        ivChart.setImageResource(R.drawable.superadmin);
        ivHome = findViewById(R.id.ivHome);
        ivHome.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
        ivReminder = findViewById(R.id.ivReminder);
        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setImageResource(R.drawable.baseline_assignment_add_24);
        ivSettings = findViewById(R.id.ivSettings);
        ivSettings.setVisibility(View.GONE);
        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
        ivChart.setOnClickListener(this);

    }


    private void loadDataIbuHamil(String userId) {
        Log.d("debug_old", userId);
        String[] parts = userId.split(": ");
        String uid = parts[1].trim();
        Log.d("debug_new", uid);  // Output: 3
        // Arahkan ke path ibu hamil di Firebase
        databaseReference = FirebaseDatabase.getInstance(url).getReference("users").child(uid).child("data_user").child("bumil");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    Map<String, Object> kesehatanData = (Map<String, Object>) dataSnapshot.getValue();
                    List<Kesehatan> kesehatanList = new ArrayList<>();

                    for (Map.Entry<String, Object> entry : kesehatanData.entrySet()) {
                        Map<String, String> detail = (Map<String, String>) entry.getValue();

                        String nama = detail.get("nama");
                        String beratBadan = detail.get("berat_badan");
                        String tinggiBadan = detail.get("tinggi_badan");
                        String statusVaksin = detail.get("status_obat_vaksin");
                        String riwayatPenyakit = detail.get("riwayat_penyakit");
                        String lingkarKepala = detail.get("lingkar_kepala");
                        String lingkarLengan = detail.get("lingkar_lengan");
                        String lingkarperut = detail.get("lingkar_perut");
                        String tanggal = detail.get("tanggal");
                        String jam = detail.get("jam");

                        Kesehatan kesehatan = new Kesehatan(nama, beratBadan, tinggiBadan, statusVaksin, riwayatPenyakit, lingkarKepala, lingkarLengan, lingkarperut, tanggal, jam);
                        kesehatanList.add(kesehatan);
                    }

                    // Menetapkan adapter untuk ListView
                    KesehatanAdapter adapter = new KesehatanAdapter(getApplicationContext(), kesehatanList);
                    ListView listView = findViewById(R.id.listViewKesehatan);
                    listView.setAdapter(adapter);
                    Kategori.setText("Detail User - Bumil");
                } else {
                    // Jika data bayi kosong, tampilkan Toast
                    Toast.makeText(getApplicationContext(), "Belum ada data ibu hamil", Toast.LENGTH_SHORT).show();
                    Log.d("Debug", "Belum ada data bayi di Firebase");
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tangani kesalahan
                Log.e("debug", "Error loading ibu hamil data", databaseError.toException());
            }
        });
    }


    private void displayUserData(String userId) {
        Map<String, String> userData = userDataMap.get(userId);
        if (userData != null) {
            this.userId.setText("ID: " + userData.getOrDefault("id", "-"));
            userName.setText("Nama Lengkap: " + userData.getOrDefault("nama_lengkap", "-"));
            userUsername.setText("Username: " + userData.getOrDefault("username", "-"));
            userPhone.setText("Nomor HP: " + userData.getOrDefault("nomor_hp", "-"));
            userAddress.setText("Alamat Lengkap: " + userData.getOrDefault("alamat_lengkap", "-"));
            userBirthDate.setText("Tanggal Lahir: " + userData.getOrDefault("tanggal_lahir", "-"));
            userPregnancyAge.setText("Usia Kehamilan: " + userData.getOrDefault("usia_kehamilan", "-"));
            this.userId.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);
            userUsername.setVisibility(View.GONE);
            userPhone.setVisibility(View.GONE);
            userAddress.setVisibility(View.GONE);
            userBirthDate.setVisibility(View.GONE);
            userPregnancyAge.setVisibility(View.GONE);

        }
    }

    private void ClearListViewData() {
        ListView listView = findViewById(R.id.listViewKesehatan);
        listView.setAdapter(null);

    }

    private void showDeleteConfirmationDialog(String userId) {
        String [] splituid = userId.split(" - ");
        new AlertDialog.Builder(this, R.style.AppTemaDialog)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user " +userId+" ?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    // If the user confirms, proceed to delete the user
                    deleteUser(splituid[0]);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // If the user cancels, dismiss the dialog
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void deleteUser(String userId) {
        databaseReference.child(userId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "User deleted successfully.", Toast.LENGTH_SHORT).show();
                loadUsers(); // Refresh user list
                Intent intent = new Intent(this, MonitorUsersActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to delete user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ivHome.getId()) {

        } else if (v.getId() == ivReminder.getId()) {
            intent = new Intent(this, PengingatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v.getId() == ivSettings.getId()) {
            intent = new Intent(this, DataIbuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (v.getId() == ivProfile.getId()) {

            intent = new Intent(this, AbsensiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();

        } else if (v.getId() == ivChart.getId()) {
            intent = new Intent(this, RegisterAdminActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }
}