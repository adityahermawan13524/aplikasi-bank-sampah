package com.dev.banksampahdigital.adapter;

public class Objek_penarikan {

    String id_penarikan = "";
    String id_user = "";
    String username = "";
    String tanggal = "";
    String jumlah_penarikan = "";
    String saldo_awal ="";
    String saldo_akhir = "";
    String status = "";
    String otp_code = "";

    public Objek_penarikan(String id_penarikan, String id_user, String username, String tanggal, String jumlah_penarikan, String saldo_awal, String saldo_akhir, String status, String otp_code) {
        this.id_penarikan = id_penarikan;
        this.id_user = id_user;
        this.username = username;
        this.tanggal = tanggal;
        this.jumlah_penarikan = jumlah_penarikan;
        this.saldo_awal = saldo_awal;
        this.saldo_akhir = saldo_akhir;
        this.status = status;
        this.otp_code = otp_code;
    }

    public String getOtp_code() {
        return otp_code;
    }

    public String getId_penarikan() {
        return id_penarikan;
    }

    public String getId_user() {
        return id_user;
    }

    public String getUsername() {
        return username;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJumlah_penarikan() {
        return jumlah_penarikan;
    }

    public String getSaldo_awal() {
        return saldo_awal;
    }

    public String getSaldo_akhir() {
        return saldo_akhir;
    }

    public String getStatus() {
        return status;
    }
}
