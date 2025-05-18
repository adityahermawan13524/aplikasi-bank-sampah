package com.dev.banksampahdigital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.banksampahdigital.R;

import java.util.ArrayList;

public class adapter_data_sampah extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Objek_data_sampah>model;

    public adapter_data_sampah(Context context, ArrayList<Objek_data_sampah> model){
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

    TextView tjenis_sampah,t_harga_perKg;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_data_sampah, parent, false);

        tjenis_sampah = view.findViewById(R.id.tvjenissampah);
        t_harga_perKg = view.findViewById(R.id.tvhargasampah);

        tjenis_sampah.setText(model.get(position).getJenis_sampah());
        t_harga_perKg.setText(model.get(position).getHarga_perKg());

        return view;

    }
}
