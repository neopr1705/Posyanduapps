package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.LoginActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.SubkategoriAdapter;
import com.example.posyanduapps.models.Subkategori;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointD;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ChartActivity extends Activity implements View.OnClickListener{
    private LineChart lineChart;
    private TextView tvTitle;
    private ImageView ivChoice,ivHome, ivReminder, ivProfile, ivSettings,ivChart;
    private int currentOption;
    private Intent intent;
    private XAxis xAxis;

    private Context context;
    // Menentukan warna-warna sesuai dengan warna dasar aplikasi
    // Mendapatkan warna dari resources secara langsung tanpa ContextCompat


    String url="https://posyanduapps-76c23-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initView();
        currentView();
        setChoiceImage();
        setupChart();
        populateData();
        setClickChart();
    }

    private void setClickChart() {
        // Mendapatkan referensi ke LineChart
        lineChart = findViewById(R.id.timeline_chart);

        // Menambahkan listener untuk mendeteksi ketika nilai chart dipilih
        lineChart.setOnTouchListener(new View.OnTouchListener() {
            private PopupWindow popupWindow;
            private boolean isPopupVisible = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float touchX = event.getX();
                float touchY = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isPopupVisible) {
                            showPopup(v, touchX, touchY);
                            isPopupVisible = true;
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (isPopupVisible) {
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                            }
                            isPopupVisible = false;
                        }
                        return true;
                }
                return false;
            }

            private void showPopup(View anchorView, float touchX, float touchY) {
                if (popupWindow == null) {
                    // Inisialisasi PopupWindow jika belum ada
                    popupWindow = new PopupWindow(anchorView.getContext());
                    View popupView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.popup_chart, null);
                    popupWindow.setContentView(popupView);
                    popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setOutsideTouchable(false);
                    popupWindow.setFocusable(false);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

                // **Hitung posisi titik data terdekat**
                Highlight highlight = lineChart.getHighlightByTouchPoint(touchX, touchY); // Dapatkan highlight dari koordinat
                if (highlight != null) {
                    Entry entry = lineChart.getEntryByTouchPoint(touchX, touchY); // Dapatkan data yang terkait
                    MPPointD position = lineChart.getTransformer(YAxis.AxisDependency.LEFT)
                            .getPixelForValues(entry.getX(), entry.getY());
                    int popupX = (int) position.x; // Ambil nilai X dari MPPointD
                    int popupY = (int) position.y; // Ambil nilai Y dari MPPointD

// Jangan lupa untuk melepaskan MPPointD setelah digunakan
                    MPPointD.recycleInstance(position);


                    // Atur teks atau data popup (jika diperlukan)
                    TextView popupText = popupWindow.getContentView().findViewById(R.id.popup_text);
                    popupText.setText(xAxis.getFormattedLabel((int)entry.getX())+"\nValue: " + entry.getY());

                    View chartView = findViewById(R.id.timeline_chart);
                    int maxHeight = chartView.getHeight();

                    popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                    popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                    // Batasi tinggi popup
                    popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    int popupHeight = popupWindow.getContentView().getMeasuredHeight();
                    if (popupHeight > maxHeight) {
                        popupWindow.setHeight(maxHeight); // Atur tinggi maksimal
                    }



// Tampilkan popup di lokasi yang diperoleh
                    popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, popupX, Math.max(popupY - popupHeight, chartView.getTop()));
                }
            }
        });
    }

        private String formatDate(float x) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) x * 1000); // Jika x adalah timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }



    private void setupChart() {
        // Set chart background and basic configurations
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getDescription().setEnabled(false);
        lineChart.setExtraOffsets(5, 10, 5, 15);

        // Customize X-Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.DKGRAY);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // Ensure timeline consistency

        // Customize Y-Axis
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTextColor(Color.DKGRAY);
        yAxisLeft.setDrawGridLines(false);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        // Set Animation
        lineChart.animateX(1000); // Smooth loading animation
    }

    private void initDb(){
        SharedPreferences userid = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String uid = userid.getString("BucketUser","");
        databaseReference = FirebaseDatabase.getInstance(url).getReference("users").child(uid).child("data_user");
        Log.d("Firebase",databaseReference.toString());
    }

    private void syncData() {
        if(currentOption==1){
            databaseReference=databaseReference.child("bayi");
        }else if(currentOption==2){
            databaseReference=databaseReference.child("lansia");
        }else if(currentOption==3){
            databaseReference=databaseReference.child("bumil");
        }else{

        }
    }


    private void populateData() {
        initDb();
        syncData();
        Log.d("Firebase",databaseReference.toString());

        ArrayList<Entry> entriesberatbadan = new ArrayList<>();
        ArrayList<Entry> entriestinggibadan = new ArrayList<>();
        ArrayList<Entry> entrieslingkarKepala = new ArrayList<>();
        ArrayList<Entry> entrieslingkarperut = new ArrayList<>();
        ArrayList<Entry> entrieslingkarlengan = new ArrayList<>();
        ArrayList<String> dateLabels = new ArrayList<>(); // Untuk menyimpan label tanggal



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterasi setiap entry pada kategori "bayi"
                int index = 0;  // Ini digunakan untuk X-Axis (Tanggal) sebagai indeks
                for (DataSnapshot recordSnapshot : dataSnapshot.getChildren()) {
                    if (currentOption==1&&recordSnapshot.child("berat_badan").exists() && recordSnapshot.child("tinggi_badan").exists() && recordSnapshot.child("lingkar_kepala").exists()) {
                        String tanggal = recordSnapshot.child("tanggal").getValue(String.class);
                        String beratBadan = recordSnapshot.child("berat_badan").getValue(String.class);
                        String tinggiBadan = recordSnapshot.child("tinggi_badan").getValue(String.class);
                        String lingkarKepala = recordSnapshot.child("lingkar_kepala").getValue(String.class);
                        int beratBadanInt = Integer.parseInt(beratBadan);
                        int tinggiBadanInt = Integer.parseInt(tinggiBadan);
                        int lingkarKepalaInt = Integer.parseInt(lingkarKepala);
                        int berat =beratBadanInt+tinggiBadanInt+lingkarKepalaInt;
                        Log.d("data",index+":"+beratBadanInt+","+tinggiBadanInt+","+lingkarKepalaInt);


                        // Menambahkan data ke ArrayList untuk grafik
                        // X-Axis menggunakan indeks yang bertambah
                        entriesberatbadan.add(new Entry(index,beratBadanInt));
                        entriestinggibadan.add(new Entry(index,tinggiBadanInt));
                        entrieslingkarKepala.add(new Entry(index,lingkarKepalaInt));
                        dateLabels.add(tanggal);

                        // Menambahkan tanggal (untuk informasi) jika diperlukan untuk label di grafik
                        System.out.println("Tanggal: " + tanggal);
                        System.out.println("Berat Badan: " + beratBadan + ", Tinggi Badan: " + tinggiBadan + ", Lingkar Kepala: " + lingkarKepala);

                        index++;
                    }
                    else if(currentOption==2&&recordSnapshot.child("berat_badan").exists() && recordSnapshot.child("tinggi_badan").exists() && recordSnapshot.child("lingkar_perut").exists()){
                        String tanggal = recordSnapshot.child("tanggal").getValue(String.class);
                        String beratBadan = recordSnapshot.child("berat_badan").getValue(String.class);
                        String tinggiBadan = recordSnapshot.child("tinggi_badan").getValue(String.class);
                        String lingkarKepala = recordSnapshot.child("lingkar_kepala").getValue(String.class);
                        String lingkarperut = recordSnapshot.child("lingkar_perut").getValue(String.class);
                        int beratBadanInt = Integer.parseInt(beratBadan);
                        int tinggiBadanInt = Integer.parseInt(tinggiBadan);
                        int lingkarKepalaInt = Integer.parseInt(lingkarKepala);
                        int lingkarperutInt = Integer.parseInt(lingkarperut);
                        Log.d("data",index+":"+beratBadanInt+","+tinggiBadanInt+","+lingkarKepalaInt);


                        // Menambahkan data ke ArrayList untuk grafik
                        // X-Axis menggunakan indeks yang bertambah
                        entriesberatbadan.add(new Entry(index,beratBadanInt));
                        entriestinggibadan.add(new Entry(index,tinggiBadanInt));
                        entrieslingkarKepala.add(new Entry(index,lingkarKepalaInt));
                        entrieslingkarperut.add(new Entry(index,lingkarperutInt));
                        dateLabels.add(tanggal);

                        // Menambahkan tanggal (untuk informasi) jika diperlukan untuk label di grafik
                        System.out.println("Tanggal: " + tanggal);
                        System.out.println("Berat Badan: " + beratBadan + ", Tinggi Badan: " + tinggiBadan + ", Lingkar Kepala: " + lingkarKepala);

                        index++;
                    } else if (currentOption==3&&recordSnapshot.child("berat_badan").exists() && recordSnapshot.child("tinggi_badan").exists() && recordSnapshot.child("lingkar_lengan").exists()) {
                        String tanggal = recordSnapshot.child("tanggal").getValue(String.class);
                        String beratBadan = recordSnapshot.child("berat_badan").getValue(String.class);
                        String tinggiBadan = recordSnapshot.child("tinggi_badan").getValue(String.class);
                        String lingkarKepala = recordSnapshot.child("lingkar_kepala").getValue(String.class);
                        String lingkarlengan = recordSnapshot.child("lingkar_lengan").getValue(String.class);
                        int beratBadanInt = Integer.parseInt(beratBadan);
                        int tinggiBadanInt = Integer.parseInt(tinggiBadan);
                        int lingkarKepalaInt = Integer.parseInt(lingkarKepala);
                        int lingkarlenganInt = Integer.parseInt(lingkarlengan);
                        Log.d("data",index+":"+beratBadanInt+","+tinggiBadanInt+","+lingkarKepalaInt);


                        // Menambahkan data ke ArrayList untuk grafik
                        // X-Axis menggunakan indeks yang bertambah
                        entriesberatbadan.add(new Entry(index,beratBadanInt));
                        entriestinggibadan.add(new Entry(index,tinggiBadanInt));
                        entrieslingkarKepala.add(new Entry(index,lingkarKepalaInt));
                        entrieslingkarlengan.add(new Entry(index,lingkarlenganInt));
                        dateLabels.add(tanggal);

                        // Menambahkan tanggal (untuk informasi) jika diperlukan untuk label di grafik
                        System.out.println("Tanggal: " + tanggal);
                        System.out.println("Berat Badan: " + beratBadan + ", Tinggi Badan: " + tinggiBadan + ", Lingkar Kepala: " + lingkarKepala);

                        index++;
                    }else{
                        Toast.makeText(ChartActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();

                    }
                }

                // Menampilkan isi dari ArrayList<Entry> entriesberatbadan
                for (Entry entry : entriesberatbadan) {
                    Log.d("datagrafik", "X: " + entry.getX() + ", Y: " + entry.getY());
                }

// Menampilkan isi dari ArrayList<Entry> entriestinggibadan
                for (Entry entry : entriestinggibadan) {
                    Log.d("datagrafik", "X: " + entry.getX() + ", Y: " + entry.getY());
                }

// Menampilkan isi dari ArrayList<Entry> entrieslingkarKepala
                for (Entry entry : entrieslingkarKepala) {
                    Log.d("datagrafik", "X: " + entry.getX() + ", Y: " + entry.getY());
                }

// Menampilkan isi dari ArrayList<Entry> entrieslingkarperut
                for (Entry entry : entrieslingkarperut) {
                    Log.d("datagrafik", "X: " + entry.getX() + ", Y: " + entry.getY());
                }

// Menampilkan isi dari ArrayList<Entry> entrieslingkarlengan
                for (Entry entry : entrieslingkarlengan) {
                    Log.d("datagrafik", "X: " + entry.getX() + ", Y: " + entry.getY());
                }

// Menampilkan isi dari ArrayList<String> dateLabels
                for (String date : dateLabels) {
                    Log.d("datagrafik", "Tanggal: " + date);
                }



                //tambah penggambaran data disini
                int colorPrimary = getResources().getColor(R.color.colorPrimary); // Merah muda terang
                int colorAccent = getResources().getColor(R.color.colorAccent); // Merah muda cerah
                int colorlp = getResources().getColor(R.color.primaryDark); // Merah muda medium
                int color3rd = getResources().getColor(R.color.color3rd); // Merah muda medium
                int color4th = getResources().getColor(R.color.color4th); // Pink terang
                int blackColor = getResources().getColor(R.color.black); // Hitam
                int whiteColor = getResources().getColor(R.color.selected_button_color); // Putih


// Dataset utama untuk Berat Badan
                LineDataSet dataSet = new LineDataSet(entriesberatbadan, "Berat Badan");
                dataSet.setColor(colorPrimary); // Merah muda terang
                dataSet.setValueTextColor(blackColor); // Menghilangkan teks dari nilai
                dataSet.setCircleRadius(5f);
                dataSet.setCircleColor(colorAccent); // Merah muda cerah
                dataSet.setLineWidth(3f); // Garis sedikit lebih tebal
                dataSet.setDrawFilled(false); // Menghilangkan fill area bawah garis
                dataSet.setDrawValues(false); // Menghilangkan teks dari nilai

// Dataset utama untuk Tinggi Badan
                LineDataSet dataSetTb = new LineDataSet(entriestinggibadan, "Tinggi Badan");
                dataSetTb.setColor(color3rd); // Merah muda medium
                dataSetTb.setValueTextColor(blackColor); // Menghilangkan teks dari nilai
                dataSetTb.setCircleRadius(5f);
                dataSetTb.setCircleColor(color4th); // Pink terang
                dataSetTb.setLineWidth(3f); // Garis sedikit lebih tebal
                dataSetTb.setDrawFilled(false); // Menghilangkan fill area bawah garis
                dataSetTb.setDrawValues(false); // Menghilangkan teks dari nilai


// Dataset utama untuk Lingkar Kepala
                LineDataSet dataSetlk = new LineDataSet(entrieslingkarKepala, "Lingkar Kepala");
                dataSetlk.setColor(whiteColor); // Putih
                dataSetlk.setValueTextColor(blackColor); // Menghilangkan teks dari nilai
                dataSetlk.setCircleRadius(5f);
                dataSetlk.setCircleColor(colorPrimary); // Merah muda terang
                dataSetlk.setLineWidth(3f); // Garis sedikit lebih tebal
                dataSetlk.setDrawFilled(false); // Menghilangkan fill area bawah garis
                dataSetlk.setDrawValues(false); // Menghilangkan teks dari nilai


// Dataset utama untuk Lingkar Perut
                LineDataSet dataSetlp = new LineDataSet(entrieslingkarperut, "Lingkar Perut");
                dataSetlp.setColor(colorlp); // Merah muda cerah
                dataSetlp.setValueTextColor(blackColor); // Menghilangkan teks dari nilai
                dataSetlp.setCircleRadius(5f);
                dataSetlp.setCircleColor(color3rd); // Merah muda medium
                dataSetlp.setLineWidth(3f); // Garis sedikit lebih tebal
                dataSetlp.setDrawFilled(false); // Menghilangkan fill area bawah garis
                dataSetlp.setDrawValues(false); // Menghilangkan teks dari nilai

// Dataset utama untuk Lingkar Lengan
                LineDataSet dataSetll = new LineDataSet(entrieslingkarlengan, "Lingkar Lengan");
                dataSetll.setColor(colorlp); // Pink terang
                dataSetll.setValueTextColor(blackColor); // Menghilangkan teks dari nilai
                dataSetll.setCircleRadius(6f); // Radius lingkaran yang sedikit lebih besar
                dataSetll.setCircleColor(color3rd); // Merah muda medium untuk lingkaran
                dataSetll.setLineWidth(3f); // Ketebalan garis sedikit lebih besar
                dataSetll.setDrawFilled(false); // Menghilangkan fill area bawah garis
                dataSetll.setDrawValues(false); // Menghilangkan teks dari nilai




                // Gabungkan kedua dataset ke dalam satu LineData
                LineData lineData = new LineData();

                if (currentOption==1){
                    lineData.addDataSet(dataSet);   // Tambahkan dataset 1
                    lineData.addDataSet(dataSetTb); // Tambahkan dataset 2
                    lineData.addDataSet(dataSetlk);
                } else if (currentOption==2) {
                    lineData.addDataSet(dataSet);   // Tambahkan dataset 1
                    lineData.addDataSet(dataSetTb); // Tambahkan dataset 2
                    lineData.addDataSet(dataSetlk);
                    lineData.addDataSet(dataSetlp);
                } else if (currentOption==3) {
                    lineData.addDataSet(dataSet);   // Tambahkan dataset 1
                    lineData.addDataSet(dataSetTb); // Tambahkan dataset 2
                    lineData.addDataSet(dataSetlk);
                    lineData.addDataSet(dataSetll);
                }

                xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));

                // Tampilkan data ke LineChart
                lineChart.setData(lineData);
                lineChart.invalidate(); // Refresh chart
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error fetching data: " + databaseError.getMessage());
            }
        });

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
    public void currentView(){
        ivChart.setColorFilter(getResources().getColor(R.color.softBlue));  // Mengubah tint menjadi warna hitam
    }

    public void initView(){
        // Inisialisasi HeaderIconHelper
        View headerLayout = findViewById(R.id.header_layout);
        new HeaderIconHelper(this, headerLayout);
        tvTitle = findViewById(R.id.tvTitle);
        ivChoice = findViewById(R.id.ivChoice);
        tvTitle.setText("Grafik Timeline");
        SharedPreferences sharedPreferences = getSharedPreferences("Option", MODE_PRIVATE);
        currentOption = sharedPreferences.getInt("currentOption",-1);

        //Inisialisasi Footer
        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);
        ivChart = findViewById(R.id.ivChart);
        ivProfile = findViewById(R.id.ivProfile);
        ivSettings = findViewById(R.id.ivSettings);
        ivHome.setOnClickListener(this);
        ivReminder.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
        ivChart.setOnClickListener(this);

        //inisialisasi konten
        lineChart = findViewById(R.id.timeline_chart);

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
        else if(v.getId()==ivChart.getId()){
            intent = new Intent(this, ChartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish(); // Menghancurkan aktivitas saat ini

        }
    }
}
