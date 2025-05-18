package com.dev.banksampahdigital.admin.View.informasi_bank_sampah;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.adapter.Sampah;
import com.dev.banksampahdigital.admin.View.input.input_sampah_admin;
import com.dev.banksampahdigital.database.Db_Contract;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class informasi_bank_sampah extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText total_berat_sampah, total_pajak, total_saldo, tgl;
    private Button btnFetch;
    private PieChart pieChart;
    private BubbleChart bubbleChart;
    private BarChart barChart;
    private DatePickerDialog inpuTgl;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informasi_bank_sampah);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tgl = findViewById(R.id.t_tgl);
        //      input tanggal
        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                inpuTgl = new DatePickerDialog(informasi_bank_sampah.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tgl.setText(selectedDate);
                    }
                }, year, month, day);
                inpuTgl.show();
            }
        });

        btnFetch = findViewById(R.id.btnFetch);
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedDate.isEmpty()){
                    tampil_barapertgl();
                    berdasarkan_jenis();
                }else {
                    Toast.makeText(informasi_bank_sampah.this, "Pilih tanggal terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toolbar = findViewById(R.id.toolbar_informasi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        total_berat_sampah = findViewById(R.id.t_brt_sampah);
        ttlberat();

        total_pajak = findViewById(R.id.t_pajak);
        ttlpajak();

        total_saldo = findViewById(R.id.t_saldo);
        ttlsaldo();

        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);

            totalkat();

            informasi();

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(informasi_bank_sampah.this, "Berat: " + e.getY() + " kg", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    public void ttlberat(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, Db_Contract.TOTAL_BERAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray gethasil = jsonObject.getJSONArray("hasil");
                            for (int i = 0; i <gethasil.length();i++){
                                JSONObject getData = gethasil.getJSONObject(i);
                                total_berat_sampah.setText(getData.getString("total_sampah") + " KG");
                            }
                        } catch (JSONException e) {
                            total_berat_sampah.setText("0");
                            Toast.makeText(informasi_bank_sampah.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(informasi_bank_sampah.this, "EROR", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ttlpajak(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, Db_Contract.TOTAL_PAJAK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray gethasil = jsonObject.getJSONArray("hasil");
                            for (int i = 0; i <gethasil.length();i++){
                                JSONObject getData = gethasil.getJSONObject(i);
                                total_pajak.setText(getData.getString("total_pajak"));
                            }
                        } catch (JSONException e) {
                            total_pajak.setText("0");
                            Toast.makeText(informasi_bank_sampah.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(informasi_bank_sampah.this, "EROR", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ttlsaldo(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, Db_Contract.TOTAL_SALDO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray gethasil = jsonObject.getJSONArray("hasil");
                            for (int i = 0; i <gethasil.length();i++){
                                JSONObject getData = gethasil.getJSONObject(i);
                                total_saldo.setText(getData.getString("total_saldo"));
                            }
                        } catch (JSONException e) {
                            total_saldo.setText("0");
                            Toast.makeText(informasi_bank_sampah.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(informasi_bank_sampah.this, "EROR", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    ArrayList<Sampah>model;
    public void totalkat(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, Db_Contract.INFORMASI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray gethasil = jsonObject.getJSONArray("hasil");

                            model = new ArrayList<>();
                            for (int i = 0; i <gethasil.length();i++){
                                JSONObject getData = gethasil.getJSONObject(i);
                                String tanggal = getData.getString("tanggal");
                                float berat = Float.parseFloat(getData.getString("total_berat"));
                                String kategori_sampah = getData.getString("kategori_sampah");

                                model.add(new Sampah(tanggal, kategori_sampah, berat));
                            }
                            ArrayList<PieEntry> entries = new ArrayList<>();
                            for (Sampah item : model) {
                                entries.add(new PieEntry(item.getTotalBerat(), item.getKategoriSampah()));
                            }
                            PieDataSet dataSet = new PieDataSet(entries, "Jenis Sampah");
                            dataSet.setColors(new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA});
                            dataSet.setValueTextSize(14f);

                            PieData pieData = new PieData(dataSet);
                            pieChart.setData(pieData);
                            pieChart.getDescription().setEnabled(false);
                            pieChart.setUsePercentValues(true);
                            pieChart.setDrawHoleEnabled(false);

                            // Menampilkan legenda
                            Legend legend = pieChart.getLegend();
                            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

                            pieChart.invalidate(); // Refresh chart


                        } catch (JSONException e) {
                            total_saldo.setText("0");
                            Toast.makeText(informasi_bank_sampah.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(informasi_bank_sampah.this, "EROR", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void informasi(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, Db_Contract.INFORMASI_PERTGL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray gethasil = jsonObject.getJSONArray("hasil");

                            model = new ArrayList<>();
                            for (int i = 0; i <gethasil.length();i++){
                                JSONObject getData = gethasil.getJSONObject(i);
                                String tanggal = getData.getString("tanggal");
                                float berat = Float.parseFloat(getData.getString("total_berat"));
                                String kategori_sampah = getData.getString("kategori_sampah");

                                model.add(new Sampah(tanggal, kategori_sampah, berat));
                            }

                            ArrayList<BarEntry> entries = new ArrayList<>();
                            int index = 0;
                            for (Sampah item : model) {
                                entries.add(new BarEntry(index++, item.getTotalBerat()));
                            }
                            BarDataSet dataSet = new BarDataSet(entries, "Berat Sampah");
                            dataSet.setColors(new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA});
                            BarData barData = new BarData(dataSet);
                            barChart.setData(barData);

                            barChart.setDragEnabled(true);
                            barChart.setScaleEnabled(true);
                            barChart.setPinchZoom(true);
                            barChart.setDoubleTapToZoomEnabled(true);
                            barChart.setVisibleXRangeMaximum(5);
                            barChart.getXAxis().setGranularity(1f);
                            barChart.getXAxis().setGranularityEnabled(true);
                            barChart.setExtraBottomOffset(70f);

                            // Atur Label X-Axis
                            ArrayList<String> labels = new ArrayList<>();
                            for (Sampah item : model) {
                                String label = item.getKategoriSampah() + "\n" + item.getTanggal();
                                labels.add(label);
                            }
//                            String[] labels = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setGranularity(1f);
                            xAxis.setGranularityEnabled(true);
                            xAxis.setYOffset(15f);
                            xAxis.setLabelRotationAngle(45);



                            // Atur Y-Axis
                            YAxis yAxisLeft = barChart.getAxisLeft();
                            yAxisLeft.setAxisMinimum(0f);

                            YAxis yAxisRight = barChart.getAxisRight();
                            yAxisRight.setEnabled(false);

                            barChart.getDescription().setEnabled(false);

                            Description desc = new Description();
                            desc.setText("Total Berat Sampah per Tanggal");
                            barChart.setDescription(desc);
                            barChart.invalidate();  // Refresh chart




                        } catch (JSONException e) {
                            total_saldo.setText("0");
                            Toast.makeText(informasi_bank_sampah.this, "GAGAL AMBIL DATA", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(informasi_bank_sampah.this, "EROR", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void berdasarkan_jenis(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.INFORMASI_TOTAL_PERTGL + tgl.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray getHasil = jsonObject.getJSONArray("hasil");

                            model = new ArrayList<>();
                            for (int i = 0; i<getHasil.length(); i++){
                                JSONObject getData = getHasil.getJSONObject(i);
                                String kategori_sampah = getData.getString("kategori_sampah");
                                float berat = Float.parseFloat(getData.getString("total_berat"));
                                model.add(new Sampah(tgl.getText().toString(), kategori_sampah, berat));
                            }
                            ArrayList<PieEntry> entries = new ArrayList<>();
                            for (Sampah item : model) {
                                entries.add(new PieEntry(item.getTotalBerat(), item.getKategoriSampah()));
                            }
                            PieDataSet dataSet = new PieDataSet(entries, "Jenis Sampah");
                            dataSet.setColors(new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA});
                            dataSet.setValueTextSize(14f);

                            PieData pieData = new PieData(dataSet);
                            pieChart.setData(pieData);
                            pieChart.getDescription().setEnabled(false);
                            pieChart.setUsePercentValues(true);
                            pieChart.setDrawHoleEnabled(false);

                            // Menampilkan legenda
                            Legend legend = pieChart.getLegend();
                            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

                            pieChart.invalidate(); // Refresh chart


                        } catch (JSONException e) {
                            Toast.makeText(informasi_bank_sampah.this, "Gagal Ambil Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(informasi_bank_sampah.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void tampil_barapertgl(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.INFORMASI_TOTAL_PERTGL + tgl.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray getHasil = jsonObject.getJSONArray("hasil");

                            model = new ArrayList<>();
                            for (int i = 0; i<getHasil.length(); i++){
                                JSONObject getData = getHasil.getJSONObject(i);
                                String kategori_sampah = getData.getString("kategori_sampah");
                                float berat = Float.parseFloat(getData.getString("total_berat"));
                                model.add(new Sampah(tgl.getText().toString(), kategori_sampah, berat));
                            }
                            ArrayList<BarEntry> entries = new ArrayList<>();
                            int index = 0;
                            for (Sampah item : model) {
                                entries.add(new BarEntry(index++, item.getTotalBerat()));
                            }

                            BarDataSet dataSet = new BarDataSet(entries, "Berat Sampah");
                            dataSet.setColors(new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA});
                            BarData barData = new BarData(dataSet);
                            barChart.setData(barData);

                            barChart.setDragEnabled(true);
                            barChart.setScaleEnabled(true);
                            barChart.setPinchZoom(true);
                            barChart.setDoubleTapToZoomEnabled(true);
                            barChart.setVisibleXRangeMaximum(5);
                            barChart.getXAxis().setGranularity(1f);
                            barChart.getXAxis().setGranularityEnabled(true);
                            barChart.setExtraBottomOffset(70f);

                            // Atur Label X-Axis
                            ArrayList<String> labels = new ArrayList<>();
                            for (Sampah item : model) {
                                String label = item.getKategoriSampah() + "\n" + item.getTanggal() ;
                                labels.add(label);
                            }
//                            String[] labels = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setGranularity(1f);
                            xAxis.setGranularityEnabled(true);
                            xAxis.setYOffset(1f);
                            xAxis.setLabelRotationAngle(45);



                            // Atur Y-Axis
                            YAxis yAxisLeft = barChart.getAxisLeft();
                            yAxisLeft.setAxisMinimum(0f);

                            YAxis yAxisRight = barChart.getAxisRight();
                            yAxisRight.setEnabled(false);

                            barChart.getDescription().setEnabled(false);

                            Description desc = new Description();
                            desc.setText("Total Berat Sampah per Tanggal");
                            barChart.setDescription(desc);
                            barChart.invalidate();  // Refresh chart


                        } catch (JSONException e) {
                            Toast.makeText(informasi_bank_sampah.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(informasi_bank_sampah.this, "Gagal ambil Data", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}