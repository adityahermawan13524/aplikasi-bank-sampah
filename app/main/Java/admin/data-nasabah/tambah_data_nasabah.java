package com.dev.banksampahdigital.admin.View.data_nasabah;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.dev.banksampahdigital.LoginRegister.RegisterActivity;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.View.main.HomeActivity;
import com.dev.banksampahdigital.admin.View.menu_admin.home_admin;
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;
import com.dev.banksampahdigital.model.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class tambah_data_nasabah extends AppCompatActivity {

    private EditText etNama, etAlamat, etPassword, etConfPass, etEmail, etDob, etNohp;
    private Button Tambah_nasbah;
    private RadioGroup radioGrouptbGender, radioGroupStatus;
    private RadioButton radioButtontbGenderSelected, radioButtonStatusSelected;
    private DatePickerDialog picker;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah_data_nasabah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(tambah_data_nasabah.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan Data...");

        //RadioButton for Gender
        radioGrouptbGender = findViewById(R.id.radioGroup_tb_Gender);
        radioGrouptbGender.clearCheck();

        //RadioButton for status
        radioGroupStatus = findViewById(R.id.radioGroup_tb_status);
        radioGroupStatus.clearCheck();

        etNama = findViewById(R.id.editText_tb_Nama);
        etAlamat = findViewById(R.id.editText_tb_alamat);
        etPassword = findViewById(R.id.editText_tb_password);
        etConfPass = findViewById(R.id.editText_tb_confirm_password);
        etEmail = findViewById(R.id.editText_tb_email);
        etDob = findViewById(R.id.editText_tb_dob);
        etNohp = findViewById(R.id.editText_tb_mobile);
        Tambah_nasbah = findViewById(R.id.button_tb_tamabh_nasabah);

        //setup the datepicker on edit text
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //date picker

                picker = new DatePickerDialog(tambah_data_nasabah.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                        etDob.setText(dayofMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Tambah_nasbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = radioGrouptbGender.getCheckedRadioButtonId();
                radioButtontbGenderSelected = findViewById(selectedGenderId);

                int selectedStatusId = radioGroupStatus.getCheckedRadioButtonId();
                radioButtonStatusSelected =findViewById(selectedStatusId);

                //Enter data
                String textName = etNama.getText().toString();
                String textAlamat = etAlamat.getText().toString();
                String textPassword = etPassword.getText().toString();
                String textConfPass = etConfPass.getText().toString();
                String textEmail = etEmail.getText().toString();
                String textDob = etDob.getText().toString();
                String textNoHp = etNohp.getText().toString();
                String gender;
                String status;

                if (TextUtils.isEmpty(textName)){
                   etNama.setError("Nama Harus Diisi");
                   etNama.requestFocus();
                } else if (TextUtils.isEmpty(textAlamat)){
                   etAlamat.setError("Silahkan Isi Alamat");
                   etAlamat.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                   etPassword.setError("Kata Sandi Diperlukan");
                   etPassword.requestFocus();
                } else if (textPassword.length() < 6) {
                   etPassword.setError("Kata Sandi Minimal 6 Digit");
                   etPassword.requestFocus();
                } else if (TextUtils.isEmpty(textConfPass)) {
                   etConfPass.setError("Silahkan Ketikkan Ulang Password");
                   etConfPass.requestFocus();
                } else if (!textPassword.equals(textConfPass)) {
                   etConfPass.setError("Konfirmasi Password Tidak Sama");
                   etConfPass.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    etEmail.setError("Email Tidak Valid");
                    etEmail.requestFocus();
                } else if (TextUtils.isEmpty(textDob)) {
                    etDob.setError("Silahkan Masukkan Tanggal Lahir");
                    etDob.requestFocus();
//                } else if (radioGrouptbGender.getCheckedRadioButtonId()== -1) {
//                    radioButtontbGenderSelected.setError("Silahkan Masukkan Jenis Kelamin");
//                    radioButtontbGenderSelected.requestFocus();
//                } else if (radioGroupStatus.getCheckedRadioButtonId() == -1) {
//                    radioButtonStatusSelected.setError("Silahkan Pilih Status");
//                    radioButtonStatusSelected.requestFocus();
                } else if (TextUtils.isEmpty(textNoHp)) {
                    etNohp.setError("Silahkan Masukkan No HP");
                    etNohp.requestFocus();
                }else {
                    gender = radioButtontbGenderSelected.getText().toString();
                    status = radioButtonStatusSelected.getText().toString();
                    if (status.equals("User")){
                        CreateDataToServerUser(textName, textAlamat, textPassword, textEmail, textDob, textNoHp, gender, status,"0");
                    }else {
                        CreateDataToServerAdmin(textName, textAlamat, textPassword, textEmail, textDob, textNoHp, gender, status);

                    }
                }
            }
        });

    }

    public void CreateDataToServerUser(final String nama, final String alamat, final String password, final String email, final String dob, final String nohp, final String gender, final String status, final String saldo){
        if (cekNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, Db_Contract.SERVER_REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status \":\"succses\"}]")){
                                    Toast.makeText(getApplicationContext(), "Register Berhasil ", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), data_nasabah.class);
                                    startActivity(intent);
                                    finish();

                                }else {
                                    Toast.makeText(getApplicationContext(), "Register User Gagal", Toast.LENGTH_SHORT).show();
                                    etNama.setError("Username Sudah diGunakan.");
                                    etNama.requestFocus();
                                }
                            } catch (JSONException e) {
                               Toast.makeText(getApplicationContext(), "SALDO Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(tambah_data_nasabah.this, "EROR", Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", nama);
                    params.put("alamat", alamat);
                    params.put("password", password);
                    params.put("email", email);
                    params.put("dob", dob);
                    params.put("mobile", nohp);
                    params.put("gender", gender);
                    params.put("status", status);
                    params.put("saldo", saldo);
                    return params;
                }
            };
            VolleyConnection.getInstance(tambah_data_nasabah.this).addToRequesQue(stringRequest);
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

    public void CreateDataToServerAdmin(final String nama, final String alamat, final String password, final String email, final String dob, final String nohp, final String gender, final String status){
        if (cekNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, Db_Contract.SERVER_REGISTER_ADMIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status \":\"succses\"}]")){
                                    Toast.makeText(getApplicationContext(), "Register Berhasil ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), home_admin.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(tambah_data_nasabah.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                                    etNama.setError("Username Sudah diGunakan.");
                                    etNama.requestFocus();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", nama);
                    params.put("alamat", alamat);
                    params.put("password", password);
                    params.put("email", email);
                    params.put("dob", dob);
                    params.put("mobile", nohp);
                    params.put("gender", gender);
                    params.put("status", status);
                    return params;
                }
            };
            VolleyConnection.getInstance(tambah_data_nasabah.this).addToRequesQue(stringRequest);
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
}