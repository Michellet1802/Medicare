package com.example.medicare.DoctorUI;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DoctorEnEsperaFrag extends Fragment {
    ListView citasEnEspera;
    DoctorEnEspera asignar;
    List<Cita> misCitas;
    FirebaseUser usuario;
    String emailDoctor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_enespera_fragment, container, false);
        citasEnEspera = view.findViewById(R.id.citas_espera);
        misCitas = new ArrayList<>();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        emailDoctor = usuario.getEmail();
        DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("Citas");

        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                misCitas.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Cita cita = data.getValue(Cita.class);
                 //   Log.w("ETIQUETA",dataSnapshot.toString());

                 //   Log.e("ESTADO ",data.getValue(Cita.class).toString());
                    try {
                        if( cita.getEstado().equals("En espera")) {
                            misCitas.add(cita);
                            if (getActivity()!=null){
                                asignar = new DoctorEnEspera(getActivity(), misCitas);
                                citasEnEspera.setAdapter(asignar);
                            }
                        }

                    }catch(NullPointerException e){

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });


        return view;
    }
}
