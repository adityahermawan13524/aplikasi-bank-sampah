package com.dev.banksampahdigital.LoginRegister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.dev.banksampahdigital.View.main.HomeActivity;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.adapter.Objek;
import com.dev.banksampahdigital.adapter.adpter_cari_username;
import com.dev.banksampahdigital.admin.View.data_nasabah.data_nasabah;
import com.dev.banksampahdigital.admin.View.input.input_sampah_admin;
import com.dev.banksampahdigital.admin.View.menu_admin.home_admin;
import com.dev.banksampahdigital.database.Db_Contract;
import com.dev.banksampahdigital.database.VolleyConnection;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText user, pass;
    private Button btnlogin;
    private ProgressDialog progressDialog;
   private static final String TAG = "LoginActivity";
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "username";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = findViewById(R.id.editText_login_email);
        pass = findViewById(R.id.editText_login_password);
        btnlogin = findViewById(R.id.button_login);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menunggu Proses...");

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_NAME, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        if (name != null && password != null){
            shredname(name, password);
        }


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sUsername = user.getText().toString();
                String sPassword = pass.getText().toString();

                if (TextUtils.isEmpty(sUsername)){
                    Toast.makeText(LoginActivity.this, "Silahkan Isi Username Anda.", Toast.LENGTH_SHORT).show();
                        user.setError("Username Diperlukan.");
                        user.requestFocus();
                }else if (TextUtils.isEmpty(sPassword)){
                    Toast.makeText(LoginActivity.this, "Silahkan isi Password anda.", Toast.LENGTH_SHORT).show();
                    pass.setError("Password Diperlukan.");
                    pass.requestFocus();

                } else{
                    //generate token dulu

                    CheckLogin(sUsername, sPassword);
                }

            }
        });
    }

    public void shredname(final String username, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.SERVER_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("server_respon");
                            if (resp.equals("[{\"status\":\"user\"}]")){
                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                token();
                                finish();


                            }else if (resp.equals("[{\"status\":\"admin\"}]")){

                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), home_admin.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                token();
                                finish();


                            }else {
                                Toast.makeText(LoginActivity.this, "Username dan Kata Sandi Tidak Cocok", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "eror", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void CheckLogin(final String username, final String password){
        if(cekNetworkConnection()){
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.SERVER_LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_respon");
                                if (resp.equals("[{\"status\":\"user\"}]")){
                                    String sUsername = user.getText().toString();
                                    String sPassword = pass.getText().toString();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_NAME, sUsername);
                                    editor.putString(KEY_PASSWORD, sPassword);
                                    editor.apply();


                                    Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                    token();
                                    finish();


                                }else if (resp.equals("[{\"status\":\"admin\"}]")){
                                    String sUsername = user.getText().toString();
                                    String sPassword = pass.getText().toString();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_NAME, sUsername);
                                    editor.putString(KEY_PASSWORD, sPassword);
                                    editor.apply();

                                    Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), home_admin.class);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                    token();
                                    finish();


                                }else {
                                    Toast.makeText(LoginActivity.this, "Username dan Kata Sandi Tidak Cocok", Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "eror", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
                }
            };
                VolleyConnection.getInstance(LoginActivity.this).addToRequesQue(stringRequest);
                 new Handler().postDelayed(new Runnable() {
                     @Override
                      public void run() {
                    progressDialog.cancel();
                    progressDialog.dismiss();
                    }
                },       2000);
        }else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !!", Toast.LENGTH_SHORT).show();
        }
    }


    public Boolean cekNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void token(){
        String susername = user.getText().toString();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        create_token_to_db(susername, token);
                    }
                });
    }

    public void create_token_to_db(String username, String token){
        if(cekNetworkConnection()) {
            StringRequest request = new StringRequest(
                    Request.Method.POST, Db_Contract.SERVER_CREATE_TOKEN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");

                                if (resp.equals("[{\"status\":\"success\"}]")){
//                                    Toast.makeText(LoginActivity.this, "sukses", Toast.LENGTH_SHORT).show();
                                }else{
//                                    Toast.makeText(LoginActivity.this, "Token Sudah Ada", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "eror", Toast.LENGTH_SHORT).show();
                }
            }
            ) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("token", token);

                    return params;
                }
            };
                VolleyConnection.getInstance(LoginActivity.this).addToRequesQue(request);

        }else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !!", Toast.LENGTH_SHORT).show();
        }
    }
}