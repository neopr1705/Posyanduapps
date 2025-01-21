package com.example.posyanduapps.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.posyanduapps.R;
import com.example.posyanduapps.models.Absensi;

public class DialogUtils {
    public static void showEditAbsensiDialog(Context context, Absensi absensi, AbsensiUpdateListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_absensi, null);

        // Bind view
        EditText etNama = dialogView.findViewById(R.id.editNama);
        TextView tvTanggal = dialogView.findViewById(R.id.editTanggal);
        TextView tvHari = dialogView.findViewById(R.id.editHari);
        TextView tvJam = dialogView.findViewById(R.id.editJam);
        EditText etTempat = dialogView.findViewById(R.id.editTempat);

        // Set data awal
        etNama.setText(absensi.getNama());
        tvTanggal.setText(absensi.getTanggal());
        tvHari.setText(absensi.getHari());
        tvJam.setText(absensi.getJam());
        etTempat.setText(absensi.getTempat());

        // Tampilkan dialog
        new android.app.AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    Absensi updatedAbsensi = new Absensi(
                            etNama.getText().toString(),
                            tvTanggal.getText().toString(),
                            tvHari.getText().toString(),
                            tvJam.getText().toString(),
                            etTempat.getText().toString()
                    );
                    listener.onAbsensiUpdated(updatedAbsensi);
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public interface AbsensiUpdateListener {
        void onAbsensiUpdated(Absensi updatedAbsensi);
    }
}
