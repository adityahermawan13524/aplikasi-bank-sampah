package com.dev.banksampahdigital.admin.View.penarikan_saldo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.dev.banksampahdigital.View.history.RiwayatTransaksiSampah;
import com.dev.banksampahdigital.View.input.jemput_sampah;
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class transaksi_penarikan_saldo extends AppCompatActivity {

    Toolbar toolbar;
    private TextInputEditText input_nama, input_saldo_awal, input_jumlah_penarikan, input_saldo_akhir, input_tanggal;
    private DatePickerDialog inpuTgl;
    private ProgressDialog progressDialog;
    private Button btn_penarikan;
    int count_saldo_awal = 0, count_saldo_akhir = 0, count_jmlPenarikan = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaksi_penarikan_saldo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        input_nama = findViewById(R.id.inputNama);
        input_saldo_awal = findViewById(R.id.p_saldo_awal);
        input_jumlah_penarikan = findViewById(R.id.p_Jumlah_penarikan);
        input_saldo_akhir = findViewById(R.id.p_saldo_akhir);
        input_tanggal = findViewById(R.id.p_inputtgl);

        progressDialog = new ProgressDialog(transaksi_penarikan_saldo.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan Data...");

            input_tanggal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);

                    inpuTgl = new DatePickerDialog(transaksi_penarikan_saldo.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            input_tanggal.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }, year, month, day);
                    inpuTgl.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    inpuTgl.show();
                }
            });

        input_jumlah_penarikan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (input_nama.getText().toString().isEmpty()) {
                    input_nama.requestFocus();
                    input_nama.setError("Silahkan input nama");

                } else {
                    input_jumlah_penarikan.removeTextChangedListener(this);
                    if ((s.length() > 0))
                    {

                        String sal = input_saldo_awal.getText().toString();
                        String jum = input_jumlah_penarikan.getText().toString();

                        int sa = Integer.parseInt(sal);
                        int ju = Integer.parseInt(jum);

                        boolean b = sa <= ju;

                        if (b == true){
                            AlertDialog.Builder builder = new AlertDialog.Builder(transaksi_penarikan_saldo.this);
                            builder.setMessage("Opps Saldo Anda Tidak Cukup, Mohon Periksa Kembali Saldo Anda !!!");
                            builder.setNegativeButton("Ok !!.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    input_jumlah_penarikan.requestFocus();
                                    input_jumlah_penarikan.setError("Sldo tidak cukup");
                                    input_saldo_akhir.getText().clear();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            Toast.makeText(transaksi_penarikan_saldo.this, "GAGAL", Toast.LENGTH_SHORT).show();
                        }else {
                            count_jmlPenarikan = Integer.parseInt(s.toString());
                            total_saldo_akhir(count_jmlPenarikan);
                        }

                    }
                    else{
                        input_saldo_akhir.setText("0");
                    }
                }

                input_jumlah_penarikan.addTextChangedListener(this);
            }
        });

        tampil_nama();

        btn_penarikan = findViewById(R.id.btnproses);
        btn_penarikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String susername = input_nama.getText().toString();
                String stanggal = input_tanggal.getText().toString();
                String sjumlah_penarikan = input_jumlah_penarikan.getText().toString();
                String ssaldo_awal = input_saldo_awal.getText().toString();
                String ssaldo_akhir = input_saldo_akhir.getText().toString();
                String max = "500000";


                int sa = Integer.parseInt(ssaldo_awal);
                int ju = Integer.parseInt(max);

                boolean b = sa <= ju;


                if (TextUtils.isEmpty(susername)){
                    input_nama.requestFocus();
                    input_nama.setError("Silahkan input nama");
                }else if(TextUtils.isEmpty(stanggal)){
                    input_tanggal.requestFocus();
                    input_tanggal.setError("Silahkan input tanggal");
                }else if(TextUtils.isEmpty(sjumlah_penarikan)){
                    input_jumlah_penarikan.requestFocus();
                    input_jumlah_penarikan.setError("Silahkan input tanggal");
                }else if(TextUtils.isEmpty(ssaldo_awal)){
                    input_saldo_awal.requestFocus();
                    input_saldo_awal.setError("Silahkan input tanggal");
                }else if(TextUtils.isEmpty(ssaldo_akhir)){
                    input_saldo_akhir.requestFocus();
                    input_saldo_akhir.setError("Silahkan input tanggal");
                } else if (b == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(transaksi_penarikan_saldo.this);
                    builder.setMessage("Opps Saldo Anda Tidak Cukup, Saldo Anda Kurang dari Rp 500000 !!!");
                    builder.setNegativeButton("Ok !!.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            input_saldo_awal.requestFocus();
                            input_saldo_awal.setError("Sldo tidak cukup");

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else{
                        cekstatus(susername, "Sedang Diproses !!");
                }
            }
        });


    }

    public void creatpenarikan(String username, String tanggal, String jumlah_penarikan, String saldo_awal, String saldo_akhir, String status){
        if (cekNetworkConnection()){
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, Db_Contract.SERVER_CREATE_PENARIKAN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"success\"}]")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(transaksi_penarikan_saldo.this);
                                    builder.setMessage("Input Data Penarikan Berhasil " );
                                    builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), home_penarikan_saldo.class);
                                            startActivity(intent);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Input Sampah Gagal", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(transaksi_penarikan_saldo.this, "GAGAL CREAT INPUT PENARIKAN", Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_user", getIntent().getStringExtra("id_user"));
                    params.put("username", username);
                    params.put("tanggal", tanggal);
                    params.put("jumlah_penarikan", jumlah_penarikan);
                    params.put("saldo_awal", saldo_awal);
                    params.put("saldo_akhir", saldo_akhir);
                    params.put("status", status);

                    return params;

                }
            };
            VolleyConnection.getInstance(transaksi_penarikan_saldo.this).addToRequesQue(request);
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


    public void cekstatus(String username, String status) {
        if (cekNetworkConnection()) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, Db_Contract.SERVER_CEK_STATUS_PENARIKAN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_respon");
                                if (resp.equals("[{\"status\":\"in-progres\"}]")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(transaksi_penarikan_saldo.this);
                                    builder.setMessage("Mohon Maaf Penarikan Tidak dapat Di Proses !!, " +
                                            "Karena Masih Ada Data Yang Belum Dikonfirmasi Oleh Admin ");
                                    builder.setPositiveButton("Ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String name = getIntent().getStringExtra("username");
                                            Intent intent = new Intent(getApplicationContext(), RiwayatTransaksiSampah.class);
                                            intent.putExtra("username", name);
                                            startActivity(intent);
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                } else {
                                    String susername = input_nama.getText().toString();
                                    String stanggal = input_tanggal.getText().toString();
                                    String sjumlah_penarikan = input_jumlah_penarikan.getText().toString();
                                    String ssaldo_awal = input_saldo_awal.getText().toString();
                                    String ssaldo_akhir = input_saldo_akhir.getText().toString();

                                    creatpenarikan(susername, stanggal, sjumlah_penarikan, ssaldo_awal, ssaldo_akhir, "Sedang Diproses !!");
                                }

                            } catch (JSONException e) {
                                Toast.makeText(transaksi_penarikan_saldo.this, "GAGAl Cek status", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(transaksi_penarikan_saldo.this, "GAGAL CEK STTA ", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("status", status);
                    return params;
                }
            };
            VolleyConnection.getInstance(transaksi_penarikan_saldo.this).addToRequesQue(stringRequest);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !!", Toast.LENGTH_SHORT).show();
        }

    }
    public void total_saldo_akhir(int count_jmlPenarikan){
        String saldoawal = input_saldo_awal.getText().toString();
        int count_harga = Integer.parseInt(saldoawal);
        count_saldo_akhir = (count_harga-count_jmlPenarikan);
        String jumlah = String.valueOf(count_saldo_akhir);
        input_saldo_akhir.setText(jumlah);
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

    void tampil_nama(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Db_Contract.SERVER_TAMPIL_NAMA_LOGIN + getIntent().getStringExtra("username"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            input_nama.setText(jsonObject.get("username").toString());
                            input_saldo_awal.setText(jsonObject.get("saldo").toString());

                            String id_user = jsonObject.get("id_user").toString();
                            getIntent().putExtra("id_user", id_user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(transaksi_penarikan_saldo.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


}