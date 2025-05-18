package com.dev.banksampahdigital.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dev.banksampahdigital.R;

import java.util.ArrayList;

public class adapter_detail_transaksi extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Objek_transaki>model;

    public adapter_detail_transaksi(Context context, ArrayList<Objek_transaki>model){
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

    TextView tusername, ttanggal, tkategori, tberat, tpajak, tharga, tharga_perKg;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_item_detail_transaksi, parent, false);

        tusername = view.findViewById(R.id.tvNama);
        ttanggal = view.findViewById(R.id.tvDate);
        tkategori = view.findViewById(R.id.tvKategori);
        tberat = view.findViewById(R.id.tvBerat);
        tpajak = view.findViewById(R.id.tvpajak);
        tharga = view.findViewById(R.id.tvHarga);
        tharga_perKg = view.findViewById(R.id.tvHarga_perKg);

        tusername.setText(model.get(position).getUsername());
        ttanggal.setText(model.get(position).getTanggal());
        tkategori.setText(model.get(position).getKategori_sampah());
        tberat.setText(model.get(position).getBerat()+" Kg");
        tpajak.setText(model.get(position).getPajak());
        tharga.setText(model.get(position).getHarga());
        tharga_perKg.setText(model.get(position).getHarga_perKg());

        return  view;

    }

}
