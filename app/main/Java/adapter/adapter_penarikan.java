package com.dev.banksampahdigital.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.banksampahdigital.R;

import java.util.ArrayList;

public class adapter_penarikan extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Objek_penarikan>model;

    public adapter_penarikan(Context context, ArrayList<Objek_penarikan>model){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;

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

    TextView username, tanggal, jumlah_penarikan, saldo_akhir, status;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_riwayat_penarikan, parent, false);

        username = view.findViewById(R.id.tv_p_nama_nasabah);
        tanggal = view.findViewById(R.id.tv_p_tanggal);
        jumlah_penarikan = view.findViewById(R.id.tvjmlpenarikan);
        saldo_akhir = view.findViewById(R.id.tv_p_saldo_akhir);
        status = view.findViewById(R.id.statuspenarikan);

        username.setText(model.get(position).getUsername());
        tanggal.setText(model.get(position).getTanggal());
        jumlah_penarikan.setText(model.get(position).getJumlah_penarikan());
        saldo_akhir.setText(model.get(position).saldo_akhir);
        status.setText(model.get(position).getStatus());

        int colorblue = Color.rgb(0,0, 255);
        int colorred = Color.RED;
        int colorjingga = Color.rgb(255,153,0);

        String stts = status.getText().toString();
        

        if (stts.equals("Sukses")){
            status.setTextColor(colorblue);
        } else if (stts.equals("Di-Tolak")) {
            status.setTextColor(colorred);
        }else if (stts.equals("Sedang Diproses !!")){
            status.setTextColor(colorjingga);
        }


        return view;
    }
}
