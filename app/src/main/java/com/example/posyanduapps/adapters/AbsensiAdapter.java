package com.example.posyanduapps.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.DatePickerDialog; // Untuk menampilkan dialog pemilih tanggal
import android.app.TimePickerDialog; // Untuk menampilkan dialog pemilih tanggal

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.R;
import com.example.posyanduapps.models.Absensi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import android.widget.EditText;

public class AbsensiAdapter extends android.widget.BaseAdapter {
    private Context context;
    private ArrayList<Absensi> absensiList;
    private DatabaseReference databaseReference;
    private String url="https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";


    public AbsensiAdapter(Context context, ArrayList<Absensi> absensiList) {
        this.context = context;
        this.absensiList = absensiList;
        this.databaseReference  =  FirebaseDatabase.getInstance(url).getReference("absensi"); // Inisialisasi DatabaseHelper
    }

    @Override
    public int getCount() {
        return absensiList.size();
    }

    @Override
    public Object getItem(int position) {
        return absensiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        if (convertView == null) {
            convertView = android.view.LayoutInflater.from(context).inflate(R.layout.item_absensi, parent, false);
        }

        TextView tvAbsensi = convertView.findViewById(R.id.tvAbsensi);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

        // Set data ke TextView

        Absensi absensi = absensiList.get(position);
        tvAbsensi.setText(absensi.toString());

        // Fungsi Edit
//        btnEdit.setOnClickListener(v -> showEditDialog(position));

        // Fungsi Hapus
        // Setup tombol hapus
        // Setup tombol hapus
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog(absensi, position));


        return convertView;
    }

    private void showDeleteConfirmationDialog(Absensi absensi, int position) {
        new AlertDialog.Builder(context, R.style.AppTemaDialog)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus jadwal ini?")
                .setPositiveButton("Ya", (dialog, which) -> deleteAbsensiFromFirebase(absensi, position))
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Hapus data dari Firebase
     */
    private void deleteAbsensiFromFirebase(Absensi absensi, int position) {
        String path = "root/absensi/" + absensi.getId();
        Log.d("Path yang akan dihapus:", path);
        databaseReference
                .child(absensi.getId()) // Asumsi setiap data memiliki ID unik di Firebase
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    absensiList.remove(position); // Hapus dari daftar lokal
                    notifyDataSetChanged();
                    Toast.makeText(context, "Data absensi dihapus!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Gagal menghapus data!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

//    private void showEditDialog(int position) {
//        // Inflate custom layout
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View dialogView = inflater.inflate(R.layout.dialog_edit_absensi, null);
//
//        // Deklarasi EditText dan TextView dari custom layout
//        EditText etNama = dialogView.findViewById(R.id.editNama);
//        TextView tvTanggal = dialogView.findViewById(R.id.editTanggal);
//        TextView tvHari = dialogView.findViewById(R.id.editHari);
//        TextView tvJam = dialogView.findViewById(R.id.editJam);
//        EditText etTempat = dialogView.findViewById(R.id.editTempat);
//
//        // Isi data sebelumnya (jika ada) ke komponen dialog
//        String existingData = absensiList.get(position);
//        Log.d("Data yang akan dihapus:", existingData);
//        // Ekstrak data dari string absensi yang ada
//        String nama = extractData(existingData, "Nama");
//        String tanggal = extractData(existingData, "Tanggal");
//        String hari = extractData(existingData, "Hari");
//        String jam = extractData(existingData, "Jam");
//        String tempat = extractData(existingData, "Tempat");
//
//        etNama.setText(nama);
//        etTempat.setText(tempat);
//        tvTanggal.setText(tanggal);
//        tvHari.setText(hari);
//        tvJam.setText(jam);
//
//
//        // Setup dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setView(dialogView);
//        builder.setPositiveButton("Simpan", (dialog, which) -> {
//            // Ambil data dari input
//            String Inputnama = etNama.getText().toString();
//            String Inputtanggal = tvTanggal.getText().toString();
//            String Inputhari = tvHari.getText().toString();
//            String Inputjam = tvJam.getText().toString();
//            String Inputtempat = etTempat.getText().toString();
//
//            // Format data yang akan disimpan
//            String updatedAbsensi = "Nama: " + Inputnama + "\nTanggal: " + Inputtanggal + "\nHari: " + Inputhari
//                    + "\nJam: " + Inputjam + "\nTempat: " + Inputtempat;
//            boolean isDuplicate = databaseHelper.checkDuplicateAbsensi(Inputtanggal, Inputjam,Inputtempat);
//            if (isDuplicate) {
//                Toast.makeText(context, "Jadwal tidak tersedia", Toast.LENGTH_SHORT).show();
//                return;
//            }else {
//                // Perbarui data di database
//                boolean isUpdated = databaseHelper.updateAbsensi(nama, tanggal, Inputnama, Inputtanggal, Inputhari, Inputjam, Inputtempat);
//                if (isUpdated) {
//                    // Update data di list
//                    absensiList.set(position, updatedAbsensi);
//                    notifyDataSetChanged();
//                    Toast.makeText(context, "Absensi berhasil diperbarui!", Toast.LENGTH_SHORT).show();
////                loadAbsensiData(); // Panggil kembali jika perlu sinkronisasi ulang
//                } else {
//                    Toast.makeText(context, "Gagal memperbarui absensi!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
//
//        // Tambahkan listener untuk elemen yang interaktif
//        tvTanggal.setOnClickListener(v -> {
//            // Ketika tanggal dipilih, hari otomatis diperbarui
//            // Panggil dengan TextView tvTanggal dan tvHari yang sesuai
//            showDatePickerDialog(tvTanggal, tvHari);
//
//        });
//
//        tvJam.setOnClickListener(v -> {
//            // Logika untuk memilih jam (gunakan TimePickerDialog)
//            showTimePickerDialog(tvJam);
//        });
//
//        // Tampilkan dialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    // Fungsi tambahan untuk DatePicker
    private void showDatePickerDialog(TextView tvTanggal, TextView tvHari) {
        // Buat Calendar instance untuk tanggal saat ini sebagai default
        Calendar calendar = Calendar.getInstance();

        // Tampilkan DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                R.style.CustomDatePicker,
                (view, year, month, dayOfMonth) -> {
                    // Format tanggal yang dipilih
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    tvTanggal.setText(selectedDate);

                    // Perbarui TextView Hari berdasarkan tanggal yang dipilih
                    calendar.set(year, month, dayOfMonth);
                    String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
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

    // Fungsi tambahan untuk TimePicker
    private void showTimePickerDialog(TextView tvJam) {
        // Buat TimePickerDialog dan set listener
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                R.style.CustomDatePicker,
                (view, hourOfDay, minute) -> {
                    String selectedTime = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
                    tvJam.setText(selectedTime);
                },
                /* jam default */ 12,
                /* menit default */ 0,
                true
        );
        timePickerDialog.show();
    }
    // Fungsi untuk mengekstrak data berdasarkan label
    private String extractData(String data, String label) {
        int startIdx = data.indexOf(label + ": ") + label.length() + 2;
        int endIdx = data.indexOf("\n", startIdx);
        String extractedData = endIdx != -1 ? data.substring(startIdx, endIdx) : data.substring(startIdx);
        Log.d("ExtractedData", "Label: " + label + ", Value: " + extractedData);
        return extractedData.trim();
    }
}