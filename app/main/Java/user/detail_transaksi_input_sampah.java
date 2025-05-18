package com.dev.banksampahdigital.View.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.dev.banksampahdigital.adapter.Objek_transaki;
import com.dev.banksampahdigital.adapter.adapter_transaksi;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class detail_transaksi_input_sampah extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_transaksi_input_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbardtljmptsmph);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = findViewById(R.id.list_detail_jemput_sampah);

        load();

    }

    ArrayList<Objek_transaki> model;
    public void load(){
        StringRequest request = new StringRequest(
                Request.Method.GET, Db_Contract.SERVER_TRANSAKSI_USER + getIntent().getStringExtra("username"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray getHasil = jsonObject.getJSONArray("hasil");

                            model = new ArrayList<>();
                            for (int i = 0; i<getHasil.length();i++) {
                                JSONObject getData = getHasil.getJSONObject(i);
                                String id_input_smph = getData.getString("id_input_smph");
                                String id_user = getData.getString("id_user");
                                String username = getData.getString("username");
                                String kategori_sampah = getData.getString("kategori_sampah");
                                String berat = getData.getString("berat");
                                String harga_perKg = "Rp, " + getData.getString("harga_perKg");
                                String pajak = "Rp, " + getData.getString("pajak");
                                String harga ="Rp, " + getData.getString("harga");
                                String tanggal = getData.getString("tanggal");
                                String alamat = getData.getString("alamat");
                                String status = getData.getString("status");
                                model.add(new Objek_transaki(
                                        id_input_smph,
                                        "",
                                        username,
                                        kategori_sampah,
                                        berat,
                                        harga_perKg,
                                        pajak,
                                        harga,
                                        tanggal,
                                        alamat,
                                        "",
                                        "",
                                        "",
                                        status
                                ));
                            }
                            final adapter_transaksi adapter = new adapter_transaksi(detail_transaksi_input_sampah.this, model);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getApplicationContext(), detail_input_sampah_user.class);
                                    intent.putExtra("id_input_smph", model.get(position).getId_input_smph());
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(detail_transaksi_input_sampah.this);
                            builder.setMessage("Riwayat Transaksi Kosong");
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
                Toast.makeText(detail_transaksi_input_sampah.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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