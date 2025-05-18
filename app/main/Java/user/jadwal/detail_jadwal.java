package com.dev.banksampahdigital.View.jadwal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class detail_jadwal extends AppCompatActivity {
    private TextView tvhari, tvtanggal, tvwaktu, tvtempat, tvcatatan;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_jadwal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvhari = findViewById(R.id.tv_j_hari);
        tvtanggal = findViewById(R.id.tv_j_tanggal);
        tvwaktu = findViewById(R.id.tv_jwaktu);
        tvtempat = findViewById(R.id.tv_j_tempat);
        tvcatatan = findViewById(R.id.tv_j_catatan);

        tmpljadwal();

    }

    public void tmpljadwal(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, Db_Contract.SERVER_TMPL_DATA_JADWAL + getIntent().getStringExtra("id_jadwal"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvhari.setText(jsonObject.getString("Hari"));
                            tvtanggal.setText(jsonObject.getString("tanggal"));
                            tvwaktu.setText(jsonObject.getString("waktu_awal") +" S/d " + jsonObject.getString("waktu_akhir"));
                            tvtempat.setText(jsonObject.getString("tempat"));
                            tvcatatan.setText(jsonObject.getString("catatan"));
                        } catch (JSONException e) {
                            Toast.makeText(detail_jadwal.this, "GAGAL AMBIIL DATA", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(detail_jadwal.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}