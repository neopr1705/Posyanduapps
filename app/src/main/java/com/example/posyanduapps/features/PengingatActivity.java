package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.R;
import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.adapters.ReminderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PengingatActivity extends Activity implements View.OnClickListener {

    private Button btnSetReminder;
    private ListView lvReminders;
    private TextView tvTitle,tvidentifierdb;

    private DatabaseHelper dbHelper;
    private ArrayList<String> reminderList;
    private ReminderAdapter reminderAdapter;

    private ImageView ivHome, ivReminder, ivProfile, ivSettings;
    private int currentOption;
    Switch swAktif;
    private String url="https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengingat);
        initializeUI();
        initializeDatabase();
    }

    private void initializeUI() {
        // Initialize header
        View headerLayout = findViewById(R.id.header_layout);
        new HeaderIconHelper(this, headerLayout);

        // Set title
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getText(R.string.str_Pengingat));
        tvidentifierdb = findViewById(R.id.tvidentifierDb);
        tvidentifierdb.setVisibility(View.GONE);

        // Initialize footer buttons
        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);
        ivProfile = findViewById(R.id.ivProfile);
        ivSettings = findViewById(R.id.ivSettings);

        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);

        // Set the color for the reminder icon
        ivReminder.setColorFilter(getResources().getColor(R.color.softBlue));

        // Initialize ListView
        lvReminders = findViewById(R.id.lvReminders);
        swAktif = findViewById(R.id.swAktif);
        SharedPreferences sharedPreferences = getSharedPreferences("Option", MODE_PRIVATE);
        currentOption = sharedPreferences.getInt("currentOption",4);
        if (currentOption ==3) {
            tvTitle.setText("Pengingat Jadwal Ibu Hamil");
            lvReminders.setVisibility(View.GONE);
            tvidentifierdb.setVisibility(View.VISIBLE);
        }else if(currentOption ==1){
            tvTitle.setText("Pengingat Jadwal Balita");
            lvReminders.setVisibility(View.GONE);
            tvidentifierdb.setVisibility(View.VISIBLE);

        }else if(currentOption ==2){
            tvTitle.setText("Pengingat Jadwal Lansia");
            lvReminders.setVisibility(View.GONE);
            tvidentifierdb.setVisibility(View.VISIBLE);

        }else{
            tvTitle.setText("List Jadwal Pengguna");
            tvidentifierdb.setVisibility(View.GONE);
            ImageView ivchoice = findViewById(R.id.ivChoice);
            ivchoice.setVisibility(View.GONE);
            TextView tvchoice = findViewById(R.id.tvchoice);
            tvchoice.setVisibility(View.GONE);

            ivProfile.setImageResource(R.drawable.baseline_assignment_add_24);
            ivSettings.setVisibility(View.GONE);


        }

        setChoiceImage();

    }

    private void initializeDatabase() {
        dbHelper = new DatabaseHelper(this);

        reminderList = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(this, reminderList);
        lvReminders.setAdapter(reminderAdapter);
        loadRemindersFromFirebase();

    }

    private void loadRemindersFromFirebase() {
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);

        // Pastikan reminderList sudah terinisialisasi
        if (reminderList == null) {
            reminderList = new ArrayList<>();
        }

        // Hapus data yang ada sebelumnya
        reminderList.clear();

        // Referensi ke Firebase Realtime Database
        DatabaseReference absensiRef = FirebaseDatabase.getInstance(url).getReference("absensi");
        DatabaseReference usersRef = FirebaseDatabase.getInstance(url).getReference("users");

        // Dapatkan ID pengguna saat ini (misalnya dari sesi login)
        String currentUserId = sharedPreferences.getString("currentUser","");; // Sesuaikan implementasi
        if (currentUserId == null) {
            Log.e("LoadReminders", "Pengguna belum login.");
            return;
        }

        // Ambil data absensi dari Firebase
        absensiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot absensiSnapshot : dataSnapshot.getChildren()) {
                        String assignedTo = absensiSnapshot.child("assigned_to").getValue(String.class);
                        String name = absensiSnapshot.child("nama").getValue(String.class);
                        String tanggal = absensiSnapshot.child("tanggal").getValue(String.class);
                        String hari = absensiSnapshot.child("hari").getValue(String.class);
                        String jam = absensiSnapshot.child("jam").getValue(String.class);
                        String tempat = absensiSnapshot.child("tempat").getValue(String.class);

                        // Logika filter berdasarkan `currentOption`
                        boolean isValidReminder = false;
                        if (currentOption >= 1 && currentOption <= 3) {
                            isValidReminder = currentUserId.equals(assignedTo);
                        } else {
                            isValidReminder = true; // Tampilkan semua jika currentOption selain 1-3
                        }

                        if (isValidReminder) {
                            // Format jam untuk pengingat
                            if (jam != null) {
                                jam = jam.replace(":", ".");
                            }

                            // Gabungkan data pengingat
                            String reminderData = String.format("%s;%s;%s;%s;%s", name, tanggal, hari, tempat, jam);
                            reminderList.add(reminderData);
                        }
                    }

                    // Update tampilan setelah data berhasil dimuat
                    reminderAdapter.notifyDataSetChanged();

                    // Sembunyikan indikator jika data ada
                    tvidentifierdb.setVisibility(View.GONE);
                } else {
                    // Jika tidak ada data
                    Log.d("LoadReminders", "Tidak ada data absensi.");
                    tvidentifierdb.setVisibility(View.VISIBLE);
                    tvidentifierdb.setText("Tidak ada pengingat");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LoadReminders", "Gagal memuat data: " + databaseError.getMessage());
                tvidentifierdb.setVisibility(View.VISIBLE);
                tvidentifierdb.setText("Gagal memuat data");
            }
        });
    }


    private void setChoiceImage(){
        ImageView ivChoice = findViewById(R.id.ivChoice);
        if(currentOption == 1){
            ivChoice.setImageResource(R.drawable.baby);
        }else if(currentOption == 2){
            ivChoice.setImageResource(R.drawable.elder);
        } else if (currentOption==3) {
            ivChoice.setImageResource(R.drawable.ic_mom);
        }else{
            ivChoice.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        // Menggunakan Intent untuk membuka aktivitas yang sesuai dengan tombol yang diklik
        Intent intent;

        if(v.getId() == ivHome.getId()){
            if(currentOption==4){
                intent = new Intent(this, MonitorUsersActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }else{
            intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            }
        }
        else if (v.getId() == ivSettings.getId()) {
            intent = new Intent(this, DataIbuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        else if (v.getId() == ivProfile.getId()) {
            if(currentOption==4){
                intent = new Intent(this, AbsensiActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }else {
                intent = new Intent(this, EdukasiBumilActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }
    }
}
