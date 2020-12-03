package com.example.medicare.DoctorUI;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.medicare.R;
import com.example.medicare.modelo.Paciente;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuscarPacienteAsignar extends BaseAdapter {
    int i=0;
    Context mContext;
    LayoutInflater inflater;
    List<Paciente> pacienteList;
    ArrayList<Paciente> arrayList;

    public BuscarPacienteAsignar(Context context, List<Paciente> pacienteList) {
        mContext = context;
        this.pacienteList = pacienteList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList();
        this.arrayList.addAll(pacienteList);
    }

    @Override
    public int getCount() {
        return pacienteList.size();
    }

    @Override
    public Object getItem(int position) {
        return pacienteList.get(position);
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
        TextView fullName = convertView.findViewById(R.id.doctorNombreC);

        Paciente paciente = pacienteList.get(position);

        fullName.setText(paciente.getNombreC().trim());
        circleText.setText((paciente.getNombreC().charAt(0)+"").toUpperCase());
        return convertView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        pacienteList.clear();
        if (charText.length()==0){
            pacienteList.addAll(arrayList);
        }
        else {
            for (Paciente p : arrayList){
                if (p.getNombreC().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    pacienteList.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

}
