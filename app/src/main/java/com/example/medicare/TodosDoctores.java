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

import com.example.medicare.modelo.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodosDoctores extends Fragment {
    static ListView listView;
    static AsignarVistaLista asignar;
    List<Doctor> misDoctores;

    public static AsignarVistaLista getAdapter() {
        return asignar;
    }

    public static ListView getListView(){return listView;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentuno_layout, container, false);
        listView = view.findViewById(R.id.myListView);
        misDoctores = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctores");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                misDoctores.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    misDoctores.add(doctor);
                    Collections.sort(misDoctores);
                    asignar = new AsignarVistaLista(getContext(), misDoctores);
                    listView.setAdapter(asignar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), com.example.medicare.DisplayDoctorInfo.class);
                intent.putExtra("nombreCompleto",misDoctores.get(position).getnombreC());
                intent.putExtra("email",misDoctores.get(position).getEmail());
                intent.putExtra("especialidad",misDoctores.get(position).getespecialidad());
                intent.putExtra("celular",misDoctores.get(position).getcelular());
                intent.putExtra("direccion", misDoctores.get(position).getdireccion());
                intent.putExtra("ciudad", misDoctores.get(position).getciudad());
                startActivity(intent);
            }
        });




        return view;
    }
}
