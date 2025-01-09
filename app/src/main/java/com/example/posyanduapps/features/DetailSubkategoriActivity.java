package com.example.posyanduapps.features;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.posyanduapps.R;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.posyanduapps.adapters.DynamicContentAdapter;
import com.example.posyanduapps.models.DynamicContent;

import java.util.Arrays;
public class DetailSubkategoriActivity extends Activity {
    DynamicContent dynamicContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_subkategori);

        // Get data from intent
        String subkategoriNama = getIntent().getStringExtra("subkategori_nama");

        // Set the title manually (TextView as title)
        TextView toolbarTitle = findViewById(R.id.tvTitle);
        toolbarTitle.setText(subkategoriNama);
        // Inisialisasi tombol
        Button btnBack = findViewById(R.id.btnBack);

        // Menambahkan fungsi untuk tombol kembali
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup activity saat tombol ditekan
            }
        });

        // Ambil RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Set adapter untuk RecyclerView
        DynamicContentAdapter adapter = new DynamicContentAdapter(this, getContentBySubkategori(subkategoriNama));
        recyclerView.setAdapter(adapter);

//        // Set content based on the subcategory
//        TextView contentText = findViewById(R.id.tvContent);
//        contentText.setText(getContentBySubkategori(subkategoriNama));
    }

    private DynamicContent getContentBySubkategori(String subkategoriNama) {

        switch (subkategoriNama) {
            case "PERAWATAN SEHARI-HARI IBU HAMIL":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "1. Makan beragam makanan secara proporsional.\nDengan pola gizi seimbang dan 1 porsi lebih banyak daripada sebelum hamil.\n\nSerta imbangi dengan minum TTD(Tablet Tambah darah) satu tablet setiap hari selama kehamilannya.",
                                "2. Istirahat yang cukup.\n - Tidur malam sedikitnya 6-7jam.\n - Siang hari usahakan tidur atau berbaring terlentang 1 - 2 jam.",
                                "3. Menjaga Kebersihan Diri.\n - Cuci tangan dengan sabun dan menggunakan air bersih mengalir.\n - Mandi dan gosok gigi 2 kali sehari.\n - Keramas / cuci rambut 2 hari sekali.\n - Jaga kebersihan payudara dan daerah kemaluan.\n - Ganti pakaian dan pakaian dalam setiap hari\n - Periksa gigi.",
                                "4. Bersama suami lakukan stimulasi janin.\nDengan cara, sering berbicara dengan janin, dan sering lakukan sentuhan pada perut ibu."
                        ,       "5. Hubungan suami istri selama hamil boleh dilakukan, selama kehamilan sehat."
                        ),
                        Arrays.asList(
                                R.drawable.edu_1_1,
                                R.drawable.perawatan_2,
                                R.drawable.perawatan_3,
                                R.drawable.perawatan_4,
                                -1
                                )
                );

                return dynamicContent;

            case "YANG HARUS DIHINDARI IBU HAMIL":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "1. Kerja berat",
                                "2. Merokok atau terpapar asap rokok",
                                "3. Minum minuman bersoda, beralkohol dan jamu",
                                "4. Tidur terlentang > 10 menit pada masa hamil tua untuk menghindari kekurangan oksigen pada janin"
                        ,       "5. Ibu hamil minum obat tanpa resep dokter",
                                "6. Stress berlebihan",
                                "Tanyakan kepada Bidan/Perawat/Dokter untuk penjelasan lebih lanjut terkait kehamilan"
                        ),
                        Arrays.asList(
                                R.drawable.edu_2_1,
                                R.drawable.edu_2_2,
                                R.drawable.edu_2_3,
                                R.drawable.edu_2_4,
                                R.drawable.edu_2_5,
                                R.drawable.edu_2_6,
                                -1
                                )
                );

                return dynamicContent;

            case "PORSI MAKAN DAN MINUM IBU HAMIL":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "nodata",
                                "nodata",
                                "nodata",
                                "Minum Air Putih: 8-12 Gelas Per hari\nCatatan:\nKonsultasikan porsi makan kepada tenaga kesehatan, perhatikan indeks massa tubuh"

                        ),
                        Arrays.asList(
                                R.drawable.edu_3_1,
                                R.drawable.edu_3_2,
                                R.drawable.edu_3_3,
                                -1

                        )
                );

                return dynamicContent;
            case "AKTIVITAS FISIK DAN LATIHAN FISIK IBU HAMIL":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "nodata",
                                "nodata",
                                "nodata"
                        ),
                        Arrays.asList(
                                R.drawable.edu_4_1,
                                R.drawable.edu_4_2,
                                R.drawable.edu_4_3
                        )
                );

                return dynamicContent;
            case "TANDA BAHAYA KEHAMILAN":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "nodata"

                        ),
                        Arrays.asList(
                                R.drawable.edu_5_1
                        )
                );

                return dynamicContent;
            case "MASALAH LAIN PADA KEHAMILAN":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "nodata",
                                "nodata"
                        ),
                        Arrays.asList(
                                R.drawable.edu_6_1,
                                R.drawable.edu_6_2
                        )
                );

                return dynamicContent;
            case "PERSIAPAN MELAHIRKAN":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "1. Tanyakan kepada bidan dan dokter tanggal perkiraan persalinan\nSuami atau keluarga mendampingi ibu saat periksa kehamilan.",
                                "2. Persiapkan tabungan atau dana cadangan untuk biaya persalinan dan biaya lainnya.",
                                "3. Rencanakan melahirkan ditolong oleh dokter atau bidan difasilitas kesehatan.",
                                "4. Siapkan KTP, Kartu Keluarga, dan keperluan lainnya untuk ibu dan bayi yang akan dilahirkan.",
                                "5. Siapkan lebih dari satu orang yang memiliki golongan darah yang sama dan bersedia menjadi pendonor jika diperlukan.",
                                "6. Suami, keluarga, dan masyarakat, menyiapkan kendaraan jika sewaktu-waktu diperlukan.",
                                "7. Pastikan ibu hamil dan keluarga menyepakati amanat persalinan.",
                                "8. Rencanakan ikut ber-KB setelah melahirkan, tanyakan ke petugas kesehatan tentang cara ber-KB."

                        ),
                        Arrays.asList(
                                R.drawable.edu_7_1,
                                R.drawable.edu_7_2,
                                R.drawable.edu_7_3,
                                R.drawable.edu_7_4,
                                R.drawable.edu_7_5,
                                R.drawable.edu_7_6,
                                R.drawable.edu_7_7,
                                R.drawable.edu_7_8
                        )
                );

                return dynamicContent;

                case "TANDA AWAL PERSALINAN":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "1. Perut mulas-mulas yang teratur, timbulnya semakin sering dan semakin lama",
                                "2. Keluar lendir bercampur darah dari jalan lahir atau keluar cairan ketuban dari jalan lahir",
                                ""
                        ),
                        Arrays.asList(
                                R.drawable.edu_8_1,
                                R.drawable.edu_8_2,
                                R.drawable.edu_8_3
                        )
                );

                return dynamicContent;

            default:
                // Jika kategori tidak ditemukan, kembalikan pesan default
             dynamicContent = new DynamicContent(
                        Arrays.asList("Data Tidak Ditemukan"),
                        Arrays.asList(R.drawable.masalah_lain)
                );
            // Kembalikan panduan perawatan sehari-hari ibu hamil
            return dynamicContent;
        }
    }
}
