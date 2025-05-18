package com.dev.banksampahdigital.admin.View.data_sampah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.admin.View.data_nasabah.tambah_data_nasabah;
import com.dev.banksampahdigital.admin.View.input.input_sampah_admin;
import com.dev.banksampahdigital.admin.View.menu_admin.home_admin;
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class tambah_data_sampah extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText jenis_sampah, harga_perKg;
    Button tambah_data_sampah;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah_data_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(tambah_data_sampah.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan Data...");

        toolbar = findViewById(R.id.toolbartmbhdatasampah);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        jenis_sampah = findViewById(R.id.input_jenis_sampah);
        harga_perKg = findViewById(R.id.harga_sampah);

        tambah_data_sampah = findViewById(R.id.btninputdatasampah);

        tambah_data_sampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TextJenisSampah = jenis_sampah.getText().toString();
                String TextHargaSampah = harga_perKg.getText().toString();

                if (TextUtils.isEmpty(TextJenisSampah)){
                    jenis_sampah.setError("Jenis Sampah Harus Diisi !!!");
                    jenis_sampah.requestFocus();
                } else if (TextUtils.isEmpty(TextHargaSampah)) {
                    harga_perKg.setError("Harga Sampah Harus diisi !!!");
                    harga_perKg.requestFocus();
                }else{
                    create_data_sampah(TextJenisSampah, TextHargaSampah);
                }
            }
        });
    }

    public void create_data_sampah(final String jenissampah, final String hargaperKG) {
        if (cekNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, Db_Contract.SERVER_TAMBAH_DATA_SAMPAH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status \":\"succses\"}]")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(tambah_data_sampah.this);
                                    builder.setMessage("Input Sampah Berhasil");
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
                                    Toast.makeText(getApplicationContext(), "Tambah Data Sampah GAGAL", Toast.LENGTH_SHORT).show();
                                    jenis_sampah.setError("Jenis Sampah Sudah ada !!.");
                                    jenis_sampah.requestFocus();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(tambah_data_sampah.this, "Input Data Sampah Gagal", Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("jenis_sampah", jenissampah);
                    params.put("harga_perKg", hargaperKG);
                    return params;
                }
            };
            VolleyConnection.getInstance(tambah_data_sampah.this).addToRequesQue(stringRequest);
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