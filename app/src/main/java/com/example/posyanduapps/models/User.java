package com.example.posyanduapps.models;
import android.os.Parcelable;
import android.os.Parcel;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

public class User implements Parcelable {
    private String id;
    @PropertyName("nama_lengkap")
    private String namaLengkap;
    @PropertyName("alamat_lengkap")
    private String alamatLengkap;
    @PropertyName("tanggal_lahir")
    private String tanggalLahir;
    @PropertyName("usia_kehamilan")
    private String usiaKehamilan;
    @PropertyName("nomor_hp")
    private String nomorHp;
    private String username;
    private String password;
    @PropertyName("roles")
    private String roles;

    // Konstruktor tanpa argumen (default constructor)
    public User() {}
    public User(String id, String namaLengkap, String alamatLengkap, String tanggalLahir, String usiaKehamilan, String nomorHp, String username, String password,String roles) {
        this.namaLengkap = namaLengkap;
        this.alamatLengkap = alamatLengkap;
        this.tanggalLahir = tanggalLahir;
        this.usiaKehamilan = usiaKehamilan;
        this.nomorHp = nomorHp;
        this.username = username;
        this.password = password;
        this.id = id;
        this.roles = roles;
    }

public String getId() {
        return id;
}

    public int castID(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            Log.e("User", "Invalid ID format: " + id);
            return -1; // atau nilai default lainnya
        }
    }


public long castID(){
        return Integer.parseInt(this.id);
}
    public String
    getNamaLengkap() {
        return namaLengkap;
    }

    public String getAlamatLengkap() {
        return alamatLengkap;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getUsiaKehamilan() {
        return usiaKehamilan;
    }

    public String getNomorHp() {
        return nomorHp;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}
