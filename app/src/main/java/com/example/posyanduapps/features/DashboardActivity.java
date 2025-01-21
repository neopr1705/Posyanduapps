package com.example.posyanduapps.features;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.posyanduapps.Helper.HeaderIconHelper;
import com.example.posyanduapps.R;

public class DashboardActivity extends Activity {
    private Button btnBalita, btnLansia, btnBumil;
    private ImageView ivHome, ivReminder, ivProfile, ivSettings,ivChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SharedPreferences sharedPreferences = getSharedPreferences("Option", MODE_PRIVATE);
        SharedPreferences UserPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String NamaLengkap = UserPreferences.getString("currentNama", "");
        String Userbucket = UserPreferences.getString("BucketUser","");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        View HeaderLayout = findViewById(R.id.header_layout);
        HeaderIconHelper headerIconHelper = new HeaderIconHelper(this, HeaderLayout);
        headerIconHelper.setChoiceGONE();
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Mom Care");
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Selamat Datang,"+NamaLengkap);

        Log.d("Bucket","User Bucket sekarang: "+Userbucket);

        btnBalita = findViewById(R.id.btnBalita);
        btnLansia = findViewById(R.id.btnLansia);
        btnBumil = findViewById(R.id.btnBumil);
        ivHome = findViewById(R.id.ivHome);
        ivReminder = findViewById(R.id.ivReminder);
        ivProfile = findViewById(R.id.ivProfile);
        ivSettings = findViewById(R.id.ivSettings);

        removefooterIcon();
        btnBalita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EdukasiBumilActivity.class);
                editor.putInt("currentOption",1 );  // username yang didapat saat login
                editor.apply();  // Menyimpan perubahan
                startActivity(intent);
            }
        });

        btnLansia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EdukasiBumilActivity.class);
                editor.putInt("currentOption", 2);  // username yang didapat saat login
                editor.apply();  // Menyimpan perubahan
                startActivity(intent);
            }
        });

        btnBumil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EdukasiBumilActivity.class);
                editor.putInt("currentOption",3 );  // username yang didapat saat login
                editor.apply();  // Menyimpan perubahan
                startActivity(intent);
            }
        });
    }

    public void removefooterIcon(){
        ivHome.setVisibility(View.GONE);
        ivReminder.setVisibility(View.GONE);
        ivProfile.setVisibility(View.GONE);
        ivSettings.setVisibility(View.GONE);
    }
}
