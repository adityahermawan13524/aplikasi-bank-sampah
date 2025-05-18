package com.dev.banksampahdigital.admin.View.data_nasabah;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class lihat_data_nasabah extends AppCompatActivity {

    TextView tvNama, tvsaldo, tvemail, tvdob, tvalamat, tvgender, tvmobile,tvpassword, tvstatus;
    Button edit, hapus;
    ArrayList<Objek> model;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lihat_data_nasabah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(lihat_data_nasabah.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mohon Tunggu...");

        tvNama = findViewById(R.id.tmpl_Nama_user);
        tvsaldo = findViewById(R.id.saldonasabah);
        tvemail = findViewById(R.id.tv_email_tampil);
        tvdob = findViewById(R.id.tv_dob_tampil);
        tvalamat = findViewById(R.id.tv_alamat_tampil);
        tvgender = findViewById(R.id.tv_gender_tampil);
        tvmobile = findViewById(R.id.tv_NoHp_tampil);
        tvpassword = findViewById(R.id.tv_password_tampil);
        tvstatus = findViewById(R.id.tv_status_tampil);
        edit = findViewById(R.id.btnedit);
        hapus = findViewById(R.id.btnhapus);

        model = new ArrayList<>();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), update_data_nasabah.class);
                intent.putExtra("id_user", getIntent().getStringExtra("id_user"));
                startActivity(intent);
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(lihat_data_nasabah.this);
                builder.setMessage("Apakah Anda Ingin Menghapus Data?");
                builder.setPositiveButton("Iya.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapus_data_nasabah();
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

        tampil_data_nasabah();

    }

    void tampil_data_nasabah(){
        StringRequest request = new StringRequest(
                Request.Method.GET, Db_Contract.SERVER_TAMPIL_DATA_NASABAH + getIntent().getStringExtra("id_user"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvNama.setText(jsonObject.getString("username"));
                            tvemail.setText(jsonObject.getString("email"));
                            tvdob.setText(jsonObject.getString("dob"));
                            tvalamat.setText(jsonObject.getString("alamat"));
                            tvgender.setText(jsonObject.getString("gender"));
                            tvmobile.setText(jsonObject.getString("mobile"));
                            tvpassword.setText(jsonObject.getString("password"));
                            tvstatus.setText(jsonObject.getString("status"));
                            tvsaldo.setText("Rp. " + jsonObject.getString("saldo"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(lihat_data_nasabah.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    void hapus_data_nasabah(){
        if (cekNetworkConnection()){
            progressDialog.show();
            StringRequest request = new StringRequest(
                    Request.Method.GET, Db_Contract.SERVER_HAPUS_DATA_NASABAH + getIntent().getStringExtra("id_user"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("data_terhapus")){

                                    AlertDialog.Builder builder = new AlertDialog.Builder(lihat_data_nasabah.this);
                                    builder.setMessage("Data Sukses Terhapus?");
                                    builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }else {
                                    Toast.makeText(lihat_data_nasabah.this, "Data Gagal DiHapus..", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(lihat_data_nasabah.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        }else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !!", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean cekNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}