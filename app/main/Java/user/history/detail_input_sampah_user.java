package com.dev.banksampahdigital.View.history;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.dev.banksampahdigital.View.main.HomeActivity;
import com.dev.banksampahdigital.admin.View.transaksi.detail_transaksi_input_sampah_admin;
import com.dev.banksampahdigital.admin.View.transaksi.konfirmasi_jemput_sampah;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class detail_input_sampah_user extends AppCompatActivity {

    private TextView tvstatus, tvnama, tvkategori, tvberat, tvhargaperkg, tvpajak, tvtanggal, tvtotalharga, tvsaldo;
    private Button  btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_input_sampah_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvstatus = findViewById(R.id.d_stts);
        tvnama = findViewById(R.id.d_nama);
        tvkategori = findViewById(R.id.d_kategori);
        tvberat = findViewById(R.id.d_berat);
        tvhargaperkg = findViewById(R.id.d_hargaperkg);
        tvpajak = findViewById(R.id.d_pajak);
        tvtanggal = findViewById(R.id.d_Tanggal);
        tvtotalharga = findViewById(R.id.d_jmlhargasmph);
        tvsaldo = findViewById(R.id.d_jumlah);

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

        tmpldtl();

    }

    public void tmpldtl(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, Db_Contract.SERVER_TMPL_DTL_INPT_SMPH + getIntent().getStringExtra("id_input_smph"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            tvstatus.setText(jsonObject.getString("status"));
                            tvnama.setText(jsonObject.getString("username"));
                            tvkategori.setText(jsonObject.getString("kategori_sampah"));
                            tvberat.setText(jsonObject.getString("berat") + "Kg");
                            tvhargaperkg.setText("Rp. " + jsonObject.getString("harga_perKg"));
                            tvpajak.setText("Rp. " + jsonObject.getString("pajak"));
                            tvtanggal.setText(jsonObject.getString("tanggal"));
                            tvtotalharga.setText("Rp. " + jsonObject.getString("harga"));
                            tvsaldo.setText("Rp. " + jsonObject.getString("saldo"));

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
                            Toast.makeText(detail_input_sampah_user.this, "Gagal ambil Data", Toast.LENGTH_SHORT).show();
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
}