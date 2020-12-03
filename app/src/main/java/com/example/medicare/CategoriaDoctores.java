package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CategoriaDoctores extends Fragment {
    static ListView listView;
    static AsignacionEspecialidad adapter;
    ArrayList<String> especialidadList;
    static AsignacionEspecialidad getAdapter(){
        return adapter;
    }
    public static ListView getListView(){return listView;}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentdos_layout, container, false);
        listView = view.findViewById(R.id.listaEspecialidad);
        especialidadList = new ArrayList<>();
        especialidadList.addAll(Arrays.asList(getResources().getStringArray(R.array.doctorEspecialidad)));
        Collections.sort(especialidadList);
        adapter = new AsignacionEspecialidad(getContext(),especialidadList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DoctorEspecialidad.class);
                intent.putExtra("especialidad", especialidadList.get(position));
                startActivity(intent);
            }
        });
        return view;
    }
}
