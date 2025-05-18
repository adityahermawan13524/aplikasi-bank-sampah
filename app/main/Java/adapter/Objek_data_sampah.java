package com.dev.banksampahdigital.adapter;

public class Objek_data_sampah {
    String id_sampah = "";
    String jenis_sampah = "";
    String harga_perKg = "";

    public Objek_data_sampah(String id_sampah, String jenis_sampah, String harga_perKg){

        this.id_sampah = id_sampah;
        this.jenis_sampah = jenis_sampah;
        this.harga_perKg = harga_perKg;

    }


    public String getId_sampah() {
        return id_sampah;
    }

    public String getJenis_sampah() {
        return jenis_sampah;
    }

    public String getHarga_perKg() {
        return harga_perKg;
    }
}
