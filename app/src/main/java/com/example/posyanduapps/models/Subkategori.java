package com.example.posyanduapps.models;

public class Subkategori {
    private String nama;
    private int iconResId;

    public Subkategori(String nama, int iconResId) {
        this.nama = nama;
        this.iconResId = iconResId;
    }

    public String getNama() {
        return nama;
    }

    public int getIconResId() {
        return iconResId;
    }
}
