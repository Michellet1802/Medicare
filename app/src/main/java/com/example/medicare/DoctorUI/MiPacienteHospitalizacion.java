package com.example.medicare.DoctorUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medicare.HospitalizacionP;
import com.example.medicare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiPacienteHospitalizacion extends Fragment {
    FloatingActionButton fab;
    ListView hospitalitation;
    HospitalizacionP asignar;
    ArrayList<com.example.medicare.modelo.Hospitalizacion> hospitalizacionList = new ArrayList<>();
    List<com.example.medicare.modelo.Hospitalizacion> misHospitalizacion;
    List<String> misHospitalizaciones;
    FirebaseUser usuario;
    static String emailPaciente;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mipaciente_hospitalizacion_fragment, container, false);
        hospitalitation = view.findViewById(R.id.hospitalizacion);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnadirHospitalizacionDialog anadirHospitalizacionDialog = new AnadirHospitalizacionDialog(emailPaciente);
                anadirHospitalizacionDialog.show(getFragmentManager(), "Añadir hospitalizacion");
            }
        });
        misHospitalizacion = new ArrayList<>();
        misHospitalizaciones = new ArrayList<>();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hospitalizacion");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                misHospitalizacion.clear();
                misHospitalizaciones.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    com.example.medicare.modelo.Hospitalizacion hospitalizacion = data.getValue(com.example.medicare.modelo.Hospitalizacion.class);
                    if(hospitalizacion.getEmailPaciente().equals(emailPaciente)) {
                        misHospitalizacion.add(hospitalizacion);
                        Collections.sort(misHospitalizacion);
                        misHospitalizaciones.add(data.getKey());
                        if (getActivity()!=null){
                            asignar = new HospitalizacionP(getActivity(), misHospitalizacion);
                            hospitalitation.setAdapter(asignar);
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });
        hospitalitation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditarHospitalizacionDialogo editarHospitalizacionDialogo = new EditarHospitalizacionDialogo(misHospitalizacion.get(position), misHospitalizaciones.get(position));
                editarHospitalizacionDialogo.show(getFragmentManager(), "Editar hospitalizacion");
            }
        });
        return view;
    }
    public static void setEmailPaciente(String email)
    {
        emailPaciente = email;
    }
}
