package com.example.medicare;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class HospitalizacionP extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<com.example.medicare.modelo.Hospitalizacion> hospitalizacionList;
    ArrayList<com.example.medicare.modelo.Hospitalizacion> arrayList;


    public HospitalizacionP(Context context, List<com.example.medicare.modelo.Hospitalizacion> hospitalizacionList) {
        mContext = context;
        this.hospitalizacionList = hospitalizacionList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<com.example.medicare.modelo.Hospitalizacion>();
        this.arrayList.addAll(hospitalizacionList);
    }

    @Override
    public int getCount() {
        return hospitalizacionList.size();
    }

    @Override
    public Object getItem(int position) {
        return hospitalizacionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.hospitalisation_row, null);
        final TextView nombreHospital = convertView.findViewById(R.id.nombreHospital);
        final TextView date = convertView.findViewById(R.id.fecha);
        TextView disease = convertView.findViewById(R.id.enfermedad);
        TextView price = convertView.findViewById(R.id.precioHosp);

        final com.example.medicare.modelo.Hospitalizacion hospitalizacion = hospitalizacionList.get(position);

        nombreHospital.setText(hospitalizacion.getnombreHospital());
        date.setText(hospitalizacion.getfecha());
        disease.setText(hospitalizacion.getenfermedad());
        price.setText(hospitalizacion.getprecio());


        return convertView;
    }


}
