package com.example.posyanduapps.models;

public class Subkategori {
    private String nama,keterangan;
    private int iconResId;

    public Subkategori(String nama,String keterangan, int iconResId) {
        this.nama = nama;
        this.keterangan = keterangan;
        this.iconResId = iconResId;
    }

    public String getNama() {
        return nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public int getIconResId() {
        return iconResId;
    }
}
