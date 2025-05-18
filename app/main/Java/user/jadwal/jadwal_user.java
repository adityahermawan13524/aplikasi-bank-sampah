package com.dev.banksampahdigital.View.jadwal;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.adapter.Objek_Jadwal;
import com.dev.banksampahdigital.adapter.adapter_jadwal;
import com.dev.banksampahdigital.admin.View.data_sampah.tambah_data_sampah;
import com.dev.banksampahdigital.admin.View.jadwal.jadwal_admin;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class jadwal_user extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jadwal_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbarjadwaluser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = findViewById(R.id.list_jadwal);
        load();
    }


    ArrayList<Objek_Jadwal> model;
    public void load(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_DATA_JADWAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray gethasil = jsonObject.getJSONArray("hasil");
                            model = new ArrayList<>();

                            for (int i = 0; i <gethasil.length();i++){
                                JSONObject getData = gethasil.getJSONObject(i);
                                String id_jadwal = getData.getString("id_jadwal");
                                String Hari= getData.getString("Hari");
                                String tanggal = getData.getString("tanggal");
                                String waktu_awal = getData.getString("waktu_awal");
                                String waktu_akhir = getData.getString("waktu_akhir");
                                String tempat = getData.getString("tempat");
                                String catatan = getData.getString("catatan");

                                model.add(new Objek_Jadwal(id_jadwal, Hari, tanggal, waktu_awal, waktu_akhir, tempat, catatan));
                            }

                            final adapter_jadwal adapter = new adapter_jadwal(jadwal_user.this, model);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getApplicationContext(), detail_jadwal.class);
                                    intent.putExtra("id_jadwal", model.get(position).getId_jadwal());
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(jadwal_user.this);
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
                Toast.makeText(jadwal_user.this, "ERROR", Toast.LENGTH_SHORT).show();
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