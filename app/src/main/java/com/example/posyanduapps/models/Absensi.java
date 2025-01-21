package com.example.posyanduapps.models;

import com.google.firebase.database.PropertyName;

public class Absensi {
    @PropertyName("id")
    private String idAbsensi;
    private String nama;
    private String tanggal;
    private String hari;
    private String jam;
    private String tempat;
    private String assignedTo;
    private String assginedby;

    public Absensi() {
        // Diperlukan untuk Firebase
    }

    public Absensi(String nama, String tanggal, String hari, String jam, String tempat,String assignedTo,String assginedby) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.hari = hari;
        this.jam = jam;
        this.tempat = tempat;
        this.assignedTo=assignedTo;
        this.assginedby=assginedby;
    }

    public Absensi(String nama, String tanggal, String hari, String jam, String tempat) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.hari = hari;
        this.jam = jam;
        this.tempat = tempat;
    }

    public String getId() {
        return idAbsensi;
    }

    public void setId(String idAbsensi) {
        this.idAbsensi = idAbsensi;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getHari() {
        return hari;
    }

    public String getJam() {
        return jam;
    }

    public String getTempat() {
        return tempat;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getAssginedby() {
        return assginedby;
    }
    @Override
    public String toString() {
        return "Nama: " + nama + "\nTanggal: " + tanggal + "\nHari: " + hari + "\nJam: " + jam + "\nTempat: " + tempat+"\nDibuat oleh: "+assginedby+"\nDibuat untuk: "+assignedTo;
    }
}
