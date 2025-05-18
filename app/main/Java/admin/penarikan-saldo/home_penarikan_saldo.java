package com.dev.banksampahdigital.admin.View.penarikan_saldo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.adapter.Objek_penarikan;
import com.dev.banksampahdigital.adapter.adapter_penarikan;
import com.dev.banksampahdigital.admin.View.data_sampah.data_sampah;
import com.dev.banksampahdigital.admin.View.data_sampah.lihat_data_sampah;
import com.dev.banksampahdigital.admin.View.data_sampah.tambah_data_sampah;
import com.dev.banksampahdigital.database.Db_Contract;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class home_penarikan_saldo extends AppCompatActivity {

    Toolbar toolbar;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_penarikan_saldo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbarlihatdatapenarikan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        listView = findViewById(R.id.list_data_penarikan);
        load();
    }

    ArrayList<Objek_penarikan>model;
    public void load(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_DATA_PENARIKAN,
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

                                String otp = getData.getString("otp_code");

                                model.add(new Objek_penarikan(id_penarikan, id_user, username,tanggal,jumlah_penarikan,"",saldo,status, otp));
                            }

                            final adapter_penarikan adapter = new adapter_penarikan(home_penarikan_saldo.this, model);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getApplicationContext(), konfirmasi_penarikan.class);
                                    intent.putExtra("id_penarikan", model.get(position).getId_penarikan());
                                    intent.putExtra("id_user", model.get(position).getId_user());
                                    intent.putExtra("otp_code", model.get(position).getOtp_code());
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(home_penarikan_saldo.this);
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