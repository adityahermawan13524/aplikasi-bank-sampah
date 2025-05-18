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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.admin.View.data_nasabah.data_nasabah;
import com.dev.banksampahdigital.admin.View.data_nasabah.update_data_nasabah;
import com.dev.banksampahdigital.database.Db_Contract;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class edit_data_sampah extends AppCompatActivity {
    Toolbar toolbar;
    Button edit;
    TextInputEditText editJenisSampah, editHargaperKg;
    ProgressDialog progressDialog, progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_data_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(edit_data_sampah.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mohon Tunggu...");

        progressDialog1 = new ProgressDialog(edit_data_sampah.this);
        progressDialog1.setTitle("Loading");
        progressDialog1.setMessage("Mengambil Data...");

        toolbar = findViewById(R.id.toolbareditdatasampah);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editJenisSampah = findViewById(R.id.edit_jenis_sampah);
        editHargaperKg = findViewById(R.id.edit_harga_sampah);

        edit = findViewById(R.id.btneditdatasampah);

        tampil_data_sampah();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SeditJenisSampah = editJenisSampah.getText().toString();
                String SeditHargaperKg = editHargaperKg.getText().toString();

                if (SeditJenisSampah.isEmpty() && SeditHargaperKg.isEmpty()){
                    Toast.makeText(edit_data_sampah.this, "Silahkan Isi Semua Data", Toast.LENGTH_SHORT).show();
                }else{
                    edit_data_sampah(SeditJenisSampah, SeditHargaperKg);
                }
            }
        });


    }

    public void tampil_data_sampah() {
        if (cekNetworkConnection()) {
            progressDialog1.show();
            StringRequest request = new StringRequest(
                    Request.Method.GET, Db_Contract.SERVER_TAMPIL_DATA_SAMPAH + getIntent().getStringExtra("id_sampah"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                editJenisSampah.setText(jsonObject.getString("jenis_sampah"));
                                editHargaperKg.setText(jsonObject.getString("harga_perKg"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(edit_data_sampah.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog1.cancel();
                }
            }, 2000);
        }else{
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !!", Toast.LENGTH_SHORT).show();
        }
    }

    public void edit_data_sampah(final String jenis_sampah, final String harga_perKg){
        if (cekNetworkConnection()) {
            StringRequest request = new StringRequest(
                    Request.Method.POST, Db_Contract.SERVER_EDIT_DATA_SAMPAH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status \":\"succses\"}]")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(edit_data_sampah.this);
                                    builder.setMessage("Data Sukses Terupdate");
                                    builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(edit_data_sampah.this, data_sampah.class);
                                            startActivity(intent);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                } else {
                                    Toast.makeText(edit_data_sampah.this, "Update data Gagal", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(edit_data_sampah.this, "GAGAL", Toast.LENGTH_SHORT).show();
                }
            }
            ) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_sampah", getIntent().getStringExtra("id_sampah"));
                    params.put("jenis_sampah", jenis_sampah);
                    params.put("harga_perKg", harga_perKg);

                    return params;
                }
            };
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