package com.dev.banksampahdigital.admin.View.data_sampah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
import com.dev.banksampahdigital.admin.View.data_nasabah.lihat_data_nasabah;
import com.dev.banksampahdigital.database.Db_Contract;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class lihat_data_sampah extends AppCompatActivity {

    Toolbar toolbar;
    Button edit, hapus;
    TextInputEditText lihatJenisSampah, lihatHargaperKg;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lihat_data_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(lihat_data_sampah.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mohon Tunggu...");

        toolbar = findViewById(R.id.toolbarlihatdatasampah);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lihatJenisSampah = findViewById(R.id.lihat_jenis_sampah);
        lihatHargaperKg = findViewById(R.id.lihat_harga_sampah);

        edit = findViewById(R.id.btneditdatasampah);
        hapus = findViewById(R.id.btnhapusdatasampah);

        tampil_data_sampah();

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(lihat_data_sampah.this);
                builder.setMessage("Apakah Anda Ingin Menghapus Data?");
                builder.setPositiveButton("Iya.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapus_data_sampah();
                    }
                });
                builder.setNegativeButton("Tidak.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), edit_data_sampah.class);
                intent.putExtra("id_sampah", getIntent().getStringExtra("id_sampah"));
                startActivity(intent);
            }
        });


    }

    public void tampil_data_sampah(){
        StringRequest request = new StringRequest(
                Request.Method.GET, Db_Contract.SERVER_TAMPIL_DATA_SAMPAH + getIntent().getStringExtra("id_sampah"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            lihatJenisSampah.setText(jsonObject.getString("jenis_sampah"));
                            lihatHargaperKg.setText(jsonObject.getString("harga_perKg"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(lihat_data_sampah.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    public void hapus_data_sampah(){
        if (cekNetworkConnection()){
            progressDialog.show();
            StringRequest request = new StringRequest(
                    Request.Method.GET, Db_Contract.SERVER_HAPUS_DATA_SAMPAH + getIntent().getStringExtra("id_sampah"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status \":\"succses\"}]")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(lihat_data_sampah.this);
                                    builder.setMessage("Hapus Data Sampah Berhasil");
                                    builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), data_sampah.class);
                                            startActivity(intent);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(lihat_data_sampah.this);
                                    builder.setMessage("Hapus Data Sampah GAGAL");
                                    builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), lihat_data_sampah.class);
                                            startActivity(intent);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(lihat_data_sampah.this, "GAGAL HAPUS DATA", Toast.LENGTH_SHORT).show();
                }
            }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        }else{
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !!", Toast.LENGTH_SHORT).show();
        }
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

    public Boolean cekNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}