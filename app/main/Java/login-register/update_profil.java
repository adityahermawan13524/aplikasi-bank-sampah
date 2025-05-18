package com.dev.banksampahdigital.LoginRegister;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.admin.View.data_nasabah.data_nasabah;
import com.dev.banksampahdigital.admin.View.data_nasabah.update_data_nasabah;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class update_profil extends AppCompatActivity {

    private EditText etNama, etAlamat, etEmail, etDob, etNohp,etPassword;
    private RadioGroup radioGrouptbGender, radioGroupStatus;
    private RadioButton radioButtontbGenderSelected, radioButtonUser;
    private Button btnupdate;
    private DatePickerDialog picker;
    private ProgressDialog progressDialog, progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(update_profil.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan Data...");

        progressDialog1 = new ProgressDialog(update_profil.this);
        progressDialog1.setTitle("Loading");
        progressDialog1.setMessage("Mengambil Data...");

        etNama = findViewById(R.id.editText_tb_Nama);
        etAlamat = findViewById(R.id.editText_tb_alamat);
        etEmail = findViewById(R.id.editText_tb_email);
        etDob = findViewById(R.id.editText_tb_dob);
        etNohp = findViewById(R.id.editText_tb_mobile);
        etPassword = findViewById(R.id.editText_tb_password);
        btnupdate = findViewById(R.id.btn_update_data_admin);

        //RadioButton for Gender
        radioGrouptbGender = findViewById(R.id.radioGroup_tb_Gender);


        //setup the datepicker on edit text
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //date picker
                picker = new DatePickerDialog(update_profil.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                        etDob.setText(dayofMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        tampil_data();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = radioGrouptbGender.getCheckedRadioButtonId();
                radioButtontbGenderSelected = findViewById(selectedGenderId);

                //Enter data
                String textName = etNama.getText().toString();
                String textAlamat = etAlamat.getText().toString();
                String textEmail = etEmail.getText().toString();
                String textDob = etDob.getText().toString();
                String textNoHp = etNohp.getText().toString();
                String textPassword = etPassword.getText().toString();
                String gender;

                if(textName.isEmpty() && textAlamat.isEmpty() && textEmail.isEmpty() && textDob.isEmpty() && textNoHp.isEmpty()){
                    Toast.makeText(update_profil.this, "Silahkan Isi Semua Data", Toast.LENGTH_SHORT).show();
                }else {
                    gender = radioButtontbGenderSelected.getText().toString();
                    Update_Data(textName, textAlamat, textEmail, textDob, textNoHp,gender,textPassword);
                }
            }
        });
    }

    void  Update_Data(final String nama, final String alamat, final String email, final String dob, final String nohp, final String gender, final String password){
        if (cekNetworkConnection()){
            StringRequest request = new StringRequest(
                    Request.Method.POST, Db_Contract.SERVER_UPDATE_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status \":\"succses\"}]")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(update_profil.this);
                                    builder.setMessage("Data Sukses Terupdate");
                                    builder.setPositiveButton("ok.", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String name = etNama.getText().toString();
                                            Intent intent = new Intent(update_profil.this, Profil.class);
                                            intent.putExtra("username", name);
                                            startActivity(intent);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }else {
                                    Toast.makeText(update_profil.this, "Update data Gagal", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(update_profil.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", nama);
                    params.put("alamat", alamat);
                    params.put("email", email);
                    params.put("dob", dob);
                    params.put("mobile", nohp);
                    params.put("gender", gender);
                    params.put("id_user", getIntent().getStringExtra("id_user"));
                    params.put("status", getIntent().getStringExtra("status"));
                    params.put("password", password);


                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
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

    void tampil_data(){
        if (cekNetworkConnection()){
            progressDialog1.show();
            StringRequest request = new StringRequest(
                    Request.Method.GET, Db_Contract.SERVER_TAMPIL_NAMA_LOGIN + getIntent().getStringExtra("user"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                etNama.setText(jsonObject.getString("username"));
                                etEmail.setText(jsonObject.getString("email"));
                                etDob.setText(jsonObject.getString("dob"));
                                etAlamat.setText(jsonObject.getString("alamat"));
                                etNohp.setText(jsonObject.getString("mobile"));
                                etPassword.setText(jsonObject.getString("password"));
                                String id_user = jsonObject.getString("id_user");
                                String status = jsonObject.getString("status");

                                getIntent().putExtra("id_user", id_user);
                                getIntent().putExtra("status", status);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(update_profil.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
                }
            }
            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog1.cancel();
                }
            }, 2000);
        }else{
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !!", Toast.LENGTH_SHORT).show();
        }

    }
    public Boolean cekNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}