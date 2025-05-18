package com.dev.banksampahdigital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.banksampahdigital.R;

import java.util.ArrayList;

public class adapter_jadwal extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Objek_Jadwal>model;

    public adapter_jadwal(Context context, ArrayList<Objek_Jadwal>model){
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

    TextView thari, ttanggal, twaktu_awal, twaktu_akhir, ttempat, tcatatan;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_jadwal_sampah, parent, false);

        thari = view.findViewById(R.id.tv_j_hari);
        ttanggal = view.findViewById(R.id.tv_j_tanggal);
        twaktu_awal = view.findViewById(R.id.tv_j_waktuawal);
        twaktu_akhir = view.findViewById(R.id.tv_j_waktuakhir);
        ttempat = view.findViewById(R.id.tv_j_tempat);
        tcatatan = view.findViewById(R.id.tv_j_catatan);

        thari.setText(model.get(position).getHari());
        ttanggal.setText(model.get(position).getTanggal());
        twaktu_awal.setText(model.get(position).getWaktu_awal());
        twaktu_akhir.setText(model.get(position).getWaktu_akhir());
        ttempat.setText(model.get(position).getTempat());
        tcatatan.setText(model.get(position).getCatatan());


        return view;
    }
}
