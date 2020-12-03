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

import com.example.medicare.modelo.Consulta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AsignarConsultaFrag extends Fragment {
    ListView consultas;
    AsignarConsulta asginar;
    List<Consulta> misConsultas;
    FirebaseUser usuario;
    String emailPaciente;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consulta_fragment, container, false);
        consultas = view.findViewById(R.id.consultas);
        misConsultas = new ArrayList<>();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        emailPaciente = usuario.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Consultas");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                misConsultas.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Consulta consulta = data.getValue(Consulta.class);
                    if(consulta.getpacienteEmail().equals(emailPaciente)) {
                        misConsultas.add(consulta);
                        if (getActivity()!=null){
                            asginar = new AsignarConsulta(getActivity(), misConsultas);
                            consultas.setAdapter(asginar);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        consultas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), InfoConsulta.class);
                Consulta consulta = misConsultas.get(position);
                intent.putExtra("nombreDoctor", consulta.getdoctorNombre());
                intent.putExtra("doctorEmail", consulta.getDoctorEmail());
                intent.putExtra("fecha", consulta.getfecha());
                intent.putExtra("precioHosp", consulta.getprecio());
                intent.putExtra("prescripcion", consulta.getprescripcion());
                intent.putExtra("enfermedad", consulta.getenfermedad());
                startActivity(intent);
            }
        });


        return view;
    }
}
