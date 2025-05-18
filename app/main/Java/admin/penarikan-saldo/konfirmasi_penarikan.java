package com.dev.banksampahdigital.admin.View.penarikan_saldo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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
import com.dev.banksampahdigital.admin.View.data_sampah.lihat_data_sampah;
import com.dev.banksampahdigital.admin.View.input.input_sampah_admin;
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class konfirmasi_penarikan extends AppCompatActivity {

    Toolbar toolbar;
    Button diproses, ditolak;
    TextInputEditText username, tanggal, saldo_awal, jumlah_penarikan, saldo_akhir, status, otp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_konfirmasi_penarikan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(konfirmasi_penarikan.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mohon Tunggu...");

        toolbar = findViewById(R.id.toolbarkonfrimpenarikan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        username = findViewById(R.id.k_nama);
        tanggal = findViewById(R.id.k_tanggal);
        saldo_awal = findViewById(R.id.k_saldo_awal);
        jumlah_penarikan = findViewById(R.id.k_jml_penarikan);
        saldo_akhir = findViewById(R.id.k_saldo_akhir);
        status = findViewById(R.id.k_status);
        otp = findViewById(R.id.k_otp);

        tampil();

        diproses = findViewById(R.id.btndiproses);
        diproses.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String textusername = username.getText().toString();
                String textsaldo = saldo_akhir.getText().toString();
                String textstatus = status.getText().toString();
                String textotp = otp.getText().toString();
                String otp_codes = getIntent().getStringExtra("otp_code").toString();



                if (textstatus.equals("Sedang Diproses !!")) {

                    if (textotp.isEmpty()){
                        otp.requestFocus();
                        otp.setError("OTP TIDAK BOLEH KOSONG");
                        Toast.makeText(konfirmasi_penarikan.this, "OTP TIDAK BOLEH KOSONG", Toast.LENGTH_SHORT).show();
                    } else if (otp_codes.equals(textotp)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(konfirmasi_penarikan.this);
                        builder.setMessage("Apakah Anda Ingin Memproses Penarikan Saldo Nasabah?");
                        builder.setPositiveButton("Iya.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updatesaldo(textusername, textsaldo);
                                update_status(textusername, "Sukses");

//                          send Notification
                                String textuser = username.getText().toString();
                                String saldo = saldo_akhir.getText().toString();
                                StringRequest request = new StringRequest(Request.Method.GET,
                                        Db_Contract.SERVER_GETTOKENFROMDB + textuser,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String username = jsonObject.getString("username");
                                                    String token = jsonObject.getString("token");

                                                    String body = "Transaksi Penarikan Tabungan \n" + "Bank Sampah Disetujui, \n" + "Saldo Anda Rp. " + saldo;

                                                    StringRequest request = new StringRequest(Request.Method.POST,
                                                            Db_Contract.SERVER_SENDNOTIFICATION,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    Log.d("result", "result: done! " + response);
                                                                }
                                                            }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(konfirmasi_penarikan.this, "terjadi kesalahan Notification", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }) {
                                                        @Override
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> params = new HashMap<>();
                                                            params.put("token", token);
                                                            params.put("title", username);
                                                            params.put("body", body);
                                                            return params;
                                                        }
                                                    };
                                                    RequestQueue requestQueue = Volley.newRequestQueue(konfirmasi_penarikan.this);
                                                    requestQueue.add(request);


                                                } catch (JSONException e) {
                                                    Toast.makeText(konfirmasi_penarikan.this, "ERR AMBIL TOKEN", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                                );
                                RequestQueue requestQueue = Volley.newRequestQueue(konfirmasi_penarikan.this);
                                requestQueue.add(request);

                                Intent intent = new Intent(getApplicationContext(), home_penarikan_saldo.class);
                                startActivity(intent);
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

                    }else {
                        otp.requestFocus();
                        otp.setError("OTP Salah");
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(konfirmasi_penarikan.this);
                    builder.setMessage("Mohon maaf data sudah di proses !!");
                    builder.setNegativeButton("Ok.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });


    }

    public void tampil(){
        StringRequest request = new StringRequest(Request.Method.GET,
                Db_Contract.SERVER_TAMPIL_DATA_PENARIKAN + getIntent().getStringExtra("id_penarikan"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            username.setText(jsonObject.getString("username"));
                            tanggal.setText(jsonObject.getString("tanggal"));
                            saldo_awal.setText(jsonObject.getString("saldo_awal"));
                            jumlah_penarikan.setText(jsonObject.getString("jumlah_penarikan"));
                            saldo_akhir.setText(jsonObject.getString("saldo_akhir"));
                            status.setText(jsonObject.getString("status"));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void updatesaldo( final String username, final String saldo){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_UPDATE_SALDO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("server_response");
                            if (resp.equals("[{\"status\":\"succes\"}]")){
                                //Toast.makeText(input_sampah_admin.this, "CREATE SALDO BERHASIL", Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(konfirmasi_penarikan.this, getIntent().getStringExtra("id_user"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(konfirmasi_penarikan.this, "GAGAL_UPDATE SALDO", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(konfirmasi_penarikan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", getIntent().getStringExtra("id_user"));
                params.put("username", username);
                params.put("saldo", saldo);
                return params;
            }
        };
        VolleyConnection.getInstance(konfirmasi_penarikan.this).addToRequesQue(stringRequest);
    }

    public void update_status(String username, String status){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_UPDATE_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("server_response");
                            if (resp.equals("[{\"status\":\"succes\"}]")){
                                //Toast.makeText(input_sampah_admin.this, "CREATE SALDO BERHASIL", Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(konfirmasi_penarikan.this, getIntent().getStringExtra("id_user"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(konfirmasi_penarikan.this, "GAGAL_UPDATE SALDO", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(konfirmasi_penarikan.this, "GAGALUPDATE STTS", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_penarikan", getIntent().getStringExtra("id_penarikan"));
                params.put("username", username);
                params.put("status", status);
                return params;
            }
        };
        VolleyConnection.getInstance(konfirmasi_penarikan.this).addToRequesQue(stringRequest);
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