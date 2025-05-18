package com.dev.banksampahdigital.admin.View.menu_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.LoginRegister.Profil;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.View.main.HomeActivity;
import com.dev.banksampahdigital.admin.View.data_nasabah.data_nasabah;
import com.dev.banksampahdigital.admin.View.data_sampah.data_sampah;
import com.dev.banksampahdigital.admin.View.informasi_bank_sampah.informasi_bank_sampah;
import com.dev.banksampahdigital.admin.View.input.input_sampah_admin;
import com.dev.banksampahdigital.admin.View.jadwal.jadwal_admin;
import com.dev.banksampahdigital.admin.View.penarikan_saldo.home_penarikan_saldo;
import com.dev.banksampahdigital.admin.View.transaksi.detail_transaksi_input_sampah_admin;
import com.dev.banksampahdigital.database.Db_Contract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class home_admin extends AppCompatActivity {

    private CardView cvInput, cvDataNasabah, cvJenisSampah, cvTransaksi, cvPenarikan, cvjadwal, cvInformasi;
    private ImageView profil;
    private TextView nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nama = findViewById(R.id.namaAdmin);


        profil = findViewById(R.id.imageProfileAdmin);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = nama.getText().toString();
                Intent intent = new Intent(getApplicationContext(), Profil.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        cvInput = findViewById(R.id.cvInputAdmin);
        cvDataNasabah = findViewById(R.id.cvUserNasabah);
        cvJenisSampah = findViewById(R.id.cvJenisSampahAdm);
        cvTransaksi = findViewById(R.id.cvTransaksi);
        cvPenarikan = findViewById(R.id.cvPenarikan);
        cvInformasi = findViewById(R.id.cvInformasi);

        cvInput.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), input_sampah_admin.class));
        });

        cvDataNasabah.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), data_nasabah.class));
        });

        cvTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nama.getText().toString();
                Intent intent = new Intent(getApplicationContext(), detail_transaksi_input_sampah_admin.class);
                intent.putExtra("usert", name);
                startActivity(intent);
            }
        });

        cvPenarikan.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), home_penarikan_saldo.class));
        });

        cvJenisSampah.setOnClickListener(v ->{
            startActivity(new Intent(getApplicationContext(), data_sampah.class));
        });

        cvjadwal = findViewById(R.id.cvJadwal);
        cvjadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), jadwal_admin.class);
                startActivity(intent);
            }
        });

        cvInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), informasi_bank_sampah.class);
                startActivity(intent);
            }
        });

        tampil_nama_login();


        subscribeadmin();
    }

    public void tampil_nama_login(){
        StringRequest request = new StringRequest(
                Request.Method.GET, Db_Contract.SERVER_TAMPIL_NAMA_LOGIN + getIntent().getStringExtra("username"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            nama.setText(jsonObject.getString("username"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home_admin.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void subscribeadmin(){
        FirebaseMessaging.getInstance().subscribeToTopic("admin")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                    }
                });
    }
}