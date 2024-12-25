package com.example.posyanduapps.features;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.MainActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.Helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class PengingatActivity extends Activity implements View.OnClickListener {

    private Button btnSetReminder;
    private TextView tvSelectedDate;
    private ListView lvReminders;
    private DatabaseHelper dbHelper;
    private ArrayList<String> reminderList;
    private ReminderAdapter reminderAdapter;  // Ganti dengan custom adapter

    private ImageView ivHome, ivReminder, ivAddAbsensi, ivProfile, ivSettings;
    private Intent intent;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengingat);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getText(R.string.str_Pengingat));
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

        lvReminders = findViewById(R.id.lvReminders);

        ivReminder.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam


        // Inisialisasi DatabaseHelper dan ListView
        dbHelper = new DatabaseHelper(this);
        reminderList = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(this, reminderList);  // Gunakan ReminderAdapter
        lvReminders.setAdapter(reminderAdapter);

        // Ambil pengingat dari database
        loadRemindersFromDatabase();

        // Set listener untuk ListView item
        lvReminders.setOnItemClickListener((parent, view, position, id) -> {
            String reminder = reminderList.get(position);
            Toast.makeText(this, "Pengingat dipilih: " + reminder, Toast.LENGTH_SHORT).show();
        });

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

    private void loadRemindersFromDatabase() {
        reminderList.clear();
        Cursor cursor = dbHelper.getAllAbsensi();

        if (cursor.moveToFirst()) {
            do {
                // Ambil data dari kolom database
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_NAME));
                String tanggal = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_TANGGAL));
                String hari = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_HARI));
                String jam = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_JAM));
                String tempat = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_TEMPAT));


                // Pastikan Hari dan Tempat hanya ditambahkan sekali
                jam = jam.replace(":", ".");
                String reminderData = "Nama: " + name + "; Tanggal: " + tanggal + "; Hari: " + hari + "; Tempat: " + tempat + "; Jam: " + jam;
                Log.d("DatabaseData", "Nama: " + name + ", Tanggal: " + tanggal + ", Hari: " + hari + ", Jam: " + jam + ", Tempat: " + tempat);

                // Tambahkan ke dalam ListView
                reminderList.add(reminderData);

                // Jadwalkan alarm jika tanggal dan jam valid
                try {
                    if (tanggal.contains("/") && jam.contains(":")) {
                        String[] dateParts = tanggal.split("/");
                        String[] timeParts = jam.split(":");

                        if (dateParts.length == 3 && timeParts.length == 2) {
                            int day = Integer.parseInt(dateParts[0]);
                            int month = Integer.parseInt(dateParts[1]) - 1; // Bulan dimulai dari 0
                            int year = Integer.parseInt(dateParts[2]);

                            int hour = Integer.parseInt(timeParts[0]);
                            int minute = Integer.parseInt(timeParts[1]);

                            // Jadwalkan alarm
                            scheduleAlarm(year, month, day, hour, minute);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("AlarmError", "Gagal menjadwalkan alarm: " + e.getMessage());
                    Toast.makeText(this, "Gagal menjadwalkan alarm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } while (cursor.moveToNext());
        } else {
            Log.w("DatabaseData", "Database kosong atau tidak ada data absensi.");
        }
        cursor.close();
        reminderAdapter.notifyDataSetChanged();
    }



    // Fungsi untuk mendapatkan nama hari berdasarkan tanggal
    private String getDayOfWeek(String tanggal) {
        try {
            String[] dateParts = tanggal.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Bulan dimulai dari 0
            int year = Integer.parseInt(dateParts[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Mendapatkan hari dari Calendar
            switch (dayOfWeek) {
                case Calendar.SUNDAY: return "Minggu";
                case Calendar.MONDAY: return "Senin";
                case Calendar.TUESDAY: return "Selasa";
                case Calendar.WEDNESDAY: return "Rabu";
                case Calendar.THURSDAY: return "Kamis";
                case Calendar.FRIDAY: return "Jumat";
                case Calendar.SATURDAY: return "Sabtu";
                default: return "Hari tidak valid";
            }
        } catch (Exception e) {
            Log.e("DateParsing", "Error parsing date: " + e.getMessage());
            return "Hari tidak ditemukan";
        }
    }

    private void scheduleAlarm(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Log.d("AlarmScheduling", "Calendar time (millis): " + calendar.getTimeInMillis() +
                ", Current time (millis): " + System.currentTimeMillis());

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            Log.w("AlarmScheduling", "Waktu alarm sudah lewat, tidak dapat dijadwalkan.");
            return;
        }

        Intent intent = new Intent(this, ReminderAdapter.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d("AlarmScheduling", "Alarm berhasil dijadwalkan untuk waktu: " + calendar.getTime());
        } else {
            Log.e("AlarmScheduling", "Gagal mendapatkan AlarmManager.");
        }
    }


}