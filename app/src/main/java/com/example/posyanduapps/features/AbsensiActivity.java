package com.example.posyanduapps.features;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.MainActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.AbsensiAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AbsensiActivity extends Activity implements View.OnClickListener {

    // UI Components
    private EditText edtNama, edtTempat;
    private Button btnHadir;
    private ListView lvAbsensi;
    private TextView tvTanggal, tvHari, tvJam, tvTitle;
    private ImageView ivHome, ivReminder, ivAddAbsensi, ivProfile, ivSettings;

    // Variables
    private String selectedTanggal, selectedHari, selectedJam;
    private String currentUser;
    private ArrayList<String> absensiList;
    private AbsensiAdapter customAdapter;

    // Helpers
    private DatabaseHelper databaseHelper;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi);

        initHelpers();
        initUI();
        initListeners();
        loadAbsensiData();
    }

    private void initHelpers() {
        databaseHelper = new DatabaseHelper(this);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        absensiList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        currentUser = sharedPreferences.getString("currentNama", null);
    }

    private void initUI() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.str_AbsensiKehadiran);

        edtNama = findViewById(R.id.edtNama);
        edtNama.setHint(currentUser);
        edtTempat = findViewById(R.id.edtTempat);

        tvTanggal = findViewById(R.id.tvTanggal);
        tvHari = findViewById(R.id.tvHari);
        tvJam = findViewById(R.id.tvJam);

        btnHadir = findViewById(R.id.btnHadir);
        lvAbsensi = findViewById(R.id.lvAbsensi);

        customAdapter = new AbsensiAdapter(this, absensiList);
        lvAbsensi.setAdapter(customAdapter);

        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);
        ivAddAbsensi = findViewById(R.id.ivAddAbsensi);
        ivAddAbsensi.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
        ivProfile = findViewById(R.id.ivProfile);
        ivSettings = findViewById(R.id.ivSettings);

        new HeaderIconHelper(this, findViewById(R.id.header_layout));
    }

    private void initListeners() {
        btnHadir.setOnClickListener(v -> tambahHadir());
        tvTanggal.setOnClickListener(v -> showDatePickerDialog());
        tvJam.setOnClickListener(v -> showTimePickerDialog());

        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivAddAbsensi.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
    }

    private void tambahHadir() {
        String nama = edtNama.getText().toString();
        String tempat = edtTempat.getText().toString();

        if (isInputValid(nama, tempat)) {
            executorService.submit(() -> {
                boolean isDuplicate = databaseHelper.checkDuplicateAbsensi(selectedTanggal, selectedJam, tempat);

                mainHandler.post(() -> {
                    if (isDuplicate) {
                        showToast("Jadwal tidak tersedia");
                    } else if (addAbsensiToDatabase(nama, tempat)) {
                        showToast("Absensi berhasil ditambahkan");
                        clearInputFields();
                    } else {
                        showToast("Gagal menambahkan absensi");
                    }
                });
            });
        } else {
            showToast("Harap isi semua data!");
        }
    }

    private boolean isInputValid(String nama, String tempat) {
        return !nama.isEmpty() && !selectedTanggal.isEmpty() && !selectedHari.isEmpty() && !selectedJam.isEmpty() && !tempat.isEmpty();
    }

    private boolean addAbsensiToDatabase(String nama, String tempat) {
        boolean isInserted = databaseHelper.insertAbsensi(nama, selectedTanggal, selectedHari, selectedJam, tempat);
        if (isInserted) {
            absensiList.add(formatAbsensiString(nama, tempat));
            customAdapter.notifyDataSetChanged();
        }
        return isInserted;
    }

    private String formatAbsensiString(String nama, String tempat) {
        return "Nama: " + nama + "\n" +
                "Tanggal: " + selectedTanggal + "\n" +
                "Hari: " + selectedHari + "\n" +
                "Jam: " + selectedJam + "\n" +
                "Tempat: " + tempat;
    }

    private void clearInputFields() {
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
                while (cursor.moveToNext()) {
                    absensiList.add(formatAbsensiFromCursor(cursor));
                }
                customAdapter.notifyDataSetChanged();
                cursor.close();
            });
        });
    }

    private String formatAbsensiFromCursor(Cursor cursor) {
        return "Nama: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_NAME)) + "\n" +
                "Tanggal: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_TANGGAL)) + "\n" +
                "Hari: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_HARI)) + "\n" +
                "Jam: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_JAM)) + "\n" +
                "Tempat: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ABSENSI_TEMPAT));
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.CustomDatePicker,
                (view, year, month, dayOfMonth) -> {
                    selectedTanggal = dayOfMonth + "/" + (month + 1) + "/" + year;
                    tvTanggal.setText(selectedTanggal);

                    calendar.set(year, month, dayOfMonth);
                    selectedHari = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
                    tvHari.setText(selectedHari);
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY: return "Minggu";
            case Calendar.MONDAY: return "Senin";
            case Calendar.TUESDAY: return "Selasa";
            case Calendar.WEDNESDAY: return "Rabu";
            case Calendar.THURSDAY: return "Kamis";
            case Calendar.FRIDAY: return "Jumat";
            case Calendar.SATURDAY: return "Sabtu";
            default: return "";
        }
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.CustomDatePicker,
                (view, hourOfDay, minute) -> {
                    selectedJam = hourOfDay + ":" + minute;
                    tvJam.setText(selectedJam);
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == ivHome.getId()) {
            intent = new Intent(this, MainActivity.class);
        } else if (v.getId() == ivAddAbsensi.getId()) {
            intent = new Intent(this, AbsensiActivity.class);
        } else if (v.getId() == ivReminder.getId()) {
            intent = new Intent(this, PengingatActivity.class);
        } else if (v.getId() == ivSettings.getId()) {
            intent = new Intent(this, DataIbuActivity.class);
        } else if (v.getId() == ivProfile.getId()) {
            intent = new Intent(this, EdukasiActivity.class);
        } else {
            return;
        }
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
