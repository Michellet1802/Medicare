package com.example.medicare.DoctorUI;

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

import com.example.medicare.InfoConsulta;
import com.example.medicare.R;
import com.example.medicare.modelo.Consulta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MiPacienteConsultaFrag extends Fragment {
    FloatingActionButton fab;
    ListView consultas;
    MiPacienteConsultaAdap asignar;
    ArrayList<Consulta> consultaList = new ArrayList<>();
    List<Consulta> misConsultas;
    FirebaseUser usuario;
    static String emailPaciente;
    String nombreDoctor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mipaciente_consulta_fragment, container, false);
        fab = view.findViewById(R.id.fab);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctores");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombreDoctor =  dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nombreC").getValue(String.class);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AnadirConsultaDialog anadirConsultaDialog = new AnadirConsultaDialog(nombreDoctor, FirebaseAuth.getInstance().getCurrentUser().getEmail(), emailPaciente);
                        anadirConsultaDialog.show(getFragmentManager(), "Añadir Consulta");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        consultas = view.findViewById(R.id.consultas);
        misConsultas = new ArrayList<>();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
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
                            asignar = new MiPacienteConsultaAdap(getActivity(), misConsultas);
                            consultas.setAdapter(asignar);
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
    public static void setEmailPaciente(String email)
    {
        emailPaciente = email;
    }
    }
