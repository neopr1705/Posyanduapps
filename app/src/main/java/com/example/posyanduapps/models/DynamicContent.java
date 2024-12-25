package com.example.posyanduapps.models;

import java.util.List;

public class DynamicContent {
    private List<String> textList; // List teks
    private List<Integer> imageList; // List gambar

    public DynamicContent() {

    }
    public DynamicContent(List<String> textList, List<Integer> imageList) {
        this.textList = textList;
        this.imageList = imageList;
    }

    public List<String> getTextList() {
        return textList;
    }

    public List<Integer> getImageList() {
        return imageList;
    }
}