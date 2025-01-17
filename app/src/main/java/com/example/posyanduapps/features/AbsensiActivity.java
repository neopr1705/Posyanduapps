package com.example.posyanduapps.features;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.MainActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.AbsensiAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private Spinner spinnerNamaPasien;
    private ArrayList<String> namaPasienList;
    private ArrayAdapter<String> adapter;
    // Helpers
    private DatabaseHelper databaseHelper;
    private ExecutorService executorService;
    private Handler mainHandler;
    String url="https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi);

        initHelpers();
        initUI();
        initListeners();
        fetchNamaPasienFromFirebase();
        loadAbsensiData();
    }

    private void initHelpers() {
        databaseHelper = new DatabaseHelper(this);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        absensiList = new ArrayList<>();

    }

    private void initUI() {
        // Inisialisasi Spinner dan List pasien
        spinnerNamaPasien = findViewById(R.id.spinnerNamaPasien);
        namaPasienList = new ArrayList<>();

        // Ambil nama pasien dari Firebase dan set ke Spinner

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText( R.string.str_AbsensiKehadiran);
        SharedPreferences sharedPreferences = getSharedPreferences("Option", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentOption", 4);  // username yang didapat saat login
        editor.apply();  // Menyimpan perubahan
        edtTempat = findViewById(R.id.edtTempat);

        tvTanggal = findViewById(R.id.tvTanggal);
        tvHari = findViewById(R.id.tvHari);
        tvJam = findViewById(R.id.tvJam);
        TextView tvchoice = findViewById(R.id.tvchoice);
        ImageView ivChoice = findViewById(R.id.ivChoice);
        tvchoice.setVisibility(View.GONE);
        ivChoice.setVisibility(View.GONE);
        btnHadir = findViewById(R.id.btnHadir);
        lvAbsensi = findViewById(R.id.lvAbsensi);

        customAdapter = new AbsensiAdapter(this, absensiList);
        lvAbsensi.setAdapter(customAdapter);

        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);
        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setImageResource(R.drawable.baseline_assignment_add_24);
        ivProfile.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
        ivSettings = findViewById(R.id.ivSettings);
        ivSettings.setVisibility(View.GONE);
        new HeaderIconHelper(this, findViewById(R.id.header_layout));

    }
    private void fetchNamaPasienFromFirebase() {
        // Firebase reference untuk mengambil data pasien dari URL Firebase yang sudah diberikan
        DatabaseReference database = FirebaseDatabase.getInstance(url).getReference("users");

        // Menambahkan listener untuk mengambil data pasien
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear daftar nama pasien sebelum menambahkan yang baru
                namaPasienList.clear();

                // Looping untuk mengambil data pasien (id dan nama_lengkap) dari Firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.child("id").getValue(String.class);  // Mendapatkan id pasien
                    String namaLengkap = snapshot.child("nama_lengkap").getValue(String.class);  // Mendapatkan nama lengkap pasien

                    if (id != null && namaLengkap != null) {


                            // Menggabungkan id dan nama lengkap untuk format yang diinginkan
                            String itemSpinner = id + " - " + namaLengkap;
                            namaPasienList.add(itemSpinner);  // Menambah ke list


                    }
                }

                // Memperbarui Spinner setelah data di-fetch
                setNamaPasienToSpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Menangani jika gagal mengambil data dari Firebase
                Toast.makeText(AbsensiActivity.this, "Error fetching data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Mengatur nama pasien yang sudah diambil ke dalam Spinner.
     */
    private void setNamaPasienToSpinner() {
        // Menyiapkan ArrayAdapter untuk Spinner
        adapter = new ArrayAdapter<>(AbsensiActivity.this, android.R.layout.simple_spinner_dropdown_item, namaPasienList);

        // Set adapter ke Spinner
        spinnerNamaPasien.setAdapter(adapter);
    }

    private void initListeners() {
        btnHadir.setOnClickListener(v -> tambahHadir());
        tvTanggal.setOnClickListener(v -> showDatePickerDialog());
        tvJam.setOnClickListener(v -> showTimePickerDialog());

        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);

        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
    }

    private void tambahHadir() {
        String nama = spinnerNamaPasien.getSelectedItem().toString();
        String strNama = nama.split(" - ")[1];
        String tempat = edtTempat.getText().toString();

        if (isInputValid(strNama, tempat)) {
            executorService.submit(() -> {
                boolean isDuplicate = databaseHelper.checkDuplicateAbsensi(selectedTanggal, selectedJam, tempat);

                mainHandler.post(() -> {
                    if (isDuplicate) {
                        showToast("Jadwal tidak tersedia");
                    } else if (addAbsensiToDatabase(strNama, tempat)) {
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
            intent = new Intent(this, MonitorUsersActivity.class);
        } else if (v.getId() == ivReminder.getId()) {
            intent = new Intent(this, PengingatActivity.class);
        } else if (v.getId() == ivSettings.getId()) {
            intent = new Intent(this, DataIbuActivity.class);
        } else if (v.getId() == ivProfile.getId()) {
            intent = new Intent(this, EdukasiBumilActivity.class);
        } else {
            return;
        }
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
