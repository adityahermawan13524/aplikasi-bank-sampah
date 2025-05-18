package com.dev.banksampahdigital.View.history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
import com.dev.banksampahdigital.View.main.HomeActivity;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class detail_penarikan_user extends AppCompatActivity {
    private TextView tvstatus, tvnama, tvjumlahpenarikan, tvsaldoawal, tvtanggal, tvotp, tvsaldo;
    private Button btnback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_penarikan_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvstatus = findViewById(R.id.d_stts);
        tvnama = findViewById(R.id.d_nama);
        tvjumlahpenarikan = findViewById(R.id.djmlpnrkn);
        tvsaldoawal = findViewById(R.id.dsaldo_awal);
        tvtanggal = findViewById(R.id.d_Tanggal);
        tvsaldo = findViewById(R.id.d_jumlah);
        tvotp = findViewById(R.id.otp);

        btnback = findViewById(R.id.kembali);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textuser = tvnama.getText().toString();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("username", textuser);
                startActivity(intent);
                finish();
            }
        });

        tampil();

    }

    public void tampil(){
        StringRequest request = new StringRequest(Request.Method.GET,
                Db_Contract.SERVER_TAMPIL_DATA_PENARIKAN + getIntent().getStringExtra("id_penarikan"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvnama.setText(jsonObject.getString("username"));
                            tvtanggal.setText(jsonObject.getString("tanggal"));
                            tvsaldoawal.setText(jsonObject.getString("saldo_awal"));
                            tvjumlahpenarikan.setText(jsonObject.getString("jumlah_penarikan"));
                            tvsaldo.setText(jsonObject.getString("saldo_akhir"));
                            tvotp.setText(jsonObject.getString("otp_code"));
                            tvstatus.setText(jsonObject.getString("status"));

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
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}