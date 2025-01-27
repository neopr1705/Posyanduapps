package com.example.posyanduapps.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HealthRecord {
    private Date tanggal;
    private int beratBadan;
    private int tinggiBadan;
    private int lingkarKepala;
    private int lingkarPerut;

    public HealthRecord(String tanggal, String beratBadan, String tinggiBadan, String lingkarKepala, String lingkarPerut) {
        try {
            // Ubah tanggal menjadi objek Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            this.tanggal = sdf.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
            this.tanggal = new Date(); // Jika format tanggal salah, gunakan tanggal saat ini
        }

        // Ubah nilai string ke integer
        this.beratBadan = Integer.parseInt(beratBadan);
        this.tinggiBadan = Integer.parseInt(tinggiBadan);
        this.lingkarKepala = Integer.parseInt(lingkarKepala);
        this.lingkarPerut = Integer.parseInt(lingkarPerut);
    }

    public Date getTanggal() {
        return tanggal;
    }

    public int getBeratBadan() {
        return beratBadan;
    }

    public int getTinggiBadan() {
        return tinggiBadan;
    }

    public int getLingkarKepala() {
        return lingkarKepala;
    }

    public int getLingkarPerut() {
        return lingkarPerut;
    }
}
