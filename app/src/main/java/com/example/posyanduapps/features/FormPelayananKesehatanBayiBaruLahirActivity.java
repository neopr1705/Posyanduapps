package com.example.posyanduapps.features;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.R;

public class FormPelayananKesehatanBayiBaruLahirActivity extends Activity {

    private CheckBox cbCatatPelayananNeonatus;
    private Button btnSaveBayiLahir;
    private TextView tvTitle;
    private ImageView logout;
    Button btnAddKN1Field, btnAddKN2Field, btnAddKN3Field, btnHapusSemua, btnRmKN1Field, btnRmKN2Field, btnRmKN3Field;;
    LinearLayout llKN1Container, llKN2Container, llKN3Container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelayanan_kesehatan_bayi_baru_lahir);

        View headerLayout = findViewById(R.id.header_layout);
        // Inisialisasi HeaderIconHelper
        new HeaderIconHelper(this, headerLayout);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Form Pelayanan Kesahatan Ibu Hamil");
        logout = findViewById(R.id.ivLogout);
        logout.setVisibility(View.GONE);



        // Initialize buttons and containers
        btnAddKN1Field = findViewById(R.id.btnAddKN1Field);
        btnAddKN2Field = findViewById(R.id.btnAddKN2Field);
        btnAddKN3Field = findViewById(R.id.btnAddKN3Field);
        btnSaveBayiLahir = findViewById(R.id.btnSaveBayiLahir);
        btnHapusSemua = findViewById(R.id.btnHapusSemua);
        btnRmKN1Field = findViewById(R.id.btnRmKN1Field);
        btnRmKN2Field = findViewById(R.id.btnRmKN2Field);
        btnRmKN3Field = findViewById(R.id.btnRmKN3Field);
        llKN1Container = findViewById(R.id.llKN1Container);
        llKN2Container = findViewById(R.id.llKN2Container);
        llKN3Container = findViewById(R.id.llKN3Container);


        // Add field to KN1
        btnAddKN1Field.setOnClickListener(v -> addDynamicEditText(llKN1Container));

        // Add field to KN2
        btnAddKN2Field.setOnClickListener(v -> addDynamicEditText(llKN2Container));

        // Add field to KN3
        btnAddKN3Field.setOnClickListener(v -> addDynamicEditText(llKN3Container));

        // Tambahkan listener untuk tombol simpan
        btnSaveBayiLahir.setOnClickListener(v -> saveData());

        // Menambahkan listener untuk tombol Hapus Semua
        btnHapusSemua.setOnClickListener(v -> hapusSemuaData());
        // Listener untuk tombol Hapus Semua input KN1
        btnRmKN1Field.setOnClickListener(v -> removeAllItemsFromKN1());

        // Listener untuk tombol Hapus Semua input KN2
        btnRmKN2Field.setOnClickListener(v -> removeAllItemsFromKN2());

        // Listener untuk tombol Hapus Semua input KN3
        btnRmKN3Field.setOnClickListener(v -> removeAllItemsFromKN3());
    }


    private void addDynamicEditText(LinearLayout container) {
        // Layout Horizontal untuk EditText dan Button
        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // EditText Dinamis
        EditText editText = new EditText(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                0, // Weight memungkinkan pengaturan ukuran dinamis
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1 // Weight untuk mengisi ruang horizontal
        ));
        editText.setHint("Masukkan data");
        editText.setPadding(16, 16, 16, 16);

        // Tombol Hapus
        Button btnDelete = new Button(this);
        btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        btnDelete.setText("X");
        btnDelete.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnDelete.setPadding(16, 8, 16, 8);
        btnDelete.setTextColor(getResources().getColor(android.R.color.darker_gray));

        // Fungsi Hapus Input
        btnDelete.setOnClickListener(v -> container.removeView(horizontalLayout));

        // Tambahkan EditText dan Tombol ke Layout Horizontal
        horizontalLayout.addView(editText);
        horizontalLayout.addView(btnDelete);

        // Tambahkan Layout Horizontal ke Container
        container.addView(horizontalLayout);
    }


    private void saveData() {
        StringBuilder allData = new StringBuilder();

        // Ambil data dari KN1
        allData.append("KN1 Data:\n");
        allData.append(getDataFromContainer(llKN1Container));

        // Ambil data dari KN2
        allData.append("KN2 Data:\n");
        allData.append(getDataFromContainer(llKN2Container));

        // Ambil data dari KN3
        allData.append("KN3 Data:\n");
        allData.append(getDataFromContainer(llKN3Container));

        // Simpan atau tampilkan hasilnya
        // Untuk demonstrasi, gunakan Toast
        new AlertDialog.Builder(this)
                .setTitle("Data Tersimpan")
                .setMessage(allData.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private String getDataFromContainer(LinearLayout container) {
        StringBuilder containerData = new StringBuilder();

        // Loop melalui semua elemen di dalam LinearLayout
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if (view instanceof LinearLayout) { // Mendeteksi LinearLayout yang berisi EditText dan Button
                LinearLayout horizontalLayout = (LinearLayout) view;
                for (int j = 0; j < horizontalLayout.getChildCount(); j++) {
                    View innerView = horizontalLayout.getChildAt(j);
                    if (innerView instanceof EditText) {
                        EditText editText = (EditText) innerView;
                        String inputData = editText.getText().toString();
                        Log.d("FormPelayanan", "Input Data: " + inputData); // Log data yang dimasukkan
                        if (!inputData.isEmpty()) {
                            containerData.append("- ").append(inputData).append("\n");
                        }
                    }
                }
            }
        }
        return containerData.toString();
    }

    private void hapusSemuaData() {
        // Periksa apakah ada item di setiap LinearLayout (KN1, KN2, KN3)
        boolean isDataPresent = false;

        if (llKN1Container.getChildCount() > 0) {
            isDataPresent = true;
        } else if (llKN2Container.getChildCount() > 0) {
            isDataPresent = true;
        } else if (llKN3Container.getChildCount() > 0) {
            isDataPresent = true;
        }

        // Jika ada item, hapus semua data
        if (isDataPresent) {
            llKN1Container.removeAllViews();
            llKN2Container.removeAllViews();
            llKN3Container.removeAllViews();

            // Menampilkan Toast sebagai konfirmasi bahwa data telah dihapus
            Toast.makeText(this, "Semua Data Telah Dihapus", Toast.LENGTH_SHORT).show();
        } else {
            // Jika tidak ada item, tampilkan Toast "Belum ada item"
            Toast.makeText(this, "Belum ada item untuk dihapus", Toast.LENGTH_SHORT).show();
        }
    }

    // Fungsi untuk menghapus semua item di KN1
    private void removeAllItemsFromKN1() {
        if (llKN1Container.getChildCount() > 0) {
            llKN1Container.removeAllViews();
            Toast.makeText(this, "Semua Data KN1 Telah Dihapus", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Belum ada item di KN1", Toast.LENGTH_SHORT).show();
        }
    }

    // Fungsi untuk menghapus semua item di KN2
    private void removeAllItemsFromKN2() {
        if (llKN2Container.getChildCount() > 0) {
            llKN2Container.removeAllViews();
            Toast.makeText(this, "Semua Data KN2 Telah Dihapus", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Belum ada item di KN2", Toast.LENGTH_SHORT).show();
        }
    }

    // Fungsi untuk menghapus semua item di KN3
    private void removeAllItemsFromKN3() {
        if (llKN3Container.getChildCount() > 0) {
            llKN3Container.removeAllViews();
            Toast.makeText(this, "Semua Data KN3 Telah Dihapus", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Belum ada item di KN3", Toast.LENGTH_SHORT).show();
        }
    }

}
