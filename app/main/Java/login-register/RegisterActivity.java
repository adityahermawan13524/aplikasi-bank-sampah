package com.dev.banksampahdigital.LoginRegister;

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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.View.main.HomeActivity;
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullname, editTextRegisterEmail, editTextRegisterDateofBrith,editTextRegisterMobile,
            editTextRegisterPwd, editTextRegisterConfirmPwd;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private Button buttonRegister;
    private DatePickerDialog picker;
    private static final String TAG = "RegisterActivity";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        editTextRegisterFullname = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDateofBrith = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_password);
        buttonRegister = findViewById(R.id.button_register);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan Data...");

        //RadioButton for Gender
        radioGroupRegisterGender = findViewById(R.id.radioGroup_register_Gender);
        radioGroupRegisterGender.clearCheck();

        //setup the datepicker on edit text
        editTextRegisterDateofBrith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //date picker

                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                        editTextRegisterDateofBrith.setText(dayofMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                //Enter data
                String textFullName = editTextRegisterFullname.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDoB = editTextRegisterDateofBrith.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmpwd = editTextRegisterConfirmPwd.getText().toString();
                String gender;

                if (TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegisterActivity.this, "Silahkan Masukkan Nama Lengkap Anda.", Toast.LENGTH_SHORT).show();
                    editTextRegisterFullname.setError("Nama Lengkap Diperlukan.");
                    editTextRegisterFullname.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Silahkan Masukkan Email Anda.", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email Lengkap Diperlukan.");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegisterActivity.this, "Silahkan Masukkan Kata Sandi anda ", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("nKata Sandi Diperlukan");
                    editTextRegisterPwd.requestFocus();
                }else if (textPwd.length() <6 ) {
                        Toast.makeText(RegisterActivity.this, "Kata Sandi Minimal 6 Karakter ", Toast.LENGTH_SHORT).show();
                        editTextRegisterPwd.setError("Password Kurang Bagus");
                        editTextRegisterPwd.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmpwd)) {
                        Toast.makeText(RegisterActivity.this, "Silahkan Masukkan Konfirmasi Kata Sandi anda ", Toast.LENGTH_SHORT).show();
                        editTextRegisterConfirmPwd.setError("Konfirmasi Kata Sandi Tidak Boleh Kosong");
                        editTextRegisterConfirmPwd.requestFocus();
                } else if (!textPwd.equals(textConfirmpwd)) {
                        Toast.makeText(RegisterActivity.this, "Konfirmasi Kata Sandi Harus Sama ", Toast.LENGTH_SHORT).show();
                        editTextRegisterConfirmPwd.setError("Password Konfirmasi Tidak sama");
                        editTextRegisterConfirmPwd.requestFocus();
                        //mengkosongkan konfirmasi password
                        editTextRegisterPwd.clearComposingText();
                        editTextRegisterConfirmPwd.clearComposingText();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Silahkan Massukan Ulang Email Anda", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email tidak valid.");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(RegisterActivity.this, "Silahkan Masukkan Tanggal lahir Anda ", Toast.LENGTH_SHORT).show();
                    editTextRegisterDateofBrith.setError("Tanggal Lahir Diperlukan");
                    editTextRegisterDateofBrith.requestFocus();
                } else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Silahkan Masukkan Jenis Kelamin Anda ", Toast.LENGTH_SHORT).show();
                    radioButtonRegisterGenderSelected.setError("Jenis Kelamin Diperlukan");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "Silahkan Masukkan No Handpone Anda. ", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("No Handpone Diperlukan");
                    editTextRegisterMobile.requestFocus();
                } else if (textMobile.length() != 13) {
                    Toast.makeText(RegisterActivity.this, "Silahkan Masukkan Ulang No Handpone Anda ", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("No Handphone diperlukan");
                    editTextRegisterMobile.requestFocus();
                }
                else {
                    gender = radioButtonRegisterGenderSelected.getText().toString();

                    CreateDataToServer(textFullName, textPwd, textEmail, textDoB, gender, textMobile);
                }
            }
        });
    }

    public void CreateDataToServer(final String username, final String password, final String email, final String dob, final String gender, final String mobile){
        if(cekNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.SERVER_REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status \":\"succses\"}]")){
                                    Toast.makeText(getApplicationContext(), "Register Berhasil ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(), "Register Gagal", Toast.LENGTH_SHORT).show();
                                    editTextRegisterFullname.setError("Username Sudah diGunakan.");
                                    editTextRegisterFullname.requestFocus();
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
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("username", username);
                    params.put("password", password);
                    params.put("email", email);
                    params.put("dob", dob);
                    params.put("gender", gender);
                    params.put("mobile", mobile);
                    return params;
                }
            };
            VolleyConnection.getInstance(RegisterActivity.this).addToRequesQue(stringRequest);
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