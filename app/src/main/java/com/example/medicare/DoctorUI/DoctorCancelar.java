package com.example.medicare.DoctorUI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class DoctorCancelar extends Fragment {
    ListView citasCanceladas;
    DoctorCitaAdaptador asignar;
    List<Cita> miCitas;
    List<String> miRazones;
    List<String> misCitasId;
    FirebaseUser user;
    String emailDoctor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_declinados_fragment, container, false);
        citasCanceladas = view.findViewById(R.id.declined_appointments);
        miCitas = new ArrayList<>();
        miRazones = new ArrayList<>();
        misCitasId = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        emailDoctor = user.getEmail();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Citas");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                miCitas.clear();
                miRazones.clear();
                misCitasId.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Cita cita = data.getValue(Cita.class);
                    //Log.w("ETIQUETA",dataSnapshot.toString());

                   // Log.e("ESTADO ",data.getValue(Cita.class).toString());
                    try {
                        //falta que elimine la cita del en espera
                        if ( cita.getEstado().equals("Declinado")) {
                            misCitasId.add(data.getKey());
                            miRazones.add(data.child("razon").getValue(String.class));
                            miCitas.add(cita);
                            if (getActivity() != null) {
                                asignar = new DoctorCitaAdaptador(getActivity(), miCitas);
                                citasCanceladas.setAdapter(asignar);
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
        citasCanceladas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogoCancelar dialogoCancelar = new DialogoCancelar(misCitasId.get(position), asignar, miRazones.get(position));
                dialogoCancelar.show(getParentFragmentManager(), "Reinicie el cuadro de dialogo");

            }
        });



        return view;
    }
}
