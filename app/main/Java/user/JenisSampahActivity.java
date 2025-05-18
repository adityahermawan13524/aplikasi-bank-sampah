package com.dev.banksampahdigital.View.jenis;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
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
import com.dev.banksampahdigital.adapter.Objek_data_sampah;
import com.dev.banksampahdigital.adapter.adapter_data_sampah;
import com.dev.banksampahdigital.admin.View.data_sampah.data_sampah;
import com.dev.banksampahdigital.admin.View.data_sampah.lihat_data_sampah;
import com.dev.banksampahdigital.admin.View.data_sampah.tambah_data_sampah;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JenisSampahActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jenis_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.detailsampah);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = findViewById(R.id.list_jenisSampah_user);
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
                            final adapter_data_sampah adapter = new adapter_data_sampah(JenisSampahActivity.this, model);
                            listView.setAdapter(adapter);


                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(JenisSampahActivity.this);
                            builder.setMessage("Opps Data Masih Kosong !!!");
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
                Toast.makeText(JenisSampahActivity.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
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
}