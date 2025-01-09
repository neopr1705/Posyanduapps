package com.example.posyanduapps.features;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.MainActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.adapters.AbsensiAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AbsensiActivity extends Activity implements View.OnClickListener {

    private EditText edtNama, edtTempat;
    private Button btnHadir;
    private DatabaseHelper databaseHelper;
    private ListView lvAbsensi;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> absensiList;
    private ImageView ivHome, ivReminder, ivAddAbsensi, ivProfile, ivSettings;
    private TextView tvTanggal, tvJam, tvHari,tvTitle;
    private String selectedTanggal, selectedHari, selectedJam;
    private AbsensiAdapter customAdapter;
    private ExecutorService executorService; // ExecutorService untuk tugas latar belakang
    private Handler mainHandler; // Handler untuk kembali ke thread utama
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getText(R.string.str_AbsensiKehadiran));
        //footer
        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);
        ivAddAbsensi = findViewById(R.id.ivAddAbsensi);
        ivProfile = findViewById(R.id.ivProfile);
        ivSettings = findViewById(R.id.ivSettings);

        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivAddAbsensi.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
        
        ImageView ivHome = findViewById(R.id.ivAddAbsensi);
        ivHome.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
        // Inisialisasi komponen
        edtNama = findViewById(R.id.edtNama);
        edtTempat = findViewById(R.id.edtTempat);
        btnHadir = findViewById(R.id.btnHadir);
        lvAbsensi = findViewById(R.id.lvAbsensi);

        tvTanggal = findViewById(R.id.tvTanggal);
        tvHari = findViewById(R.id.tvHari);
        tvJam = findViewById(R.id.tvJam);

        // Inisialisasi ExecutorService dan Handler
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        databaseHelper = new DatabaseHelper(this);
        absensiList = new ArrayList<>();

        // Ganti ArrayAdapter dengan AbsensiAdapter
        customAdapter = new AbsensiAdapter(this, absensiList);
        lvAbsensi.setAdapter(customAdapter);

        btnHadir.setOnClickListener(v -> {
            tambahHadir();
        });

        tvTanggal.setOnClickListener(v -> showDatePickerDialog());
        tvJam.setOnClickListener(v -> showTimePickerDialog());

        // Panggil loadAbsensiData() di sini untuk memuat data absensi dari database
        loadAbsensiData();
    }

    private void tambahHadir() {
        String nama = edtNama.getText().toString();
        String tempat = edtTempat.getText().toString();

        if (!nama.isEmpty() && !selectedTanggal.isEmpty() && !selectedHari.isEmpty() && !selectedJam.isEmpty() && !tempat.isEmpty()) {
            executorService.submit(() -> {
                // Pengecekan database untuk tanggal dan jam yang sama
                boolean isDuplicate = databaseHelper.checkDuplicateAbsensi(selectedTanggal, selectedJam,tempat);

                mainHandler.post(() -> {
                    if (isDuplicate) {
                        Toast.makeText(AbsensiActivity.this, "Jadwal tidak tersedia", Toast.LENGTH_SHORT).show();
                    } else {
                        // Jika tidak ada duplikasi, masukkan data ke database
                        boolean isInserted = databaseHelper.insertAbsensi(nama, selectedTanggal, selectedHari, selectedJam, tempat);
                        if (isInserted) {
                            // Masukkan data alarm ke database
                            boolean isAlarmInserted = databaseHelper.insertAlarm(selectedTanggal, selectedJam,1);
                            if (isAlarmInserted) {
                                // Set Alarm
                                setAlarm(selectedTanggal, selectedJam);
                            }
                            String absensi =
                                    "Nama: "+nama+ "\n"
                                            + "Tanggal: "+selectedTanggal+ "\n"
                                            + "Hari: "+selectedHari + "\n"
                                            + "Jam: "+selectedJam + "\n"
                                            + "Tempat: "+tempat;
                            absensiList.add(absensi);
                            customAdapter.notifyDataSetChanged(); // Meng-update UI setelah menambah data
                            Toast.makeText(AbsensiActivity.this, "Absensi berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AbsensiActivity.this, "Gagal menambahkan absensi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            });
        } else {
            Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show();
        }
        edtNama.setText("");
        edtTempat.setText("");
        tvTanggal.setText("");
        tvHari.setText("");
        tvJam.setText("");
    }

    private void loadAbsensiData() {
        executorService.submit(() -> {
            Cursor cursor = databaseHelper.getAllAbsensi();
            mainHandler.post(() -> {
                absensiList.clear();
                if (cursor.moveToFirst()) {
                    do {
                        String absensi =
                                "Nama: "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_NAME)) + "\n"
                                        + "Tanggal: "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_TANGGAL)) + "\n"
                                        + "Hari: "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_HARI)) + "\n"
                                        + "Jam: "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_JAM)) + "\n"
                                        + "Tempat: "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_TEMPAT));
                        absensiList.add(absensi);
                    } while (cursor.moveToNext());
                }
                customAdapter.notifyDataSetChanged();
                cursor.close();
            });
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.CustomDatePicker,
                (view, year, month, dayOfMonth) -> {
                    // Perbarui kalender dengan tanggal yang dipilih
                    calendar.set(year, month, dayOfMonth);
                    selectedTanggal = dayOfMonth + "/" + (month + 1) + "/" + year;
                    tvTanggal.setText(selectedTanggal);

                    // Tentukan hari berdasarkan tanggal yang sudah diperbarui
                    String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
                    selectedHari = dayOfWeek; // Simpan hari yang dipilih
                    tvHari.setText(dayOfWeek);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Fungsi untuk mendapatkan nama hari dari nilai DAY_OF_WEEK
    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Minggu";
            case Calendar.MONDAY:
                return "Senin";
            case Calendar.TUESDAY:
                return "Selasa";
            case Calendar.WEDNESDAY:
                return "Rabu";
            case Calendar.THURSDAY:
                return "Kamis";
            case Calendar.FRIDAY:
                return "Jumat";
            case Calendar.SATURDAY:
                return "Sabtu";
            default:
                return ""; // Default jika tidak valid
        }
    }
    private void showTimePickerDialog() {
        // Dialog untuk memilih jam
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                R.style.CustomDatePicker,
                (view, hourOfDay, minute) -> {
                    selectedJam = hourOfDay + ":" + minute;
                    tvJam.setText(selectedJam);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }
    private void setAlarm(String tanggal, String jam) {
        // Mengonversi jam yang dipilih menjadi waktu dalam milliseconds
        String[] timeParts = jam.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Menggunakan AlarmManager untuk menjadwalkan alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AbsensiActivity.this, AlarmReceiver.class); // Anda perlu membuat AlarmReceiver untuk menampilkan notifikasi
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AbsensiActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set alarm
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(AbsensiActivity.this, "Alarm telah disetel untuk " + jam, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        // Dapatkan aksi klik jika diperlukan
        if(v.getId() == ivHome.getId()){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (v.getId() == ivAddAbsensi.getId()) {
            intent = new Intent(this, AbsensiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (v.getId() == ivReminder.getId()) {
            intent = new Intent(this, PengingatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (v.getId() == ivSettings.getId()) {
            intent = new Intent(this, DataIbuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if (v.getId() == ivProfile.getId()) {
            intent = new Intent(this, EdukasiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}
