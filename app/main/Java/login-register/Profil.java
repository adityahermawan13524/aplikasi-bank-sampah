package com.dev.banksampahdigital.LoginRegister;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import com.dev.banksampahdigital.adapter.Objek;
import com.dev.banksampahdigital.admin.View.data_nasabah.lihat_data_nasabah;
import com.dev.banksampahdigital.admin.View.data_nasabah.update_data_nasabah;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Profil extends AppCompatActivity {

    TextView tvNama, tvsaldo, tvemail, tvdob, tvalamat, tvgender, tvmobile,tvpassword, tvstatus;
    Button edit, logout;
    ArrayList<Objek> model;
    ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "username";
    private static final String KEY_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(Profil.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mohon Tunggu...");

        tvNama = findViewById(R.id.tmpl_Nama_user);
        tvemail = findViewById(R.id.tv_email_tampil);
        tvdob = findViewById(R.id.tv_dob_tampil);
        tvalamat = findViewById(R.id.tv_alamat_tampil);
        tvgender = findViewById(R.id.tv_gender_tampil);
        tvmobile = findViewById(R.id.tv_NoHp_tampil);
        tvpassword = findViewById(R.id.tv_password_tampil);
        tvstatus = findViewById(R.id.tv_status_tampil);
        edit = findViewById(R.id.btnedit);
        logout = findViewById(R.id.btnlogout);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("username");
                Intent intent = new Intent(getApplicationContext(), update_profil.class);
                intent.putExtra("user", name);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profil.this);
                builder.setMessage("Apakah Anda Ingin Keluar?");
                builder.setPositiveButton("Iya.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutUser();
                    }
                });
                builder.setNegativeButton("Tidak.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        tampil_data();

    }

    void tampil_data(){
        StringRequest request = new StringRequest(
                Request.Method.GET, Db_Contract.SERVER_TAMPIL_NAMA_LOGIN + getIntent().getStringExtra("username"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvNama.setText(jsonObject.getString("username"));
                            tvemail.setText(jsonObject.getString("email"));
                            tvdob.setText(jsonObject.getString("dob"));
                            tvalamat.setText(jsonObject.getString("alamat"));
                            tvgender.setText(jsonObject.getString("gender"));
                            tvmobile.setText(jsonObject.getString("mobile"));
                            tvpassword.setText(jsonObject.getString("password"));
                            tvstatus.setText(jsonObject.getString("status"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profil.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        startActivity(new Intent(this, LoginActivity.class));
        finish();



    }
}