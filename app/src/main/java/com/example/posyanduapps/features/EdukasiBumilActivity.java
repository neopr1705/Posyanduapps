package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.LoginActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.SubkategoriAdapter;
import com.example.posyanduapps.models.Subkategori;

import java.util.ArrayList;
import java.util.List;

public class EdukasiBumilActivity extends Activity implements View.OnClickListener {

    private RecyclerView recyclerViewSubkategori;
    private SubkategoriAdapter subkategoriAdapter;
    private ImageView ivHome, ivReminder, ivProfile, ivSettings;
    private Intent intent;
    private TextView tvTitle;
    private int currentOption;
    private ImageView ivChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edukasi);
        View headerLayout = findViewById(R.id.header_layout);
        // Inisialisasi HeaderIconHelper
        new HeaderIconHelper(this, headerLayout);
        tvTitle = findViewById(R.id.tvTitle);
        ivChoice = findViewById(R.id.ivChoice);
        SharedPreferences sharedPreferences = getSharedPreferences("Option", MODE_PRIVATE);
        currentOption = sharedPreferences.getInt("currentOption",-1);
        if (currentOption ==3) {
            tvTitle.setText(getText(R.string.str_EduGizi));
        }else if(currentOption ==1){
            tvTitle.setText(getText(R.string.str_EduBayi));
        }else if(currentOption ==2){
            tvTitle.setText(getText(R.string.str_EduLansia));
        }else{
            tvTitle.setText("Mom Care");
        }
        setChoiceImage();
        //footer
        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);

        ivProfile = findViewById(R.id.ivProfile);
        ivSettings = findViewById(R.id.ivSettings);

        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);

        recyclerViewSubkategori = findViewById(R.id.recyclerViewSubkategori);
        recyclerViewSubkategori.setLayoutManager(new GridLayoutManager(this, 1));
        ivProfile.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
        // Initialize adapter and data
        if(currentOption == 3){
        subkategoriAdapter = new SubkategoriAdapter(getSubkategoriData());
        recyclerViewSubkategori.setAdapter(subkategoriAdapter);
        }else if(currentOption == 1){
            subkategoriAdapter = new SubkategoriAdapter(getSubkategoriDataBayi());
            recyclerViewSubkategori.setAdapter(subkategoriAdapter);
        }else if(currentOption == 2){
            subkategoriAdapter = new SubkategoriAdapter(getSubkategoriDataLansia());
            recyclerViewSubkategori.setAdapter(subkategoriAdapter);
        }else{
            Intent intent = new Intent(EdukasiBumilActivity.this, DashboardActivity.class);
            Toast.makeText(this, "Error Load Choice, move to Dashboard", Toast.LENGTH_LONG).show();
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
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

    private List<Subkategori> getSubkategoriData() {
        List<Subkategori> subkategoriList = new ArrayList<>();
        subkategoriList.add(new Subkategori("PERAWATAN SEHARI-HARI IBU HAMIL",
                "Panduan lengkap perawatan sehari-hari ibu hamil untuk menjaga kesehatan dan kenyamanan selama masa kehamilan",
                R.drawable.perawatan_sehari_hari));
        subkategoriList.add(new Subkategori("YANG HARUS DIHINDARI IBU HAMIL",
                "Hal-hal yang perlu dihindari oleh ibu hamil demi menjaga kesehatan ibu dan janin selama masa kehamilan",
                R.drawable.warning_100dp_e8eaed_fill0_wght400_grad0_opsz48));
        subkategoriList.add(new Subkategori("PORSI MAKAN DAN MINUM IBU HAMIL",
                "Panduan tentang porsi makan dan minum yang seimbang untuk ibu hamil agar mendapatkan nutrisi yang cukup",
                R.drawable.ic_makan_edukasi));
        subkategoriList.add(new Subkategori("AKTIVITAS FISIK DAN LATIHAN FISIK IBU HAMIL",
                "Jenis aktivitas fisik dan latihan yang aman untuk ibu hamil guna menjaga kebugaran tubuh dan persiapan persalinan",
                R.drawable.aktivitas));
        subkategoriList.add(new Subkategori("TANDA BAHAYA KEHAMILAN",
                "Tanda-tanda bahaya yang perlu segera diperhatikan oleh ibu hamil agar dapat mendapatkan penanganan medis tepat waktu",
                R.drawable.tanda_bahaya));
        subkategoriList.add(new Subkategori("MASALAH LAIN PADA KEHAMILAN",
                "Masalah-masalah kesehatan lain yang sering terjadi selama kehamilan dan cara mengatasinya untuk kesehatan ibu dan bayi",
                R.drawable.masalah_lain));
        subkategoriList.add(new Subkategori("PERSIAPAN MELAHIRKAN",
                "Panduan persiapan melahirkan untuk ibu hamil agar proses persalinan berjalan lancar dan meminimalkan risiko komplikasi",
                R.drawable.persiapan_melahirkan));
        subkategoriList.add(new Subkategori("TANDA AWAL PERSALINAN",
                "Tanda-tanda awal persalinan yang harus dikenali oleh ibu hamil untuk mempersiapkan diri menghadapi kelahiran",
                R.drawable.tandawal));

        // Add more categories as needed
        return subkategoriList;
    }

    private List<Subkategori> getSubkategoriDataBayi() {
        List<Subkategori> subkategoriList = new ArrayList<>();
        subkategoriList.add(new Subkategori("Balita sehat untuk generasi emas",
                "Panduan lengkap untuk memastikan bayi Anda tumbuh sehat dan kuat. Termasuk cara menjaga kebersihan tubuh bayi, mengganti popok dengan benar, serta menjaga kesehatan secara keseluruhan demi mempersiapkan generasi emas yang kuat.",
                R.drawable.icon_baby_1));
        subkategoriList.add(new Subkategori("Aktivitas Balitaku",
                "Memahami pentingnya pola tidur yang sehat bagi bayi Anda dan cara menciptakan lingkungan yang nyaman untuk mendukung tumbuh kembang bayi secara optimal.",
                R.drawable.icon_baby_2));

        return subkategoriList;
    }

    private List<Subkategori> getSubkategoriDataLansia() {
        List<Subkategori> subkategoriList = new ArrayList<>();
        subkategoriList.add(new Subkategori("Jaga Tekanan Darah, Jauhkan Stroke!",
                "Hilangnya aliran darah ke bagian otak, yang merusak jaringan otak. Stroke disebabkan oleh bekuan darah dan pecahnya pembuluh darah di otak",
                R.drawable.elder_1));
        subkategoriList.add(new Subkategori("Atur Pola Hidup Sehat",
                "Panduan menyeluruh untuk menjaga keseimbangan fisik dan mental di usia lanjut, termasuk tips pola makan sehat dan aktivitas fisik.",
                R.drawable.elder_3));
        subkategoriList.add(new Subkategori("Bye Bye Diabetes!",
                "Informasi penting untuk mengenali gejala awal dan tanda bahaya diabetes, terutama bagi lansia.",
                R.drawable.elder_2));
        return subkategoriList;
    }

    @Override
    public void onClick(View v) {
        // Dapatkan aksi klik jika diperlukan
        if(v.getId() == ivHome.getId()){
            intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        else if (v.getId() == ivReminder.getId()) {
            intent = new Intent(this, PengingatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        else if (v.getId() == ivSettings.getId()) {
            intent = new Intent(this, DataIbuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        else if (v.getId() == ivProfile.getId()) {
            intent = new Intent(this, EdukasiBumilActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }
}
