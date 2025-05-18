package com.dev.banksampahdigital.adapter;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.admin.View.transaksi.detail_transaksi_input_sampah_admin;
import com.dev.banksampahdigital.database.Db_Contract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class adapter_transaksi extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Objek_transaki>model;

    public adapter_transaksi(Activity activity, List<Objek_transaki> lists){
        this.activity = activity;
        this.model = lists;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TextView tusername, ttanggal, tkategori, tberat, tpajak, tharga, tharga_perKg, tstatus;


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null && inflater != null){
            view = inflater.inflate(R.layout.list_item_detail_transaksi, null);
        }
        if (view !=null) {
            tusername = view.findViewById(R.id.tvNama);
            ttanggal = view.findViewById(R.id.tvDate);
            tkategori = view.findViewById(R.id.tvKategori);
            tberat = view.findViewById(R.id.tvBerat);
            tpajak = view.findViewById(R.id.tvpajak);
            tharga = view.findViewById(R.id.tvHarga);
            tharga_perKg = view.findViewById(R.id.tvHarga_perKg);
            tstatus = view.findViewById(R.id.tvstatus);

            tusername.setText(model.get(position).getUsername());
            ttanggal.setText(model.get(position).getTanggal());
            tkategori.setText(model.get(position).getKategori_sampah());
            tberat.setText(model.get(position).getBerat()+" Kg");
            tpajak.setText(model.get(position).getPajak());
            tharga.setText(model.get(position).getHarga());
            tharga_perKg.setText(model.get(position).getHarga_perKg());
            tstatus.setText(model.get(position).getStatus());

            int colorblue = Color.rgb(0,0, 255);
            int colorred = Color.RED;
            int colorjingga = Color.rgb(255,153,0);

            String stts = tstatus.getText().toString();

            if (stts.equals("Sukses")){
                tstatus.setTextColor(colorblue);
            }else if (stts.equals("Di-Tolak")) {
                tstatus.setTextColor(colorred);
            }else if (stts.equals("In-Progres")){
                tstatus.setTextColor(colorjingga);
            }
        }
        return view;
    }



}
