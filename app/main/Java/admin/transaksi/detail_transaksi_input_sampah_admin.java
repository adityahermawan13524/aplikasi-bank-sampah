package com.dev.banksampahdigital.admin.View.transaksi;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.adapter.Objek;
import com.dev.banksampahdigital.adapter.Objek_transaki;
import com.dev.banksampahdigital.adapter.adapter_detail_transaksi;
import com.dev.banksampahdigital.adapter.adapter_transaksi;
import com.dev.banksampahdigital.adapter.adpter_cari_username;
import com.dev.banksampahdigital.admin.View.data_nasabah.data_nasabah;
import com.dev.banksampahdigital.admin.View.data_nasabah.lihat_data_nasabah;
import com.dev.banksampahdigital.admin.View.penarikan_saldo.home_penarikan_saldo;
import com.dev.banksampahdigital.admin.View.penarikan_saldo.transaksi_penarikan_saldo;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class detail_transaksi_input_sampah_admin extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_transaksi_input_sampah_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbardetailtransaksi);
        listView = findViewById(R.id.list_detail_transaksi);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        load();

    }

    ArrayList<Objek_transaki> model;
    void load(){
        StringRequest request = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_DETAIL_TRANSAKSI_SAMPAH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray getHasil = jsonObject.getJSONArray("hasil");

                           model = new ArrayList<>();
                            for (int i = 0; i<getHasil.length();i++) {
                                JSONObject getData = getHasil.getJSONObject(i);
                                String id_input_smph = getData.getString("id_input_smph");
                                String id_user = getData.getString("id_user");
                                String username = getData.getString("username");
                                String kategori_sampah = getData.getString("kategori_sampah");
                                String berat = getData.getString("berat");
                                String harga_perKg = "Rp, " + getData.getString("harga_perKg");
                                String pajak = "Rp, " + getData.getString("pajak");
                                String harga ="Rp, " + getData.getString("harga");
                                String tanggal = getData.getString("tanggal");
                                String alamat = getData.getString("alamat");
                                String status = getData.getString("status");
                                model.add(new Objek_transaki(
                                        id_input_smph,
                                        "",
                                        username,
                                        kategori_sampah,
                                        berat,
                                        harga_perKg,
                                        pajak,
                                        harga,
                                        tanggal,
                                        alamat,
                                        "",
                                        "",
                                        "",
                                        status
                                        ));
                            }
                            final adapter_transaksi adapter = new adapter_transaksi (detail_transaksi_input_sampah_admin.this, model);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String name = getIntent().getStringExtra("usert");
                                    Intent intent = new Intent(getApplicationContext(), konfirmasi_jemput_sampah.class);
                                    intent.putExtra("id_input_smph", model.get(position).getId_input_smph());
                                    intent.putExtra("username", model.get(position).getUsername());
                                    intent.putExtra("usert", name);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(detail_transaksi_input_sampah_admin.this);
                            builder.setMessage("Opps Data Masih Kosong, Silahkan Tambahkan Data !!!");
                            builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), transaksi_penarikan_saldo.class);
                                    startActivity(intent);
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void hapus() {
        StringRequest request = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_DELET_TRANSAKSI_INPUTSAMPAH + getIntent().getStringExtra("id_input_sampah"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("server_response");
                            if (resp.equals("[{\"status \":\"succses\"}]")){
                                Toast.makeText(getApplicationContext(), "DATA TERHAPUS", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "GAGAL", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "eror", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



    @Override
    protected void onResume() {
        load();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}