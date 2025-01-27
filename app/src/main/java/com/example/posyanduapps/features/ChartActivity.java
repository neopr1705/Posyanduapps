package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.LoginActivity;
import com.example.posyanduapps.R;
import com.example.posyanduapps.adapters.SubkategoriAdapter;
import com.example.posyanduapps.models.Subkategori;

import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChartActivity extends Activity implements View.OnClickListener{
    private LineChart lineChart;
    private TextView tvTitle;
    private ImageView ivChoice,ivHome, ivReminder, ivProfile, ivSettings,ivChart;
    private int currentOption;
    private Intent intent;

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
                int index = 1;  // Ini digunakan untuk X-Axis (Tanggal) sebagai indeks
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
                //tambah penggambaran data disini

                LineDataSet dataSet = new LineDataSet(entriesberatbadan, "Berat Badan");
                dataSet.setColor(Color.BLUE);
                dataSet.setValueTextColor(Color.DKGRAY);
                dataSet.setCircleRadius(5f); // Highlight points
                dataSet.setCircleColor(Color.RED);
                dataSet.setLineWidth(2f);

                LineDataSet dataSetTb = new LineDataSet(entriestinggibadan, "Tinggi Badan");
                dataSetTb.setColor(Color.MAGENTA);
                dataSetTb.setValueTextColor(Color.DKGRAY);
                dataSetTb.setCircleRadius(5f); // Highlight points
                dataSetTb.setCircleColor(Color.GREEN);
                dataSetTb.setLineWidth(2f);

                LineDataSet dataSetlk = new LineDataSet(entrieslingkarKepala, "Lingkar Kepala");
                dataSetlk.setColor(Color.CYAN);
                dataSetlk.setValueTextColor(Color.DKGRAY);
                dataSetlk.setCircleRadius(5f); // Highlight points
                dataSetlk.setCircleColor(Color.BLACK);
                dataSetlk.setLineWidth(2f);

                LineDataSet dataSetlp = new LineDataSet(entrieslingkarperut, "Lingkar Perut");
                dataSetlp.setColor(Color.YELLOW);
                dataSetlp.setValueTextColor(Color.DKGRAY);
                dataSetlp.setCircleRadius(5f); // Highlight points
                dataSetlp.setCircleColor(Color.BLACK);
                dataSetlp.setLineWidth(2f);

                LineDataSet dataSetll = new LineDataSet(entrieslingkarlengan, "Lingkar lengan");
                dataSetll.setColor(Color.YELLOW);
                dataSetll.setValueTextColor(Color.DKGRAY);
                dataSetll.setCircleRadius(5f); // Highlight points
                dataSetll.setCircleColor(Color.BLACK);
                dataSetll.setLineWidth(2f);



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

                XAxis xAxis = lineChart.getXAxis();
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
