package com.dev.banksampahdigital.admin.View.transaksi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.dev.banksampahdigital.admin.View.menu_admin.home_admin;
import com.dev.banksampahdigital.admin.View.penarikan_saldo.konfirmasi_penarikan;
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class konfirmasi_jemput_sampah extends AppCompatActivity {

    private TextView tvstatus, tvnama, tvkategori, tvberat, tvhargaperkg, tvpajak, tvtanggal, tvtotalharga, tvsaldo;
    private Button btnproses, btnditolak, btnback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_konfirmasi_jemput_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvstatus = findViewById(R.id.d_stts);
        tvnama = findViewById(R.id.d_nama);
        tvkategori = findViewById(R.id.d_kategori);
        tvberat = findViewById(R.id.d_berat);
        tvhargaperkg = findViewById(R.id.d_hargaperkg);
        tvpajak = findViewById(R.id.d_pajak);
        tvtanggal = findViewById(R.id.d_Tanggal);
        tvtotalharga = findViewById(R.id.d_jmlhargasmph);
        tvsaldo = findViewById(R.id.d_jumlah);


        btnback = findViewById(R.id.kembali);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("usert");
                Intent intent = new Intent(getApplicationContext(), home_admin.class);
                intent.putExtra("username", name);
                startActivity(intent);
                finish();
            }
        });

        tmpldtl();


    }

    public void tmpldtl(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, Db_Contract.SERVER_TMPL_DTL_INPT_SMPH + getIntent().getStringExtra("id_input_smph"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvstatus.setText(jsonObject.getString("status"));
                            tvnama.setText(jsonObject.getString("username"));
                            tvkategori.setText(jsonObject.getString("kategori_sampah"));
                            tvberat.setText(jsonObject.getString("berat") + "Kg");
                            tvhargaperkg.setText("Rp. " + jsonObject.getString("harga_perKg"));
                            tvpajak.setText("Rp. " + jsonObject.getString("pajak"));
                            tvtanggal.setText(jsonObject.getString("tanggal"));
                            tvtotalharga.setText("Rp. " + jsonObject.getString("harga"));
                            tvsaldo.setText(jsonObject.getString("saldo"));

                            int colorblue = Color.rgb(0,0, 255);
                            int colorred = Color.RED;
                            int colorjingga = Color.rgb(255,153,0);

                            String stts = tvstatus.getText().toString();

                            if (stts.equals("Sukses")){
                                tvstatus.setTextColor(colorblue);
                            }else if (stts.equals("Di-Tolak")) {
                                tvstatus.setTextColor(colorred);
                            }else if (stts.equals("In-Progres")){
                                tvstatus.setTextColor(colorjingga);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(konfirmasi_jemput_sampah.this, "Gagal ambil Data", Toast.LENGTH_SHORT).show();
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

    private void updatesaldo( final String saldo){
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
//                            Toast.makeText(konfirmasi_jemput_sampah.this, "GAGAL_UPDATE SALDO", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(konfirmasi_jemput_sampah.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("id_user", getIntent().getStringExtra("id_user"));
                params.put("username", getIntent().getStringExtra("username"));
                params.put("saldo", saldo);
                return params;
            }
        };
        VolleyConnection.getInstance(konfirmasi_jemput_sampah.this).addToRequesQue(stringRequest);
    }

    public void update_status(String username, String status){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_UPDATE_STTS_INPUT_SAMPAH,
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
//                            Toast.makeText(konfirmasi_jemput_sampah.this, "GAGAL_UPDATE SALDO", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(konfirmasi_jemput_sampah.this, "GAGALUPDATE STTS", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_input_smph", getIntent().getStringExtra("id_input_smph"));
                params.put("username", username);
                params.put("status", status);
                return params;
            }
        };
        VolleyConnection.getInstance(konfirmasi_jemput_sampah.this).addToRequesQue(stringRequest);
    }
}