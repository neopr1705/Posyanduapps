package com.example.posyanduapps.features;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.example.posyanduapps.R;
import com.example.posyanduapps.RegisterActivity;
import com.example.posyanduapps.models.FormDataIbu;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.utils.FirebaseManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class DataIbuActivity extends Activity implements View.OnClickListener {


    private ImageView ivHome, ivReminder, ivAddAbsensi, ivProfile, ivSettings;
    private Intent intent;
    private TextView tvTitle;
    int currentOption ;
    private Context context;
    int hexID;

    private TextView tvFormDetails, tvIbuHamilTitle, tvPemeriksaanTitle;
    private EditText etBeratBadanBayi, etTinggiBadanBayi, etLingkarKepalaBayi,
            etStatusObatVaksinBayi, etRiwayatPenyakitBayi;


    private EditText etBeratBadanLansia, etTinggiBadanLansia, etLingkarKepalaLansia,
            etLingkarPerutLansia, etStatusObatLansia, etRiwayatPenyakitLansia;


    private EditText etBeratBadanBumil, etTinggiBadanBumil, etLingkarKepalaBumil,
            etLingkarLenganBumil, etStatusObatVaksinBumil, etRiwayatPenyakitBumil;


    private Button btnSimpan,btnHapus;

    private String url="https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";

    SharedPreferences UserPreferences;
    String Userbucket;

    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelayanan_kesehatan_ibu_hamil);

        // Initialize UI components
        initializeUI();

        initializeIncludes();
        setupUIclass();

        // Set up the save button click listener
        setupSaveButton();
        setupHapusButton();

    }



    // Fungsi untuk menghasilkan sample data FormDataIbu
    // Initialize UI components
    private void setupUIclass() {
        if(currentOption==1){
            // Tampilkan EditText untuk Balita
            etBeratBadanBayi.setVisibility(View.VISIBLE);
            etTinggiBadanBayi.setVisibility(View.VISIBLE);
            etLingkarKepalaBayi.setVisibility(View.VISIBLE);
            etStatusObatVaksinBayi.setVisibility(View.VISIBLE);
            etRiwayatPenyakitBayi.setVisibility(View.VISIBLE);

        }else if(currentOption==2){

            // Tampilkan EditText untuk Lansia
            etBeratBadanLansia.setVisibility(View.VISIBLE);
            etTinggiBadanLansia.setVisibility(View.VISIBLE);
            etLingkarKepalaLansia.setVisibility(View.VISIBLE);
            etLingkarPerutLansia.setVisibility(View.VISIBLE);
            etStatusObatLansia.setVisibility(View.VISIBLE);
            etRiwayatPenyakitLansia.setVisibility(View.VISIBLE);

        }else if(currentOption==3){
            // Tampilkan EditText untuk Bumil
            etBeratBadanBumil.setVisibility(View.VISIBLE);
            etTinggiBadanBumil.setVisibility(View.VISIBLE);
            etLingkarKepalaBumil.setVisibility(View.VISIBLE);
            etLingkarLenganBumil.setVisibility(View.VISIBLE);
            etStatusObatVaksinBumil.setVisibility(View.VISIBLE);
            etRiwayatPenyakitBumil.setVisibility(View.VISIBLE);

        }else{

        }
    }
    private void initializeIncludes(){
        SharedPreferences sharedPreferences = getSharedPreferences("Option", MODE_PRIVATE);
        currentOption = sharedPreferences.getInt("currentOption",-1);
        Userbucket = UserPreferences.getString("BucketUser","");
        View headerLayout = findViewById(R.id.header_layout);
        // Inisialisasi HeaderIconHelper
        new HeaderIconHelper(this, headerLayout);
        tvTitle = findViewById(R.id.tvTitle);
        if (currentOption ==3) {
            tvTitle.setText("Data Ibu Hamil");
            tvPemeriksaanTitle.setText("Pemeriksaan Data Vital Ibu Hamil");
        }else if(currentOption ==1){
            tvTitle.setText("Data Bayi ");
            tvPemeriksaanTitle.setText("Pemeriksaan Data Vital Bayi lima tahun");
        }else if(currentOption ==2){
            tvTitle.setText("Data Lansia");
            tvPemeriksaanTitle.setText("Pemeriksaan Data Vital Lansia");
        }else{
            tvTitle.setText("Mom Care");
        }
        logout = findViewById(R.id.ivLogout);
        logout.setVisibility(View.GONE);

        //footer
        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);
        ivProfile = findViewById(R.id.ivProfile);
        ivSettings = findViewById(R.id.ivSettings);
        ivSettings.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam

        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);

        setChoiceImage();
    }
    private void initializeUI() {

        UserPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);

        tvPemeriksaanTitle = findViewById(R.id.tvPemeriksaanTitle);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnHapus = findViewById(R.id.btn_clearfields);

        // Inisialisasi variabel untuk Balita
        etBeratBadanBayi = findViewById(R.id.et_berat_badan_bayi);
        etTinggiBadanBayi = findViewById(R.id.et_tinggi_badan_bayi);
        etLingkarKepalaBayi = findViewById(R.id.et_lingkar_kepala_bayi);
        etStatusObatVaksinBayi = findViewById(R.id.et_status_obat_vaksin_bayi);
        etRiwayatPenyakitBayi = findViewById(R.id.et_riwayat_penyakit_bayi);

        // Inisialisasi variabel untuk Lansia
        etBeratBadanLansia = findViewById(R.id.et_berat_badan_lansia);
        etTinggiBadanLansia = findViewById(R.id.et_tinggi_badan_lansia);
        etLingkarKepalaLansia = findViewById(R.id.et_lingkar_kepala_lansia);
        etLingkarPerutLansia = findViewById(R.id.et_lingkar_perut_lansia);
        etStatusObatLansia = findViewById(R.id.et_status_obat_lansia);
        etRiwayatPenyakitLansia = findViewById(R.id.et_riwayat_penyakit_lansia);

        // Inisialisasi variabel untuk Bumil
        etBeratBadanBumil = findViewById(R.id.et_berat_badan_bumil);
        etTinggiBadanBumil = findViewById(R.id.et_tinggi_badan_bumil);
        etLingkarKepalaBumil = findViewById(R.id.et_lingkar_kepala_bumil);
        etLingkarLenganBumil = findViewById(R.id.et_lingkar_lengan_bumil);
        etStatusObatVaksinBumil = findViewById(R.id.et_status_obat_vaksin_bumil);
        etRiwayatPenyakitBumil = findViewById(R.id.et_riwayat_penyakit_bumil);

        hideAllEditTexts();

    }

    private void setChoiceImage(){
        ImageView ivChoice = findViewById(R.id.ivChoice);
        if(currentOption == 1){
            ivChoice.setImageResource(R.drawable.baby);
        }else if(currentOption == 2){
            ivChoice.setImageResource(R.drawable.elder);
        } else if (currentOption==3) {
            ivChoice.setImageResource(R.drawable.ic_mom);
        }else{
            ivChoice.setVisibility(View.GONE);
        }
    }
    private void hideAllEditTexts() {
        // Sembunyikan EditText untuk Balita
        etBeratBadanBayi.setVisibility(View.GONE);
        etTinggiBadanBayi.setVisibility(View.GONE);
        etLingkarKepalaBayi.setVisibility(View.GONE);
        etStatusObatVaksinBayi.setVisibility(View.GONE);
        etRiwayatPenyakitBayi.setVisibility(View.GONE);

        // Sembunyikan EditText untuk Lansia
        etBeratBadanLansia.setVisibility(View.GONE);
        etTinggiBadanLansia.setVisibility(View.GONE);
        etLingkarKepalaLansia.setVisibility(View.GONE);
        etLingkarPerutLansia.setVisibility(View.GONE);
        etStatusObatLansia.setVisibility(View.GONE);
        etRiwayatPenyakitLansia.setVisibility(View.GONE);

        // Sembunyikan EditText untuk Bumil
        etBeratBadanBumil.setVisibility(View.GONE);
        etTinggiBadanBumil.setVisibility(View.GONE);
        etLingkarKepalaBumil.setVisibility(View.GONE);
        etLingkarLenganBumil.setVisibility(View.GONE);
        etStatusObatVaksinBumil.setVisibility(View.GONE);
        etRiwayatPenyakitBumil.setVisibility(View.GONE);
    }


    private void setupSaveButton() {
        btnSimpan.setOnClickListener(v -> saveFormData());
    }
    private void setupHapusButton(){
        btnHapus.setOnClickListener(v -> resetAllEditText());
    }
    // Save the form data
    private void saveFormData() {
        // Ambil data dari EditText
        String beratBadanBayi = etBeratBadanBayi.getText().toString();
        String tinggiBadanBayi = etTinggiBadanBayi.getText().toString();
        String lingkarKepalaBayi = etLingkarKepalaBayi.getText().toString();
        String statusObatVaksinBayi = etStatusObatVaksinBayi.getText().toString();
        String riwayatPenyakitBayi = etRiwayatPenyakitBayi.getText().toString();


        String beratBadanLansia = etBeratBadanLansia.getText().toString();
        String tinggiBadanLansia = etTinggiBadanLansia.getText().toString();
        String lingkarKepalaLansia = etLingkarKepalaLansia.getText().toString();
        String lingkarPerutLansia = etLingkarPerutLansia.getText().toString();
        String statusObatLansia = etStatusObatLansia.getText().toString();
        String riwayatPenyakitLansia = etRiwayatPenyakitLansia.getText().toString();

        String beratBadanBumil = etBeratBadanBumil.getText().toString();
        String tinggiBadanBumil = etTinggiBadanBumil.getText().toString();
        String lingkarKepalaBumil = etLingkarKepalaBumil.getText().toString();
        String lingkarLenganBumil = etLingkarLenganBumil.getText().toString();
        String statusObatVaksinBumil = etStatusObatVaksinBumil.getText().toString();
        String riwayatPenyakitBumil = etRiwayatPenyakitBumil.getText().toString();

        if(currentOption==1){
            if(beratBadanBayi.equalsIgnoreCase("")||
                    tinggiBadanBayi.equalsIgnoreCase("")||
                    lingkarKepalaBayi.equalsIgnoreCase("")||
                    statusObatVaksinBayi.equalsIgnoreCase("")||
                    riwayatPenyakitBayi.equalsIgnoreCase("")){
                showToast("Mohon isi semua kolom!");
            }else{
                // Buat pesan konfirmasi
                String confirmationMessage = createConfirmationMessage(
                        beratBadanBayi, tinggiBadanBayi, lingkarKepalaBayi, statusObatVaksinBayi, riwayatPenyakitBayi,
                        beratBadanLansia, tinggiBadanLansia, lingkarKepalaLansia, lingkarPerutLansia, statusObatLansia, riwayatPenyakitLansia,
                        beratBadanBumil, tinggiBadanBumil, lingkarKepalaBumil, lingkarLenganBumil, statusObatVaksinBumil, riwayatPenyakitBumil);

                hexID = confirmationMessage.hashCode();
                // Tampilkan dialog konfirmasi
                showDialogConfirmation(hexID);
            }
        }else if(currentOption==2){
            if(beratBadanLansia.equalsIgnoreCase("")||tinggiBadanLansia.equalsIgnoreCase("")||lingkarKepalaLansia.equalsIgnoreCase("")||lingkarPerutLansia.equalsIgnoreCase("")||statusObatLansia.equalsIgnoreCase("")||riwayatPenyakitLansia.equalsIgnoreCase("")){
                showToast("Mohon isi semua kolom!");
            }else{
                // Buat pesan konfirmasi
                String confirmationMessage = createConfirmationMessage(
                        beratBadanBayi, tinggiBadanBayi, lingkarKepalaBayi, statusObatVaksinBayi, riwayatPenyakitBayi,
                        beratBadanLansia, tinggiBadanLansia, lingkarKepalaLansia, lingkarPerutLansia, statusObatLansia, riwayatPenyakitLansia,
                        beratBadanBumil, tinggiBadanBumil, lingkarKepalaBumil, lingkarLenganBumil, statusObatVaksinBumil, riwayatPenyakitBumil);

                hexID = confirmationMessage.hashCode();
                // Tampilkan dialog konfirmasi
                showDialogConfirmation(hexID);
            }

        }else if(currentOption==3){
            if(beratBadanBumil.equalsIgnoreCase("")||tinggiBadanBumil.equalsIgnoreCase("")||lingkarKepalaBumil.equalsIgnoreCase("")||lingkarLenganBumil.equalsIgnoreCase("")||statusObatVaksinBumil.equalsIgnoreCase("")||riwayatPenyakitBumil.equalsIgnoreCase("")){
                showToast("Mohon isi semua kolom!");
            }else{

                String confirmationMessage = createConfirmationMessage(
                        beratBadanBayi, tinggiBadanBayi, lingkarKepalaBayi, statusObatVaksinBayi, riwayatPenyakitBayi,
                        beratBadanLansia, tinggiBadanLansia, lingkarKepalaLansia, lingkarPerutLansia, statusObatLansia, riwayatPenyakitLansia,
                        beratBadanBumil, tinggiBadanBumil, lingkarKepalaBumil, lingkarLenganBumil, statusObatVaksinBumil, riwayatPenyakitBumil);

                hexID = confirmationMessage.hashCode();
                // Tampilkan dialog konfirmasi
                showDialogConfirmation(hexID);
            }


        }


    }

    // Create a confirmation message to show after saving the form data
    private String createConfirmationMessage(String... values) {
        DatabaseReference database = FirebaseDatabase.getInstance(url).getReference();
        HashMap<String, Object> data = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // Format tanggal
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");  // Format waktu
        Date now = new Date();
        data.put("id",ServerValue.TIMESTAMP);
        if(currentOption==1){
            data.put("berat_badan",values[0]);
            data.put("tinggi_badan",values[1]);
            data.put("lingkar_kepala",values[2]);
            data.put("status_obat_vaksin",values[3]);
            data.put("riwayat_penyakit",values[4]);
            data.put("tanggal",dateFormat.format(now));
            data.put("jam",timeFormat.format(now));
            database.child("users")
                    .child(Userbucket)
                    .child("data_user")
                    .child("bayi").push()
                    .setValue(data);
            return "Form Data Saved:\n" +
                    "\n- Berat Badan Bayi: " + values[0] +
                    "\n- Tinggi Badan Bayi: " + values[1] +
                    "\n- Lingkar Kepala Bayi: " + values[2] +
                    "\n- Status Obat & Vaksin Bayi: " + values[3] +
                    "\n- Riwayat Penyakit Bayi: " + values[4];
        }else if(currentOption==2){
            data.put("berat_badan",values[5]);
            data.put("tinggi_badan",values[6]);
            data.put("lingkar_kepala",values[7]);
            data.put("lingkar_perut",values[8]);
            data.put("status_obat_vaksin",values[9]);
            data.put("riwayat_penyakit",values[10]);
            data.put("tanggal",dateFormat.format(now));
            data.put("jam",timeFormat.format(now));
            database.child("users")
                    .child(Userbucket)
                    .child("data_user")
                    .child("lansia").push()
                    .setValue(data);
            return "Form Data Saved:\n" +
                    "\n- Berat Badan Lansia: " + values[5] +
                    "\n- Tinggi Badan Lansia: " + values[6] +
                    "\n- Lingkar Kepala Lansia: " + values[7] +
                    "\n- Lingkar Perut Lansia: " + values[8] +
                    "\n- Status Obat Lansia: " + values[9] +
                    "\n- Riwayat Penyakit Lansia: " + values[10];
        }else if(currentOption==3){
            data.put("berat_badan",values[11]);
            data.put("tinggi_badan",values[12]);
            data.put("lingkar_kepala",values[13]);
            data.put("lingkar_lengan",values[14]);
            data.put("status_obat_vaksin",values[15]);
            data.put("riwayat_penyakit",values[16]);
            data.put("tanggal",dateFormat.format(now));
            data.put("jam",timeFormat.format(now));
            database.child("users")
                    .child(Userbucket)
                    .child("data_user")
                    .child("bumil").push()
                    .setValue(data);
            return "Form Data Saved:\n"
                    +"\n- Berat Badan Bumil: " + values[11] +
                    "\n- Tinggi Badan Bumil: " + values[12] +
                    "\n- Lingkar Kepala Bumil: " + values[13] +
                    "\n- Lingkar Lengan Bumil: " + values[14] +
                    "\n- Status Obat & Vaksin Bumil: " + values[15] +
                    "\n- Riwayat Penyakit Bumil: " + values[16];
        }

        return "";
    }

    private boolean areInputsValid(String... inputs) {
        for (String input : inputs) {
            if (TextUtils.isEmpty(input)) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    // Updated saveUserToFirebase method
    private void showDialogConfirmation(int id) {

            showToast("Data berhasil disimpan ke database dengan kode "+id+" !");
            resetAllEditText();


    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void resetAllEditText() {
        etBeratBadanBayi.setText("");
        etTinggiBadanBayi.setText("");
        etLingkarKepalaBayi.setText("");
        etStatusObatVaksinBayi.setText("");
        etRiwayatPenyakitBayi.setText("");
        etBeratBadanLansia.setText("");
        etTinggiBadanLansia.setText("");
        etLingkarKepalaLansia.setText("");
        etLingkarPerutLansia.setText("");
        etStatusObatLansia.setText("");
        etRiwayatPenyakitLansia.setText("");
        etBeratBadanBumil.setText("");
        etTinggiBadanBumil.setText("");
        etLingkarKepalaBumil.setText("");
        etLingkarLenganBumil.setText("");
        etStatusObatVaksinBumil.setText("");
        etRiwayatPenyakitBumil.setText("");

    }

    @Override
    public void onClick(View v) {
        // Dapatkan aksi klik jika diperlukan
        if (v.getId() == ivHome.getId()) {
            intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish(); // Menghancurkan aktivitas saat ini
        } else if (v.getId() == ivReminder.getId()) {
            intent = new Intent(this, PengingatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish(); // Menghancurkan aktivitas saat ini
        } else if (v.getId() == ivSettings.getId()) {
            intent = new Intent(this, DataIbuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish(); // Menghancurkan aktivitas saat ini
        } else if (v.getId() == ivProfile.getId()) {
            intent = new Intent(this, EdukasiBumilActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish(); // Menghancurkan aktivitas saat ini
        }
    }

}
