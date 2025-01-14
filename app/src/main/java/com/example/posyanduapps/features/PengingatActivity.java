package com.example.posyanduapps.features;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.MainActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.adapters.ReminderAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class PengingatActivity extends Activity implements View.OnClickListener {

    private Button btnSetReminder;
    private ListView lvReminders;
    private TextView tvTitle;

    private DatabaseHelper dbHelper;
    private ArrayList<String> reminderList;
    private ReminderAdapter reminderAdapter;

    private ImageView ivHome, ivReminder, ivAddAbsensi, ivProfile, ivSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengingat);

        initializeUI();
        initializeDatabase();
        loadRemindersFromDatabase();
    }

    private void initializeUI() {
        // Initialize header
        View headerLayout = findViewById(R.id.header_layout);
        new HeaderIconHelper(this, headerLayout);

        // Set title
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getText(R.string.str_Pengingat));

        // Initialize footer buttons
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

        // Set the color for the reminder icon
        ivReminder.setColorFilter(getResources().getColor(R.color.softBlue));

        // Initialize ListView
        lvReminders = findViewById(R.id.lvReminders);
    }

    private void initializeDatabase() {
        dbHelper = new DatabaseHelper(this);
        reminderList = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(this, reminderList);
        lvReminders.setAdapter(reminderAdapter);
    }

    private void loadRemindersFromDatabase() {
        reminderList.clear();
        Cursor cursor = dbHelper.getAllAbsensi();

        if (cursor.moveToFirst()) {
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
        }
        cursor.close();
        reminderAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        // Menggunakan Intent untuk membuka aktivitas yang sesuai dengan tombol yang diklik
        Intent intent;

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
