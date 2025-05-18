package com.dev.banksampahdigital.View.input;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.View.main.HomeActivity;
import com.dev.banksampahdigital.admin.View.input.input_sampah_admin;
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class jemput_sampah extends AppCompatActivity {

    Toolbar toolbar;
    private TextInputEditText inputNama, inputBerat, inputHargaperKg, inputPajak, inputTotalHarga, inputSaldoAwal, inputSaldoAkhir, inputTanggal, inputAlamat, inputCatatan;
    private Button jemput;
    private DatePickerDialog inputgl;
    private Spinner skategorispinner;
    private ProgressDialog progressDialog;
    double jumlahHarga = 0, countTotal = 0, countHarga = 0, countBerat = 0, countsaldo = 0;
    String sKategoriSelected;
    ArrayList<String> KategroiSampahList = new ArrayList<>();
    ArrayAdapter<String> KategoryAdapter;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jemput_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar_jemput_sampah);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(jemput_sampah.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan Data...");

        inputNama = findViewById(R.id.inputNamaUser);
        inputBerat = findViewById(R.id.inputBeratUser);
        inputHargaperKg = findViewById(R.id.inputHarga);
        inputPajak = findViewById(R.id.pajakharga);
        inputTotalHarga = findViewById(R.id.totalHarga);
        inputSaldoAwal = findViewById(R.id.saldo_awal);
        inputSaldoAkhir = findViewById(R.id.saldo_akhir);
        inputTanggal = findViewById(R.id.inputTanggal);
        inputAlamat = findViewById(R.id.inputAlamat);
        inputCatatan = findViewById(R.id.inputTambahan);

//      Spinner Kategori Sampah
        skategorispinner = findViewById(R.id.spKatergori);
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Db_Contract.SERVER_DATA_SAMPAH, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray gethasil = jsonObject.getJSONArray("hasil");
                            for (int i=0; i<gethasil.length();i++){
                                JSONObject getdata = gethasil.getJSONObject(i);
                                String kategori = getdata.optString("jenis_sampah");


                                KategroiSampahList.add(kategori);
                                KategoryAdapter = new ArrayAdapter<>(jemput_sampah.this,
                                        android.R.layout.simple_spinner_item, KategroiSampahList);
                                KategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                skategorispinner.setAdapter(KategoryAdapter);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(jemput_sampah.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

//      harga_perKg
        skategorispinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                sKategoriSelected = parent.getItemAtPosition(i).toString();

                if (parent.getId() == R.id.spKatergori){
                    String selectedJenis = parent.getSelectedItem().toString();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                            Db_Contract.SERVER_TAMPIL_DATA_SAMPAH_PINNER + selectedJenis,null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                if (inputBerat.getText().toString()!=""){
                                    inputHargaperKg.setText(jsonObject.getString("harga_perKg"));
                                }else{
                                    inputHargaperKg.setText("0");
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
                    requestQueue.add(jsonObjectRequest);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//      input tanggal pesanan
        inputTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                inputgl = new DatePickerDialog(jemput_sampah.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        inputTanggal.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                inputgl.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                inputgl.show();
            }
        });

        tampil_nama();

        //      input berat sampah
        inputBerat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputNama.getText().toString().isEmpty()){
                    inputNama.requestFocus();
                    inputNama.setError("Silahkan input nama");
                }else {
                    inputBerat.removeTextChangedListener(this);

                    if (s.length() > 0) {
//                        countBerat = Integer.parseInt(s.toString());
                        countBerat = Double.parseDouble(s.toString());
                        setTotalPajak(countBerat);
                        jumlahHarga(countBerat);
                        saldoakhir1(countBerat);
                    } else {
                        inputPajak.setText("0");
                        inputTotalHarga.setText("0");
                        inputSaldoAkhir.setText("0");
                    }
                    inputBerat.addTextChangedListener(this);
                }
            }
        });

        jemput = findViewById(R.id.btnjemput);
        jemput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textusername = inputNama.getText().toString();
                String textkategori = sKategoriSelected.toString();
                String textberat = inputBerat.getText().toString();
                String texthargaperkg = inputHargaperKg.getText().toString();
                String textpajak = inputPajak.getText().toString();
                String texttotalharga = inputTotalHarga.getText().toString();
                String textsaldoawal = inputSaldoAwal.getText().toString();
                String textsaldoakhir = inputSaldoAkhir.getText().toString();
                String texttanggal = inputTanggal.getText().toString();
                String textalamat = inputAlamat.getText().toString();
                String textcatatan = inputCatatan.getText().toString();

                if (TextUtils.isEmpty(textusername)){
                    inputNama.requestFocus();
                    inputNama.setError("silahkan inputkan nama");
                } else if (TextUtils.isEmpty(textberat)){
                    inputBerat.requestFocus();
                    inputBerat.setError("silahkan inputkan berat sampah");
                } else if (TextUtils.isEmpty(texttanggal)) {
                    inputTanggal.requestFocus();
                    inputTanggal.setError("silahkan inputkan tanggal");
                }else{
                   createdatainputsampahuser(textusername, textkategori, textberat, texthargaperkg, textpajak, texttotalharga,textsaldoakhir , texttanggal, textalamat, textcatatan, "In-Progres");
                   createsaldo(textusername, textsaldoawal, textsaldoakhir);
                }
            }
        });

    }

    public  void  createdatainputsampahuser(
            String username, String kategorisampah, String berat,
            String hargaperkg, String pajak, String totalharga,String saldo, String tanggal,
            String alamat, String catatan, String status ){
        if (cekNetworkConnection()){
            progressDialog.show();
            StringRequest request = new StringRequest(
                    Request.Method.POST, Db_Contract.SERVER_INPUT_SAMPAH_ADMIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"success\"}]")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(jemput_sampah.this);
                                    builder.setMessage("Input Sampah Berhasil");
                                    builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                            intent.putExtra("username", username);
                                            sendnotification();
                                            startActivity(intent);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }else{
                                    Toast.makeText(getApplicationContext(), "Input Sampah Gagal", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Input Sampah Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Input Sampah Gagal", Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("username", username);
                    params.put("kategori_sampah", kategorisampah);
                    params.put("berat", berat);
                    params.put("harga_perKg", hargaperkg);
                    params.put("pajak", pajak);
                    params.put("harga", totalharga);
                    params.put("saldo",saldo);
                    params.put("tanggal", tanggal);
                    params.put("alamat", alamat);
                    params.put("catatan", catatan);
                    params.put("status", status);
                    params.put("id_user", getIntent().getStringExtra("id_user"));
                    return  params;
                }
            };
            VolleyConnection.getInstance(jemput_sampah.this).addToRequesQue(request);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);

        }else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !!", Toast.LENGTH_SHORT).show();

        }
    }

    private void createsaldo(final String username, final String saldo_awal, final String saldo_akhir){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_CREATE_SALDO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("server_response");
                            if (resp.equals("[{\"status \":\"succses\"}]")){
                                //  Toast.makeText(input_sampah_admin.this, "CREATE SALDO BERHASIL", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(jemput_sampah.this, "GAGAL", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(jemput_sampah.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", getIntent().getStringExtra("id_user"));
                params.put("username", username);
                params.put("saldo_awal", saldo_awal);
                params.put("saldo_akhir", saldo_akhir);
                return params;
            }
        };
        VolleyConnection.getInstance(jemput_sampah.this).addToRequesQue(stringRequest);
    }

    void tampil_nama(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Db_Contract.SERVER_TAMPIL_NAMA_LOGIN + getIntent().getStringExtra("username"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            inputNama.setText(jsonObject.get("username").toString());
                            inputAlamat.setText(jsonObject.get("alamat").toString());
                            inputSaldoAwal.setText(jsonObject.get("saldo").toString());

                            String id_user = jsonObject.get("id_user").toString();
                            getIntent().putExtra("id_user", id_user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(jemput_sampah.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void saldoakhir1(double countBerat){
        String getsaldo = inputSaldoAwal.getText().toString();
        String gettotal = inputTotalHarga.getText().toString();
        int ttlhrga = Integer.parseInt(gettotal);
        int getsal = Integer.parseInt(getsaldo);
        int total = (ttlhrga + getsal);
        String jml = String.valueOf(total);
        inputSaldoAkhir.setText(jml);
    }

    private void jumlahHarga(double countBerat){
        String harga = inputHargaperKg.getText().toString();
        String getpajak = inputPajak.getText().toString();
        int pjk = Integer.parseInt(getpajak);
        int countHarga = Integer.parseInt(harga);
        int jumlahHarga = (int) (countBerat * countHarga - pjk);
        String jml = String.valueOf(jumlahHarga);
        inputTotalHarga.setText(jml);
    }


    private void setTotalPajak(double countBerat) {
        String harga = inputHargaperKg.getText().toString();
        int countHarga = Integer.parseInt(harga);
        int countTotal = (int) (countBerat * countHarga * 0.15);
        String jumlah = String.valueOf(countTotal);
        inputPajak.setText(jumlah);
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

    public Boolean cekNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void sendnotification(){
        String textusername = inputNama.getText().toString();
        String textkategori = sKategoriSelected.toString();
        String textberat = inputBerat.getText().toString();
        String texthargaperkg = inputHargaperKg.getText().toString();
        String textpajak = inputPajak.getText().toString();
        String texttotalharga = inputTotalHarga.getText().toString();
        String textsaldoawal = inputSaldoAwal.getText().toString();
        String textsaldoakhir = inputSaldoAkhir.getText().toString();
        String texttanggal = inputTanggal.getText().toString();
        String textalamat = inputAlamat.getText().toString();
        String textcatatan = inputCatatan.getText().toString();

        String body = "Jemput Sampah Di rumah, \n" + textusername + "\n" +  texttanggal;
        String titel = "Jemput Sampah";
        String token = "/topics/admin";

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
//                Toast.makeText(jemput_sampah.this, "terjadi kesalahan Notification", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(jemput_sampah.this);
        requestQueue.add(request);
    }
}