package com.example.posyanduapps.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.posyanduapps.LoginActivity;
import com.example.posyanduapps.R;

public class HeaderIconHelper implements View.OnClickListener {

    private Context context;
    private ImageView ivLogout;

    // Constructor menerima konteks dan instance layout
    public HeaderIconHelper(Context context, View headerLayout) {
        this.context = context;

        // Inisialisasi ImageView dari layout
        ivLogout = headerLayout.findViewById(R.id.ivLogout);

        // Atur listener klik
        ivLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivLogout) {
                // Tampilkan dialog konfirmasi
                    showLogoutConfirmationDialog();

        }
    }

    private void showLogoutConfirmationDialog() {
        // Buat dialog builder
        new AlertDialog.Builder(context,R.style.AppTemaDialog)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    // Eksekusi logout
                    Toast.makeText(context, "Logout berhasil", Toast.LENGTH_SHORT).show();

                    // Navigasi ke halaman Login
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                })
                .setNegativeButton("Tidak", (dialog, which) -> {
                    // Tutup dialog tanpa melakukan apapun
                    dialog.dismiss();
                })
                .create()
                .show();
    }

}
