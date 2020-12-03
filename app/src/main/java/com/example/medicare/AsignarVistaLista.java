package com.example.medicare;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.medicare.modelo.Doctor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AsignarVistaLista extends BaseAdapter {
    int i=0;
    Context mContext;
    LayoutInflater inflater;
    List<Doctor> doctorLista;
    ArrayList<Doctor> arrayList;

    public AsignarVistaLista(Context context, List<Doctor> doctorLista) {
        mContext = context;
        this.doctorLista = doctorLista;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Doctor>();
        this.arrayList.addAll(doctorLista);
    }

    @Override
    public int getCount() {
        return doctorLista.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorLista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.row,null);

        TextView circleText = convertView.findViewById(R.id.circulo_text);
        TextView nombreC = convertView.findViewById(R.id.doctorNombreC);

        Doctor doctor = doctorLista.get(position);

        nombreC.setText(doctor.getnombreC().trim());
        circleText.setText((doctor.getnombreC().charAt(0)+"").toUpperCase());
        return convertView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        doctorLista.clear();
        if (charText.length()==0){
            doctorLista.addAll(arrayList);
        }
        else {
            for (Doctor d : arrayList){
                if (d.getnombreC().toLowerCase(Locale.getDefault()).contains(charText.trim()) || d.getciudad().toLowerCase(Locale.getDefault()).contains(charText.trim())){
                    doctorLista.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }

}
