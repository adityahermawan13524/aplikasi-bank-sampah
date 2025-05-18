package com.dev.banksampahdigital.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.banksampahdigital.R;

import java.util.ArrayList;

public class adpter_cari_username extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<Objek>model;

    public adpter_cari_username(Context context, ArrayList<Objek>model){
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

    TextView username, alamat;


    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view = inflater.inflate(R.layout.list_cari, parent, false);
       username = view.findViewById(R.id.crUsername);
       alamat = view.findViewById(R.id.crAlamat);

       username.setText(model.get(position).getUsernamae());
       alamat.setText(model.get(position).getAlamat());
       return view;
    }
}
