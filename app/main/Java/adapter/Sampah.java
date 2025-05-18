package com.dev.banksampahdigital.adapter;

public class Sampah {
    private String tanggal;
    private String kategori_sampah;
    private float total_berat;

    public Sampah(String tanggal, String kategori_sampah, float total_berat) {
        this.tanggal = tanggal;
        this.kategori_sampah = kategori_sampah;
        this.total_berat = total_berat;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKategoriSampah() {
        return kategori_sampah;
    }

    public float getTotalBerat() {
        return total_berat;
    }
}
