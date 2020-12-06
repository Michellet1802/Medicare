package com.example.medicare.DoctorUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medicare.R;
import com.example.medicare.modelo.Cita;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorAceptar extends Fragment {
    ListView citasAceptadas;
    DoctorCitaAdaptador adaptador;
    List<Cita> miCitas;
    FirebaseUser usuario;
    String emailDoctor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_aceptar_fragment, container, false);
        citasAceptadas = view.findViewById(R.id.listas_aceptadas);
        miCitas = new ArrayList<>();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        emailDoctor = usuario.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Citas");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                miCitas.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Cita cita = data.getValue(Cita.class);
                    if( cita.getEstado().equals("Aceptado")) {
                        miCitas.add(cita);
                        if (getActivity()!=null){
                            adaptador = new DoctorCitaAdaptador(getActivity(), miCitas);
                            citasAceptadas.setAdapter(adaptador);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer el archivo: " + databaseError.getCode());
            }
        });


        return view;
    }
}
