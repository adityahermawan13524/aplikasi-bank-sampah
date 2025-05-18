package com.dev.banksampahdigital.admin.View.input;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.dev.banksampahdigital.adapter.adpter_cari_username;
import com.dev.banksampahdigital.database.Db_Contract;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cari_nama extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    TextInputEditText Nama;
    TextView tvsaldo;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cari_nama);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Nama = findViewById(R.id.crnama);
        listView = findViewById(R.id.list_nama);
        toolbar = findViewById(R.id.toolbar);
//        tvsaldo = findViewById(R.id.tvSaldo);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), input_sampah_admin.class);
                intent.putExtra("id_user", model.get(position).getId_user());
                startActivity(intent);
                finish();
            }
        });


        Nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cari_nama(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        load();
    }

    ArrayList<Objek> model;

    void cari_nama(String cari_nama){

        StringRequest request = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_CARI_NAMA_URL+cari_nama, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");

                    model = new ArrayList<>();

                    for (int i = 0; i<getHasil.length();i++){
                        JSONObject getData = getHasil.getJSONObject(i);
                        String id_user = getData.getString("id_user");
                        String username = getData.getString("username");
                        String alamat = getData.getString("alamat");

                        model.add(new Objek(id_user,username, alamat));

                    }
                    final adpter_cari_username adapter = new adpter_cari_username(cari_nama.this, model);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cari_nama.this, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    void tampil_saldo(){
        StringRequest request = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_CREATE_SALDO + getIntent().getStringExtra("id_user"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvsaldo.setText(jsonObject.get("saldo_akhir").toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cari_nama.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    void load(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Db_Contract.SERVER_DATA_NASABAH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray getHasil = jsonObject.getJSONArray("hasil");

                            model = new ArrayList<>();

                            for (int i = 0; i<getHasil.length();i++){
                                JSONObject getData = getHasil.getJSONObject(i);
                                String id_user = getData.getString("id_user");
                                String username = getData.getString("username");
                                String alamat = getData.getString("alamat");
                                model.add(new Objek(id_user,username, alamat));

                            }
                            final adpter_cari_username adapter = new adpter_cari_username(cari_nama.this, model);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(cari_nama.this, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
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