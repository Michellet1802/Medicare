package com.example.medicare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class EnEspera extends Fragment {
    ListView citasEspera;
    AsignarCita asignar;
    ArrayList<Cita> citaList = new ArrayList<>();
    List<Cita> misCitas;
    FirebaseUser usuario;
    String emailPaciente;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enespera_fragment, container, false);
        citasEspera = view.findViewById(R.id.citas_espera);
        misCitas = new ArrayList<>();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        emailPaciente = usuario.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Citas");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                misCitas.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Cita cita = data.getValue(Cita.class);
                    if( cita.getEmailPaciente().equals(emailPaciente) &&  cita.getEstado().equals("En espera")) {
                        misCitas.add(cita);
                        if (getActivity()!=null){
                            asignar = new AsignarCita(getActivity(), misCitas);
                            citasEspera.setAdapter(asignar);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        return view;
    }
}
