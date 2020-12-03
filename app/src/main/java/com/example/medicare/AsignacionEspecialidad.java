package com.example.medicare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AsignacionEspecialidad extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<String> listaEspecialidad;
    ArrayList<String> arrayList;
    String[] Colores = {"#9E9E9E", "#039BE5", "#00C853", "#C51162", "#303F9F"};

    public AsignacionEspecialidad(Context context, List<String> listaEspecialidad) {
        mContext = context;
        this.listaEspecialidad = listaEspecialidad;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<String>();
        this.arrayList.addAll(listaEspecialidad);
    }

    @Override
    public int getCount() {
        return listaEspecialidad.size();
    }

    @Override
    public Object getItem(int position) {
        return listaEspecialidad.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.especialidad_row,null);

        TextView circleText = convertView.findViewById(R.id.circulo_text);
        TextView speciality = convertView.findViewById(R.id.doctorNombreC);

        String spec = listaEspecialidad.get(position);

        speciality.setText(spec);
        circleText.setText((spec.charAt(0)+"").toUpperCase());
        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listaEspecialidad.clear();
        if (charText.length() == 0) {
            listaEspecialidad.addAll(arrayList);
        } else {
            for (String s : arrayList) {
                if (s.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    listaEspecialidad.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

}
