package com.dev.banksampahdigital.model;

import com.dev.banksampahdigital.LoginRegister.RegisterActivity;

public class Data {
    private String name, tanggal, kategori, berat, harga;

    public Data() {

    }

    public Data(String name, String tanggal, String kategori, String berat, String harga) {

        this.name = name;
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.berat = berat;
        this.harga = harga;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
