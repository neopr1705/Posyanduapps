package com.example.posyanduapps.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.R;
import com.example.posyanduapps.Helper.DatabaseHelper;  // Pastikan ada DatabaseHelper

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderAdapter extends ArrayAdapter<String> {

    private DatabaseHelper databaseHelper;  // Instansiasi DatabaseHelper untuk akses database
    String reminder_tanggal,reminder_jam;
    private ExecutorService executorService; // ExecutorService untuk tugas latar belakang
    private Handler mainHandler; // Handler untuk kembali ke thread utama
    public ReminderAdapter(Context context, List<String> reminders) {
        super(context, 0, reminders);
        databaseHelper = new DatabaseHelper(context);  // Inisialisasi DatabaseHelper
    }

    public ReminderAdapter(Context context) {
        super(context, 0);
        databaseHelper = new DatabaseHelper(context);  // Inisialisasi DatabaseHelper
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < 0 || position >= getCount()) {
            return null;  // Pastikan index valid
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
        }

        String reminder = getItem(position);  // Ambil data reminder

        if (reminder == null || reminder.isEmpty()) {
            return convertView;  // Jika data kosong, kembalikan tampilan default
        }
        // Inisialisasi ExecutorService dan Handler
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        // Set data ke tampilan item_alarm
        TextView tvReminderName = convertView.findViewById(R.id.tvNamaAlarm);
        TextView tvReminderTime = convertView.findViewById(R.id.tvJamAlarm);
        TextView tvReminderDate = convertView.findViewById(R.id.tvTanggalAlarm);
        TextView tvReminderDay = convertView.findViewById(R.id.tvHariAlarm);
        TextView tvReminderPlace = convertView.findViewById(R.id.tvTempatAlarm);
        Switch swAktif = convertView.findViewById(R.id.swAktif);  // Switch untuk aktifkan alarm

        // Misalnya, memisahkan bagian yang relevan
        String[] parts = reminder.split(";");
        if (parts.length >= 4) {
            String name = getDataFromPart(parts[0]);
            String date = getDataFromPart(parts[1]);
            String day = getDataFromPart(parts[2]);
            String place = getDataFromPart(parts[3]);
            String time = "";
            if (parts.length > 4) {
                time = getDataFromPart(parts[4]);
                time = time.replace(".", ":");
            }

            // Set data ke tampilan
            tvReminderName.setText("Nama: " + name);
            tvReminderDate.setText("Tanggal: " + date);
            tvReminderDay.setText("Hari: " + day);
            tvReminderTime.setText("Jam: " + time);
            tvReminderPlace.setText("Tempat: " + place);
            reminder_tanggal = date;
            reminder_jam = time;

        }

        // Logika saat Switch diubah
        swAktif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Alarm aktif, simpan ke database
                new android.app.AlertDialog.Builder(getContext())
                        .setMessage("Alarm untuk " + reminder_tanggal + "; " + reminder_jam + " ini akan ditambahkan?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            // Menjalankan proses penambahan alarm di background thread
                            executorService.submit(() -> {
                                // Menyisipkan alarm ke database dan mendapatkan ID alarm
                                boolean alarmId = databaseHelper.insertAlarm(reminder_tanggal, reminder_jam, 1); // 1 berarti aktif
                                mainHandler.post(() -> {
                                    if (alarmId != false) {
                                        // Alarm berhasil diset
                                        databaseHelper.insertAlarm(reminder_tanggal, reminder_jam,1); // Set Alarm menggunakan tanggal dan jam
                                        Toast.makeText(getContext(), "Alarm berhasil diset!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Jika insert gagal, tampilkan pesan kesalahan
                                        Toast.makeText(getContext(), "Gagal set alarm! Periksa format tanggal/jam atau koneksi database.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            });
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            } else {
                // Alarm dimatikan, konfirmasi hapus dari database
                new android.app.AlertDialog.Builder(getContext())
                        .setMessage("Apakah Anda yakin ingin mematikan alarm ini?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            // Pastikan reminder berisi ID alarm yang valid
                            int alarmId = Integer.parseInt(reminder); // ID alarm untuk dihapus
                            executorService.submit(() -> {
                                boolean isDeleted = databaseHelper.deleteAlarm(alarmId); // Hapus alarm berdasarkan ID
                                mainHandler.post(() -> {
                                    if (isDeleted) {
                                        Toast.makeText(getContext(), "Alarm berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Gagal hapus alarm!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });


        return convertView;
    }

    private String getDataFromPart(String part) {
        if (part != null && part.matches("\\d{2}.\\d{2}")) {
            return part.trim();
        }

        String[] splitData = part.split(":");
        if (splitData.length > 1) {
            return splitData[1].trim();
        }

        return part.trim();
    }
}
