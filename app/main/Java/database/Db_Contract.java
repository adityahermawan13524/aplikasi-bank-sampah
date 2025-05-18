package com.dev.banksampahdigital.database;

import retrofit2.http.PUT;

public class Db_Contract {

    public static String ip1 = "192.168.100.122";
    public static String ip = "192.168.100.122";

    public static final String SERVER_LOGIN_URL = "http://"+ip+"/bank_sampah/api_login.php";
    public static final String SERVER_LOGIN_ADMIN = "http://" + ip + "/bank_sampah/api-login-admin.php";
    public static final String SERVER_REGISTER_URL = "http://" + ip + "/bank_sampah/api-register.php";
    public static final String SERVER_CARI_NAMA_URL = "http://" + ip + "/bank_sampah/api-cari.php?data=";
    public static final String SERVER_DATA_NASABAH = "http://" + ip + "/bank_sampah/api-data-nasabah.php";
    public static final String SERVER_TAMPIL_NAMA = "http://"+ ip +"/bank_sampah/api_tampil_nama.php?id_user=";
    public static final String SERVER_INPUT_SAMPAH_ADMIN = "http://"+ip+"/bank_sampah/api-input-sampah-admin.php";
    public static final String SERVER_REGISTER_ADMIN = "http:/" +ip+ "/bank_sampah/api-register-admin.php";
    public static final String SERVER_CARI_NAMA_DATA_NASABAH = "http://" + ip + "/bank_sampah/api-cari-data-nasabah.php?data=";
    public static final String SERVER_TAMPIL_DATA_NASABAH = "http://"+ip+"/bank_sampah/api-tampil-data-nasabah.php?id_user=";
    public static final String SERVER_HAPUS_DATA_NASABAH = "http://"+ip+"/bank_sampah/api-delete-data-nasabah.php?id_user=";
    public static final String SERVER_UPDATE_DATA = "http://"+ip+"/bank_sampah/api-update-data-nasabah.php";
    public static final String SERVER_DETAIL_TRANSAKSI_SAMPAH = "http://"+ip+"/bank_sampah/api-detail-transaksi-sampah.php";
    public static final String SERVER_DELET_TRANSAKSI_INPUTSAMPAH = "http://"+ip+"/bank_sampah/api-delete-transaksi-inputsampah.php?id_input_sampah=";
    public static final String SERVER_CREATE_SALDO = "http://"+ip+"/bank_sampah/api-create-saldo.php";
    public  static final String SERVER_TAMPIL_SALDO = "http://"+ip+"/bank_sampah/api-tampil-saldo.php?id_user=";
    public static final String SERVER_UPDATE_SALDO = "http://"+ip+"/bank_sampah/api-update-saldo.php";
    public static final String SERVER_TAMPIL_NAMA_LOGIN = "http://"+ip+"/bank_sampah/api-tampil-nama-login.php?username=";
    public static final String SERVER_TAMPIL_NAMA_LOGIN_ADMIN = "http://"+ip+"/bank_sampah/api_tampil_nama_admin.php?username=";
    public static final String SERVER_TRANSAKSI_USER = "http://"+ip+"/bank_sampah/api-transaksi-user.php?username=";
    public static final String SERVER_TAMBAH_DATA_SAMPAH = "http://"+ip+"/bank_sampah/api_input_data_sampah.php";
    public static final String SERVER_DATA_SAMPAH = "http://"+ip+"/bank_sampah/api_data_sampah.php";
    public static final String SERVER_TAMPIL_DATA_SAMPAH = "http://"+ip+"/bank_sampah/api_tampil_data_sampah.php?id_sampah=";
    public static final String SERVER_HAPUS_DATA_SAMPAH = "http://"+ip+"/bank_sampah/api-delete-data-sampah.php?id_sampah=";
    public static final String SERVER_EDIT_DATA_SAMPAH = "http://"+ip+"/bank_sampah/api_update_data_sampah.php";
    public static final String SERVER_TAMPIL_DATA_SAMPAH_PINNER = "http://"+ip+"/bank_sampah/api_tampil_data_sampah_spinner.php?jenis_sampah=";
    public static final String SERVER_DATA_PENARIKAN = "http://"+ip+"/bank_sampah/api_data_penarikan.php";
    public static final String SERVER_CREATE_PENARIKAN = "http://"+ip+"/bank_sampah/api_create_penarikan.php";
    public static final String SERVER_TAMPIL_DATA_PENARIKAN = "http://"+ip+"/bank_sampah/api_tampil_data_penarikan.php?id_penarikan=";
    public static final String SERVER_UPDATE_STATUS = "http://"+ip+"/bank_sampah/api_update_status_penarikan.php";
    public static final String SERVER_CEK_STATUS_PENARIKAN = "http://"+ip+"/bank_sampah/api_cek_status.php";
    public static final String SERVER_DETAIL_PENARIKAN_USER = "http://"+ip+"/bank_sampah/api_detail_penarikan_user.php?username=";
    public static final String SERVER_CREATE_TOKEN = "http://"+ip+"/bank_sampah/api_create_token.php";
    public static final String SERVER_SENDNOTIFICATION = "http://"+ip+"/bank_sampah/fungsi.php";
    public static final String SERVER_GETTOKENFROMDB = "http://"+ip+"/bank_sampah/api_gettoken_db.php?username=";
    public static final String SERVER_TMPL_DTL_INPT_SMPH = "http://"+ip+"/bank_sampah/api_tampil_data_input_sampah.php?id_input_smph=";
    public static final String SERVER_UPDATE_STTS_INPUT_SAMPAH = "http://"+ip+"/bank_sampah/api_update_status_input_sampah.php";
    public static final String SERVER_CREATE_JADWAL = "http://"+ip+"/bank_sampah/api_input_jadwal.php";
    public static final String SERVER_DATA_JADWAL = "http://"+ip+"/bank_sampah/api_data_jadwal.php";
    public static final String SERVER_TMPL_DATA_JADWAL = "http://"+ip+"/bank_sampah/api_tampil_data_jadwal.php?id_jadwal=";
    public static final String SERVER_TMPL_DATA_PRFL = "http://"+ip+"/bank_sampah/api-tampil-data-profil.php?username=";
    public static final String TOTAL_BERAT = "http://"+ip+"/bank_sampah/api_total_berat.php";
    public static final String TOTAL_PAJAK = "http://"+ip+"/bank_sampah/api_total_pajak.php";
    public static final String TOTAL_SALDO = "http://"+ip+"/bank_sampah/api_total_saldo.php";
    public static final String INFORMASI = "http://"+ip+"/bank_sampah/informasi.php";
    public static final String INFORMASI_PERTGL = "http://"+ip+"/bank_sampah/informasi_penjualan_berdasarkan_tgl.php";
    public static final String INFORMASI_TOTAL_PERTGL = "http://"+ip+"/bank_sampah/api_pertgl.php?tanggal=";
}
