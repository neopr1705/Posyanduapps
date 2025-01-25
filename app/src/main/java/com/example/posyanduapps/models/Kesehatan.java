package com.example.posyanduapps.models;

public class Kesehatan {
    private String nama;
    private String beratBadan;
    private String tinggiBadan;
    private String statusVaksin;
    private String riwayatPenyakit;
    private String lingkarKepala;
    private String lingkarLengan;
    private String lingkarPerut;
    private String tanggal;
    private String Jam;

    // Constructor
    public Kesehatan(String nama, String beratBadan, String tinggiBadan, String statusVaksin,
                     String riwayatPenyakit, String lingkarKepala, String lingkarLengan, String lingkarPerut,String tanggal,String jam) {
        this.nama = nama;
        this.beratBadan = beratBadan;
        this.tinggiBadan = tinggiBadan;
        this.statusVaksin = statusVaksin;
        this.riwayatPenyakit = riwayatPenyakit;
        this.lingkarKepala = lingkarKepala;
        this.lingkarLengan = lingkarLengan;
        this.lingkarPerut = lingkarPerut;
        this.tanggal = tanggal;
        this.Jam = jam;
    }

    // Getters
    public String getNama() {
        return nama;
    }
    public String getTanggal() {
        return tanggal;
    }
    public String getJam() {
        return Jam;
    }

    public String getBeratBadan() {
        return beratBadan;
    }

    public String getTinggiBadan() {
        return tinggiBadan;
    }

    public String getStatusVaksin() {
        return statusVaksin;
    }

    public String getRiwayatPenyakit() {
        return riwayatPenyakit;
    }

    public String getLingkarKepala() {
        return lingkarKepala;
    }

    public String getLingkarLengan() {
        return lingkarLengan;
    }

    public String getLingkarPerut() {
        return lingkarPerut;
    }
}
