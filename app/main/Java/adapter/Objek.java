package com.dev.banksampahdigital.adapter;

public class Objek {
    String id_user = "";
    String usernamae = "";
    String alamat = "";



    public Objek(String id_user, String usernamae, String alamat){
        this.id_user = id_user;
        this.usernamae = usernamae ;
        this.alamat = alamat;
    }

    public String getId_user() {
        return id_user;
    }

    public String getUsernamae() {
        return usernamae;
    }
    public String getAlamat() {
        return alamat;
    }
}
