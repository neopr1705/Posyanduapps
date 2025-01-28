package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.posyanduapps.Helper.HeaderIconHelper;
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
    private int currentOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_subkategori);

        // Get data from intent
        String subkategoriNama = getIntent().getStringExtra("subkategori_nama");
        intialize();
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
        if(currentOption ==1){
            // Set adapter untuk RecyclerView
            DynamicContentAdapter adapter = new DynamicContentAdapter(this, getContentBySubkategoriBayi(subkategoriNama));
            recyclerView.setAdapter(adapter);
        } else if (currentOption==2) {
            // Set adapter untuk RecyclerView
            DynamicContentAdapter adapter = new DynamicContentAdapter(this, getContentBySubkategoriLansia(subkategoriNama));
            recyclerView.setAdapter(adapter);
        } else if(currentOption==3) {
            // Set adapter untuk RecyclerView
            DynamicContentAdapter adapter = new DynamicContentAdapter(this, getContentBySubkategori(subkategoriNama));
            recyclerView.setAdapter(adapter);
        }
//        // Set content based on the subcategory
//        TextView contentText = findViewById(R.id.tvContent);
//        contentText.setText(getContentBySubkategori(subkategoriNama));
    }

    private void intialize(){
        SharedPreferences sharedPreferences = getSharedPreferences("Option", MODE_PRIVATE);
        currentOption = sharedPreferences.getInt("currentOption",-1);
        View headerLayout = findViewById(R.id.header_layout);
        new HeaderIconHelper(this,headerLayout);
        ImageView ivLogout = headerLayout.findViewById(R.id.ivLogout);
        ivLogout.setVisibility(View.GONE);

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

    private DynamicContent getContentBySubkategoriBayi(String subkategoriNama) {
        switch (subkategoriNama) {
            case "Balita sehat untuk generasi emas":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "Waspada Stunting dan Gizi Buruk:\n\n" +
                                        "1. Pastikan anak mendapatkan gizi yang cukup dan seimbang agar tumbuh kembangnya optimal\n" +
                                        "2. Stunting bisa berdapmapak pada pertumbuhan fisik dan kecerdasan si kecil.",
                                "Vitamin dan Gizi\n\n" +
                                        "1. Vitamin A:\t\tWortel dan Bayam.\n" +
                                        "2. Vitamun C:\tJeruk dan Tomat.\n" +
                                        "3. Vitamin D:\t\tIkan dan Telur.\n" +
                                        "4. Kalsium :\tSusu dan Keju.\n" +
                                        "5. Zat Besi :\tDaging dan Ayam."

                        ),
                        Arrays.asList(
                                R.drawable.balita1_2,
                                R.drawable.balita1_3

                        )
                );

                return dynamicContent;

            case "Aktivitas Balitaku":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "Bermain sambil Belajar\n\n" +
                                        "1. Puzzle.\n" +
                                        "2. Balok.\n" +
                                        "3. Permainan Peran\n",
                                "Cerita & Lagu\n\n" +
                                        "1. Buku Cerita.\n" +
                                        "2. Bergambar.\n" +
                                        "3. Lagu Edukatif.\n",
                                "Makanan Seimbang\n\n" +
                                        "1. Sayuran.\n" +
                                        "2. Buah Buahan.\n" +
                                        "3. Protein.\n" +
                                        "4. Karbohidrat",
                                "Eksplorasi Alam\n\n" +
                                        "1. Mengenal Tumbuhan.\n" +
                                        "2. Mengenal Hewan.\n" +
                                        "3. Mengenal Cuaca",
                                "Sosial & Kemandirian\n\n" +
                                        "1. Bermain Bersama.\n" +
                                        "2. Belajar Berbagi."
                                       ),
                        Arrays.asList(
                                R.drawable.balita2_1,
                                -1,
                                R.drawable.balita2_2,
                                -1,
                                R.drawable.edukasi_balita2_2
                        )
                );

                return dynamicContent;

            default:
                // Jika kategori tidak ditemukan, kembalikan pesan default
                dynamicContent = new DynamicContent(
                        Arrays.asList("Data Edukasi Balita Tidak Ditemukan"),
                        Arrays.asList(R.drawable.masalah_lain)
                );
                // Kembalikan panduan perawatan sehari-hari ibu hamil
                return dynamicContent;
        }
    }

    private DynamicContent getContentBySubkategoriLansia(String subkategoriNama) {
        switch (subkategoriNama) {
            case "Jaga Tekanan Darah, Jauhkan Stroke!":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "\n1. Mati rasa, kelemahan, atau lumpuh di wajah, lengan, atau kaki, terutama di satu sisi tubuh.\n"+
                                "2. Sakit kepala hebat yang belum pernah dirasakan sebelumnya turut menjadi tanda timbulnya penyakit stroke. Rasa pusing sebagai gejala stroke disertai dengan tremor hingga sempoyongan.",
                                "\n1. Pola makan sehat dan rutin olahraga\n" +
                                "2. Rutin periksan tekanan darah dan menghindari makanan asin atau bergaram.",
                                "\n1. Tekanan darah tinggi (hipertensi), Diabetes, Kolesterol tinggi, Kebiasaan merokok.\n" +
                                        "2. Konsumsi alkohol berlebihan. Pola makan tidak sehat (tinggi garam, lemak, dan gula)."
                                ),
                        Arrays.asList(
                                R.drawable.lansia1_1,
                                R.drawable.lansia1_2,
                                R.drawable.lansia1_3
                        )
                );

                return dynamicContent;

            case "Bye Bye Diabetes!":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "Mempertahankan Berat Badan Ideal\n" +
                                        "Kelebihan berat badan dapat meningkatkan resistensi insulin, sehingga menjaga berat badan ideal menjadi langkah awal yang krusial.\n\n" +
                                        "olahraga minimal 2-3x seminggu\n" +
                                        "Aktivitas fisik secara teratur membantu mengontrol kadar gula darah, meningkatkan sensitivitas insulin, dan mendukung penurunan berat badan",
                                "Mengelola Stress dengan Baik\n" +
                                        "Stres dapat memicu pelepasan hormon stres yang dapat meningkatkan gula darah. Strategi mengelola stres, seperti meditasi, yoga, atau hobi yang menyenangkan.\n\n" +
                                        "Makan Makanan yang Sehat\n" +
                                        "Pola makan yang sehat dengan memperbanyak serat, sayuran, buah-buahan, dan mengurangi konsumsi gula serta lemak jenuh dapat membantu menjaga kadar gula darah dalam batas normal"

                        ),
                        Arrays.asList(
                                R.drawable.lansia_2_1,
                                R.drawable.lansia_2_2
                        )
                );

                return dynamicContent;

            case "Atur Pola Hidup Sehat":
                dynamicContent = new DynamicContent(
                        Arrays.asList(
                                "\n1. Batasi konsumsi gula. garam, dan lemak secara berlebihan\n" +
                                        "2. Rutin melakukan aktifitas fisik 30 menit sehari\n" +
                                        "3. Tidak merokok atau terpapar asap rokok dan residu rokok\n" +
                                        "4. Jaga berat bdan ideal dan cegah obesitas"

                        ),
                        Arrays.asList(
                                R.drawable.lansia3_1

                        )
                );

                return dynamicContent;

            default:
                // Jika kategori tidak ditemukan, kembalikan pesan default
                dynamicContent = new DynamicContent(
                        Arrays.asList("Data Edukasi Lansia Tidak Ditemukan"),
                        Arrays.asList(R.drawable.masalah_lain)
                );
                // Kembalikan panduan perawatan sehari-hari ibu hamil
                return dynamicContent;
        }
    }
}
