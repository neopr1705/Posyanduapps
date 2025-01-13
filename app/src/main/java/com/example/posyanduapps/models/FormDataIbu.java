package com.example.posyanduapps.models;

public class FormDataIbu {
    private String id;
    private String nama;
    private String intro;
    private int iconResId;

    // Constructor
    public FormDataIbu(String id, String nama, String intro, int iconResId) {
        this.id = id;
        this.nama = nama;
        this.intro = intro;
        this.iconResId = iconResId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
