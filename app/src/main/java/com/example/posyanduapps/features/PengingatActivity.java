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

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.R;
import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.adapters.ReminderAdapter;

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
        currentOption = sharedPreferences.getInt("currentOption",-1);
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
        loadRemindersFromDatabase();

    }

    private void loadRemindersFromDatabase() {
        // Pastikan reminderList sudah terinisialisasi
        if (reminderList == null) {
            reminderList = new ArrayList<>();  // Inisialisasi jika belum ada
        }

        // Hapus data yang ada sebelumnya
        reminderList.clear();

        // Pastikan dbHelper telah terinisialisasi dengan benar
        if (dbHelper != null) {
            Cursor cursor = dbHelper.getAllAbsensi();  // Mengambil data dari database

            // Periksa apakah cursor valid dan ada data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_NAME));
                    String tanggal = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_TANGGAL));
                    String hari = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_HARI));
                    String jam = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_JAM));
                    String tempat = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_TEMPAT));

                    jam = jam.replace(":", ".");
                    String reminderData = String.format("%s;%s;%s;%s;%s", name, tanggal, hari, tempat, jam);
                    reminderList.add(reminderData);

                } while (cursor.moveToNext());
            } else {
                // Menangani jika cursor tidak berisi data
                Log.d("LoadReminders", "Tidak ada data dalam database.");
                tvidentifierdb.setVisibility(View.VISIBLE);
                // Opsional: Tampilkan pesan kepada pengguna jika perlu
                // Toast.makeText(this, "Tidak ada pengingat", Toast.LENGTH_SHORT).show();
            }

            // Menutup cursor setelah digunakan
            if (cursor != null) {
                cursor.close();
            }
        } else {
            // Menangani jika dbHelper belum terinisialisasi
            Log.e("LoadReminders", "dbHelper belum terinisialisasi.");
            tvidentifierdb.setVisibility(View.VISIBLE);
            tvidentifierdb.setText("Database tidak tersedia");
            // Opsional: Tampilkan pesan error kepada pengguna
            // Toast.makeText(this, "Database tidak tersedia", Toast.LENGTH_SHORT).show();
        }

        // Update tampilan dengan adapter setelah data dimuat
        reminderAdapter.notifyDataSetChanged();
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
