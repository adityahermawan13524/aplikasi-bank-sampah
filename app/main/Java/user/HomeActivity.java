package com.dev.banksampahdigital.View.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.LoginRegister.Profil;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.View.history.RiwayatTransaksiSampah;
import com.dev.banksampahdigital.View.history.detail_transaksi_input_sampah;
import com.dev.banksampahdigital.View.input.jemput_sampah;
import com.dev.banksampahdigital.View.jadwal.detail_jadwal;
import com.dev.banksampahdigital.View.jadwal.jadwal_user;
import com.dev.banksampahdigital.View.jenis.JenisSampahActivity;
import com.dev.banksampahdigital.admin.View.informasi_bank_sampah.informasi_bank_sampah;
import com.dev.banksampahdigital.admin.View.jadwal.jadwal_admin;
import com.dev.banksampahdigital.database.Db_Contract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends AppCompatActivity {

    private TextView name;
    private ImageView profil;
    private CardView cvinput, cvkategori, cvHistory, cvdtljmpt, cvjadwal, cvinformasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);


        name = findViewById(R.id.nama);


        //Button Keluar Sementara
        profil = findViewById(R.id.imageProfile);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                Intent intent = new Intent(getApplicationContext(), Profil.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

//        cvinput = findViewById(R.id.cvInput);
//        cvinput.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = name.getText().toString();
//                Intent intent = new Intent(getApplicationContext(), jemput_sampah.class);
//                intent.putExtra("username", username);
//                startActivity(intent);
//            }
//        });

        cvkategori = findViewById(R.id.cvKategori);
        cvkategori.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), JenisSampahActivity.class));
        });

        cvdtljmpt = findViewById(R.id.cvdtljmptsmph);
        cvdtljmpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                Intent intent = new Intent(getApplicationContext(), detail_transaksi_input_sampah.class);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });

        cvHistory = findViewById(R.id.cvTransaksi);
        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                Intent intent = new Intent(getApplicationContext(), RiwayatTransaksiSampah.class);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });
         cvjadwal = findViewById(R.id.cvJadwal);
         cvjadwal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), jadwal_user.class);
                 startActivity(intent);
             }
         });

         cvinformasi = findViewById(R.id.cvInformasi);
         cvinformasi.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), informasi_bank_sampah.class);
                 startActivity(intent);
             }
         });

        tampil_nama_login();

        subscribeToTopic();

    }

    public void tampil_nama_login(){
        StringRequest request = new StringRequest(
                Request.Method.GET, Db_Contract.SERVER_TAMPIL_NAMA_LOGIN + getIntent().getStringExtra("username"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            name.setText(jsonObject.getString("username"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("bank_sampah")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                    }
                });
    }
}