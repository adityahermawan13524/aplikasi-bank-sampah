package com.dev.banksampahdigital.adapter;

public class Objek_Jadwal {
    String id_jadwal = "";
    String hari = "";
    String tanggal = "";
    String waktu_awal = "";
    String waktu_akhir = "";
    String tempat = "";
    String catatan = "";

    public Objek_Jadwal(String id_jadwal, String hari, String tanggal, String waktu_awal, String waktu_akhir, String tempat, String catatan) {
        this.id_jadwal = id_jadwal;
        this.hari = hari;
        this.tanggal = tanggal;
        this.waktu_awal = waktu_awal;
        this.waktu_akhir = waktu_akhir;
        this.tempat = tempat;
        this.catatan = catatan;
    }



    public String getId_jadwal() {
        return id_jadwal;
    }

    public String getHari() {
        return hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getWaktu_awal() {
        return waktu_awal;
    }

    public String getWaktu_akhir() {
        return waktu_akhir;
    }

    public String getTempat() {
        return tempat;
    }

    public String getCatatan() {
        return catatan;
    }
}
