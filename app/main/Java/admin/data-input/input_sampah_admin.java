package com.dev.banksampahdigital.admin.View.input;

import android.annotation.SuppressLint;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class input_sampah_admin extends AppCompatActivity {

    Toolbar toolbar;
    private TextInputEditText inputHarga;
    private TextInputEditText inputBerat;
    private TextInputEditText totalPajak;
    private TextInputEditText totalHarga;
    private TextInputEditText inputTanggal;
    private TextInputEditText inputNama;
    private TextInputEditText inputAlamat;
    private TextInputEditText inputCatatan;
    private TextInputEditText saldo_awal;
    private TextInputEditText saldo_akhir;
    private Button checkOut;
    private DatePickerDialog inpuTgl;
    private Spinner skategorispinner;
    private ArrayAdapter<CharSequence> skategoriadapter, hargaadapter;
    private ProgressDialog progressDialog;
    double countBerat = 0;
    String sKategoriSelected;
    ListView listView;
    ArrayList<String>KategroiSampahList = new ArrayList<>();
    ArrayAdapter<String>KategoryAdapter;
    RequestQueue requestQueue;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_sampah_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        inputNama = findViewById(R.id.inputNama);
        inputTanggal = findViewById(R.id.inputTanggal);
        inputAlamat = findViewById(R.id.inputAlamat);
        inputCatatan = findViewById(R.id.inputTambahan);
        checkOut = findViewById(R.id.btnChekout);
        inputBerat = findViewById(R.id.inputBerat);
        inputHarga = findViewById(R.id.inputHarga);
        totalHarga = findViewById(R.id.totalHarga);
        totalPajak = findViewById(R.id.pajakharga);
        saldo_awal = findViewById(R.id.saldo_awal);
        saldo_akhir = findViewById(R.id.saldo_akhir);

        progressDialog = new ProgressDialog(input_sampah_admin.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan Data...");
//        listView = findViewById(R.id.list_view);

        inputNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), cari_nama.class);
                startActivity(intent);
            }
        });

        tampil_nama();

//      spin kategori sampah
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
                                KategoryAdapter = new ArrayAdapter<>(input_sampah_admin.this,
                                        android.R.layout.simple_spinner_item, KategroiSampahList);
                                KategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                skategorispinner.setAdapter(KategoryAdapter);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(input_sampah_admin.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
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
                                    inputHarga.setText(jsonObject.getString("harga_perKg"));
                                }else{
                                    inputHarga.setText("0");
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
                        countBerat = Double.parseDouble(s.toString());
                        setTotalPajak(countBerat);
                        jumlahHarga(countBerat);
                        saldoakhir1(countBerat);
                    } else {
                        totalPajak.setText("0");
                        totalHarga.setText("0");
                    }
                    inputBerat.addTextChangedListener(this);
                }
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

                inpuTgl = new DatePickerDialog(input_sampah_admin.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        inputTanggal.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                inpuTgl.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                inpuTgl.show();
            }
        });

        //input sampah checkout
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textusername = inputNama.getText().toString();
                String textkategori_sampah = sKategoriSelected.toString();
                String textberat = inputBerat.getText().toString();
                String textharga_perKg = inputHarga.getText().toString();
                String textpajak = totalPajak.getText().toString();
                String textTotal = totalHarga.getText().toString();
                String texttanggal = inputTanggal.getText().toString();
                String textalamat = inputAlamat.getText().toString();
                String textcatatan = inputCatatan.getText().toString();
                String textsaldoawal = saldo_awal.getText().toString();
                String textsaldoakhir = saldo_akhir.getText().toString();



                if (TextUtils.isEmpty(textusername)){
                    inputNama.requestFocus();
                    inputNama.setError("Silahkan input nama");
                } else if (TextUtils.isEmpty(textberat)) {
                    inputBerat.requestFocus();
                    inputBerat.setError("Silahkan Masukkan jumlah berat sampah");
                } else if(TextUtils.isEmpty(texttanggal)){
                    inputTanggal.requestFocus();
                    inputTanggal.setError("input tanggal diperlukan");
                }else{
                    CreateDataInputSampah(textusername,textkategori_sampah,textberat,textharga_perKg,textpajak,textTotal,textsaldoakhir,texttanggal,textalamat,textcatatan, "Sukses");
                    createsaldo(textusername, textsaldoawal, textsaldoakhir);
                    updatesaldo(textusername,textsaldoakhir);

                }
            }
        });
    }

    public void CreateDataInputSampah(String username, String kategori_sampah, String berat,
                                      String harga_perKg, String pajak , String totalharga, String saldo,
                                      String tanggal, String alamat, String catatan, String status){
        if (cekNetworkConnection()){
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, Db_Contract.SERVER_INPUT_SAMPAH_ADMIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"success\"}]")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(input_sampah_admin.this);
                                    builder.setMessage("Input Sampah Berhasil");
                                    builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), input_sampah_admin.class);
                                            intent.putExtra(username, "username");
                                            startActivity(intent);
                                            gettokenfromdb();
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }else{
                                    Toast.makeText(getApplicationContext(), "Input Sampah Gagal", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("username", username);
                    params.put("kategori_sampah", kategori_sampah);
                    params.put("berat", berat);
                    params.put("harga_perKg", harga_perKg);
                    params.put("pajak", pajak);
                    params.put("harga", totalharga);
                    params.put("saldo", saldo);
                    params.put("tanggal", tanggal);
                    params.put("alamat", alamat);
                    params.put("catatan", catatan);
                    params.put("id_user", getIntent().getStringExtra("id_user"));
                    params.put("status", status);
                    return params;
                }
            };
            VolleyConnection.getInstance(input_sampah_admin.this).addToRequesQue(request);
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

    private void saldoakhir1(double countBerat){
        String getsaldo = saldo_awal.getText().toString();
        String gettotal = totalHarga.getText().toString();
        int ttlhrga = Integer.parseInt(gettotal);
        int getsal = Integer.parseInt(getsaldo);
        int total = (ttlhrga + getsal);
        String jml = String.valueOf(total);
        saldo_akhir.setText(jml);
    }

    private void jumlahHarga(double countBerat){
        String harga = inputHarga.getText().toString();
        String getpajak = totalPajak.getText().toString();
        int pjk = Integer.parseInt(getpajak);
        int countHarga = Integer.parseInt(harga);
        int jumlahHarga = (int) (countBerat * countHarga - pjk);
        String jml = String.valueOf(jumlahHarga);
        totalHarga.setText(jml);
    }


    private void setTotalPajak(double countBerat) {
        String harga = inputHarga.getText().toString();
        int countHarga = Integer.parseInt(harga);
        int countTotal = (int) (countBerat * countHarga * 0.15);
        String jumlah = String.valueOf(countTotal);
        totalPajak.setText(jumlah);
    }

    @NonNull
    private String formatRupiah(Double number){
        Locale localeID = new Locale("IND", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatRupiah = numberFormat.format(number);
        String[] split = formatRupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+", "+split[0].substring(2, length);
    }

    void tampil_nama(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Db_Contract.SERVER_TAMPIL_NAMA+getIntent().getStringExtra("id_user"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            inputNama.setText(jsonObject.get("username").toString());
                            inputAlamat.setText(jsonObject.get("alamat").toString());
                            saldo_awal.setText(jsonObject.get("saldo").toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(input_sampah_admin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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
                                Toast.makeText(input_sampah_admin.this, "GAGAL", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(input_sampah_admin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        VolleyConnection.getInstance(input_sampah_admin.this).addToRequesQue(stringRequest);
    }

    private void updatesaldo( final String username, final String saldo){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Db_Contract.SERVER_UPDATE_SALDO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("server_response");
                            if (resp.equals("[{\"status \":\"succses\"}]")){

                            }else{
                                //Toast.makeText(input_sampah_admin.this, "GAGAL", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(input_sampah_admin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", getIntent().getStringExtra("id_user"));
                params.put("username", username);
                params.put("saldo", saldo);
                return params;
            }
        };
        VolleyConnection.getInstance(input_sampah_admin.this).addToRequesQue(stringRequest);
    }

    public void gettokenfromdb(){
        String textuser = inputNama.getText().toString();
        String saldo = saldo_akhir.getText().toString();

        StringRequest request = new StringRequest(Request.Method.GET,
                Db_Contract.SERVER_GETTOKENFROMDB + textuser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String username = jsonObject.getString("username");
                            String token = jsonObject.getString("token");

                            String body = "Transaksi Bank Sampah Sukses, \n" + "Saldo Anda Rp. " +  saldo;

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
                                    Toast.makeText(input_sampah_admin.this, "terjadi kesalahan Notification", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("token", token);
                                    params.put("title", username);
                                    params.put("body", body);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(input_sampah_admin.this);
                            requestQueue.add(request);



                        } catch (JSONException e) {
                            Toast.makeText(input_sampah_admin.this, "ERR AMBIL TOKEN", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}