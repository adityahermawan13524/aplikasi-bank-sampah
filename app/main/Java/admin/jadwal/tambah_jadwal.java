package com.dev.banksampahdigital.admin.View.jadwal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.admin.View.input.input_sampah_admin;
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class tambah_jadwal extends AppCompatActivity {

    private TextInputEditText inputtanggal, inputhari, inputwaktuawal, inputwaktuakhir, inputtempat, inputcatatan;
    private Button btntambahjadwal;
    DatePickerDialog inputgl;
    Toolbar toolbar;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah_jadwal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(tambah_jadwal.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan Data...");

        toolbar = findViewById(R.id.toolbar_tambah_jadwal);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        inputtanggal = findViewById(R.id.inputtanggal);
        inputhari = findViewById(R.id.inputhari);
        inputwaktuawal = findViewById(R.id.inputwaktuawal);
        inputwaktuakhir = findViewById(R.id.inputwaktuakhir);
        inputtempat = findViewById(R.id.tempatpelaksanaan);
        inputcatatan = findViewById(R.id.jcatatan);

        btntambahjadwal = findViewById(R.id.btninptjadwal);

        inputtanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettanggal();
            }
        });

        inputwaktuawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeawal();
            }
        });

        inputwaktuakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeakhir();
            }
        });

        btntambahjadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stanggal = inputtanggal.getText().toString();
                String shari = inputhari.getText().toString();
                String swaktuawal = inputwaktuawal.getText().toString();
                String swaktuakhir = inputwaktuakhir.getText().toString();
                String stempat = inputtempat.getText().toString();
                String scatatn = inputcatatan.getText().toString();

                if (TextUtils.isEmpty(stanggal)){
                    inputtanggal.setError("Tanggal Harus Diisi");
                    inputtanggal.requestFocus();
                }else if (TextUtils.isEmpty(shari)){
                    inputhari.setError("Tanggal Harus Diisi");
                    inputhari.requestFocus();
                }else if (TextUtils.isEmpty(swaktuawal)){
                    inputwaktuawal.setError("Tanggal Harus Diisi");
                    inputwaktuawal.requestFocus();
                }else if (TextUtils.isEmpty(swaktuakhir)){
                    inputwaktuakhir.setError("Tanggal Harus Diisi");
                    inputwaktuakhir.requestFocus();
                }else if (TextUtils.isEmpty(stempat)){
                    inputtempat.setError("Tanggal Harus Diisi");
                    inputtempat.requestFocus();
                }else{
                    inputjadwal(stanggal,shari, swaktuawal, swaktuakhir, stempat, scatatn);
                }

            }
        });
    }


    public void inputjadwal(String tanggal, String hari,
                            String waktu_awal, String waktu_akhir,
                            String tempat, String catatan){
        if (cekNetworkConnection()){
            progressDialog.show();
            StringRequest request = new StringRequest(
                    Request.Method.POST, Db_Contract.SERVER_CREATE_JADWAL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"success\"}]")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(tambah_jadwal.this);
                                    builder.setMessage("Input Jadwal Berhasil");
                                    builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), jadwal_admin.class);
                                            sendJadwal();
                                            startActivity(intent);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }else{
                                    Toast.makeText(tambah_jadwal.this, "GAGAL CREATE TO DB", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(tambah_jadwal.this, "ERR", Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("tanggal", tanggal);
                    params.put("hari", hari);
                    params.put("waktu_awal", waktu_awal);
                    params.put("waktu_akhir", waktu_akhir);
                    params.put("tempat", tempat);
                    params.put("catatan", catatan);
                    return params;
                }
            };
            VolleyConnection.getInstance(tambah_jadwal.this).addToRequesQue(request);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        }else{
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !!", Toast.LENGTH_SHORT).show();
        }
    }


    private void timeawal(){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.SECOND);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        inputwaktuawal.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }


    private void timeakhir(){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.SECOND);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        inputwaktuakhir.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }


    public void gettanggal(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        SimpleDateFormat formathari = new SimpleDateFormat("EEEE", new Locale("in", "ID"));


        inputgl = new DatePickerDialog(tambah_jadwal.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                inputtanggal.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

            }
        }, year, month, day);
        inputgl.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        inputgl.show();
    }


    public Boolean cekNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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

    public void sendJadwal(){
        String stanggal = inputtanggal.getText().toString();
        String shari = inputhari.getText().toString();
        String swaktuawal = inputwaktuawal.getText().toString();
        String swaktuakhir = inputwaktuakhir.getText().toString();
        String stempat = inputtempat.getText().toString();
        String scatatn = inputcatatan.getText().toString();


        String body = shari + "\n" + stanggal + "\n" + swaktuawal + " S/d " + swaktuakhir + "\n" + stempat
                                    + "\n" + scatatn ;

        String token = "/topics/bank_sampah";
        String titel = "Jadwal Bank Sampah";
        StringRequest request = new StringRequest(Request.Method.POST,
                Db_Contract.SERVER_SENDNOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("result","result: done! "+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(tambah_jadwal.this, "terjadi kesalahan Notification", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("title", titel);
                params.put("body", body);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(tambah_jadwal.this);
        requestQueue.add(request);
    }
}