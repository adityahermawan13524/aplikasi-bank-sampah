package com.dev.banksampahdigital.admin.View.data_sampah;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.dev.banksampahdigital.adapter.Objek;
import com.dev.banksampahdigital.adapter.Objek_data_sampah;
import com.dev.banksampahdigital.adapter.adapter_data_sampah;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class data_sampah extends AppCompatActivity {

    Toolbar toolbar;
    Button tambah_data_sampah;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbardatasampah);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tambah_data_sampah = findViewById(R.id.tambah_data_sampah);
        tambah_data_sampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.dev.banksampahdigital.admin.View.data_sampah.tambah_data_sampah.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.list_data_sampah);
        load();


    }

    ArrayList<Objek_data_sampah> model;
    public void load(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_DATA_SAMPAH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray gethasil = jsonObject.getJSONArray("hasil");
                            model = new ArrayList<>();

                            for (int i = 0; i <gethasil.length();i++){
                                JSONObject getData = gethasil.getJSONObject(i);
                                String id_sampah = getData.getString("id_sampah");
                                String jenis_sampah = getData.getString("jenis_sampah");
                                String harga_perKg = getData.getString("harga_perKg");

                                model.add(new Objek_data_sampah(id_sampah,jenis_sampah,harga_perKg));
                            }
                            final adapter_data_sampah adapter = new adapter_data_sampah(data_sampah.this, model);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getApplicationContext(), lihat_data_sampah.class);
                                    intent.putExtra("id_sampah", model.get(position).getId_sampah());
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(data_sampah.this);
                            builder.setMessage("Opps Data Masih Kosong, Silahkan Tambahkan Data !!!");
                            builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), tambah_data_sampah.class);
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
                Toast.makeText(data_sampah.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
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