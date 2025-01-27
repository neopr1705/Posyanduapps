package com.example.posyanduapps.Helper;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import com.example.posyanduapps.LoginActivity;
import com.example.posyanduapps.R;

public class HeaderIconHelper implements View.OnClickListener {

    private Context context;
    private ImageView ivLogout,ivChoice;

    private TextView tvChoice;

    // Constructor menerima konteks dan instance layout
    public HeaderIconHelper(Context context, View headerLayout) {
        this.context = context;


        // Inisialisasi ImageView dari layout
        ivLogout = headerLayout.findViewById(R.id.ivLogout);
        tvChoice = headerLayout.findViewById(R.id.tvchoice);
        ivChoice = headerLayout.findViewById(R.id.ivChoice);
        // Atur listener klik
        ivLogout.setOnClickListener(this);
        ivChoice.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivLogout) {
                // Tampilkan dialog konfirmasi
                    showLogoutConfirmationDialog();

        }
        if(v.getId()==R.id.ivChoice){

        }
    }


    private void showdropdownChoice() {
        // Membuat PopupMenu
        PopupMenu popupMenu = new PopupMenu(ivChoice.getContext(), ivChoice);


        // Menambahkan item pilihan
        Menu menu = popupMenu.getMenu();
        menu.add(Menu.NONE, 1, Menu.NONE, "Balita");
        menu.add(Menu.NONE, 2, Menu.NONE, "Lansia");
        menu.add(Menu.NONE, 3, Menu.NONE, "Bumil");

        // Menangani pemilihan item
        popupMenu.setOnMenuItemClickListener(item -> {
            // Menyimpan pilihan di SharedPreferences
            SharedPreferences sharedPreferences = ivChoice.getContext().getSharedPreferences("Option", MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            switch (item.getItemId()) {
                case 1:
                    // Pilihan Balita
                    editor.putInt("CurrentOption", 1);
                    ivChoice.setImageResource(R.drawable.baby); // Ganti icon
                    break;
                case 2:
                    // Pilihan Lansia
                    editor.putInt("CurrentOption", 2);
                    ivChoice.setImageResource(R.drawable.elder); // Ganti icon
                    break;
                case 3:
                    // Pilihan Bumil
                    editor.putInt("CurrentOption", 3);
                    ivChoice.setImageResource(R.drawable.ic_mom); // Ganti icon
                    break;
            }

            // Menyimpan perubahan SharedPreferences
            editor.apply();
            return true;
        });

        // Menampilkan PopupMenu
        popupMenu.show();
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

    public void setChoiceGONE(){
        tvChoice.setVisibility(View.GONE);
        ivChoice.setVisibility(View.GONE);
    }


}
