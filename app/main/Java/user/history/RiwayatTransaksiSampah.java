package com.dev.banksampahdigital.View.history;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import com.dev.banksampahdigital.View.input.jemput_sampah;
import com.dev.banksampahdigital.View.main.HomeActivity;
import com.dev.banksampahdigital.adapter.Objek_penarikan;
import com.dev.banksampahdigital.adapter.Objek_transaki;
import com.dev.banksampahdigital.adapter.adapter_penarikan;
import com.dev.banksampahdigital.adapter.adapter_transaksi;
import com.dev.banksampahdigital.admin.View.penarikan_saldo.home_penarikan_saldo;
import com.dev.banksampahdigital.admin.View.penarikan_saldo.konfirmasi_penarikan;
import com.dev.banksampahdigital.admin.View.penarikan_saldo.transaksi_penarikan_saldo;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RiwayatTransaksiSampah extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    TextView tvsaldo;
    Button tambah_data_penarikan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_riwayat_transaksi_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbardetailtransaksi_user);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvsaldo = findViewById(R.id.tvSaldo_user);
        tampilsaldouser();

        listView = findViewById(R.id.list_detail_transaksi_user);
        load();

        tambah_data_penarikan = findViewById(R.id.tambah_data_penarikan);
        tambah_data_penarikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("username");
                Intent intent = new Intent(getApplicationContext(), transaksi_penarikan_saldo.class);
                intent.putExtra("username", name);
                startActivity(intent);
            }
        });

    }

    public void tampilsaldouser(){
            StringRequest request = new StringRequest(
                    Request.Method.GET, Db_Contract.SERVER_TAMPIL_NAMA_LOGIN + getIntent().getStringExtra("username"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                tvsaldo.setText(jsonObject.getString("saldo"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RiwayatTransaksiSampah.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
                }
            }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);

    }

    ArrayList<Objek_penarikan>model;
    public void load(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_DETAIL_PENARIKAN_USER + getIntent().getStringExtra("username"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray gethasil = jsonObject.getJSONArray("hasil");
                            model = new ArrayList<>();

                            for (int i = 0; i < gethasil.length(); i++){
                                JSONObject getData = gethasil.getJSONObject(i);
                                String id_penarikan = getData.getString("id_penarikan");
                                String id_user = getData.getString("id_user");
                                String username = getData.getString("username");
                                String tanggal = getData.getString("tanggal");
                                String jumlah_penarikan = getData.getString("jumlah_penarikan");
                                String saldo = getData.getString("saldo_akhir");
                                String status = getData.getString("status");

                                model.add(new Objek_penarikan(id_penarikan, id_user, username,tanggal,jumlah_penarikan,"",saldo,status,""));
                            }

                            final adapter_penarikan adapter = new adapter_penarikan(RiwayatTransaksiSampah.this, model);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getApplicationContext(), detail_penarikan_user.class);
                                    intent.putExtra("id_penarikan", model.get(position).getId_penarikan());
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatTransaksiSampah.this);
                            builder.setMessage("Opps Data Masih Kosong, Silahkan Tambahkan Data !!!");
                            builder.setNegativeButton("ok.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
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
        requestQueue.add(stringRequest);
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

    @Override
    protected void onResume() {
        load();
        super.onResume();
    }
}