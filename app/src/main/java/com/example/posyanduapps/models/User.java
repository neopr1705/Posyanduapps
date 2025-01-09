package com.example.posyanduapps.models;

public class User {
    private int id;
    private String namaLengkap;
    private String alamatLengkap;
    private String tanggalLahir;
    private String usiaKehamilan;
    private String nomorHp;
    private String username;
    private String password;

    public User(int id, String namaLengkap, String alamatLengkap, String tanggalLahir, String usiaKehamilan, String nomorHp, String username, String password) {
        this.id = id;
        this.namaLengkap = namaLengkap;
        this.alamatLengkap = alamatLengkap;
        this.tanggalLahir = tanggalLahir;
        this.usiaKehamilan = usiaKehamilan;
        this.nomorHp = nomorHp;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNamaLengkap() {
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
}
