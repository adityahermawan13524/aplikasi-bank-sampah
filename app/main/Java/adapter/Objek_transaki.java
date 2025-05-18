package com.dev.banksampahdigital.adapter;

public class Objek_transaki {

    String id_input_smph = "";
    String id_user = "";
    String username = "";
    String kategori_sampah = "";
    String berat = "";
    String harga_perKg = "";
    String pajak = "";
    String harga = "";
    String saldo_awal = "";
    String saldo_akhir = "";
    String tanggal = "";
    String alamat = "";
    String catatan = "";
    String status = "";

    public Objek_transaki(String id_input_smph, String id_user, String username,
                          String kategori_sampah, String berat, String harga_perKg,
                          String pajak, String harga,String tanggal, String alamat,
                          String catatan, String saldo_awal, String saldo_akhir, String status){

        this.id_input_smph = id_input_smph;
        this.id_user = id_user;
        this.username = username;
        this.kategori_sampah = kategori_sampah;
        this.berat = berat;
        this.harga_perKg = harga_perKg;
        this.pajak = pajak;
        this.harga = harga;
        this.tanggal = tanggal;
        this.alamat = alamat;
        this.saldo_awal = saldo_awal;
        this.saldo_akhir = saldo_akhir;
        this.catatan = catatan;
        this.status = status;

    }

    public String getId_input_smph() {
        return id_input_smph;
    }

    public String getId_user() {
        return id_user;
    }

    public String getUsername() {
        return username;
    }

    public String getKategori_sampah() {
        return kategori_sampah;
    }

    public String getBerat() {
        return berat;
    }

    public String getHarga_perKg() {
        return harga_perKg;
    }

    public String getPajak() {
        return pajak;
    }

    public String getHarga() {
        return harga;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getCatatan() {
        return catatan;
    }

    public String getSaldo_awal() {
        return saldo_awal;
    }

    public String getSaldo_akhir() {
        return saldo_akhir;
    }

    public String getStatus(){
        return  status;
    }
}
