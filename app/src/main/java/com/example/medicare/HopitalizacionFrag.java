package com.example.medicare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class HopitalizacionFrag extends Fragment {
    ListView hospitalizaciones;
    HospitalizacionP asigar;
    ArrayList<com.example.medicare.modelo.Hospitalizacion> hospitalizacionList = new ArrayList<>();
    List<com.example.medicare.modelo.Hospitalizacion> misHospitalizaciones;
    FirebaseUser user;
    String emailPaciente;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hospitalizacion_fragment, container, false);
        hospitalizaciones = view.findViewById(R.id.hospitalizacion);
        misHospitalizaciones = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        emailPaciente = user.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hospitalizacion");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                misHospitalizaciones.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    com.example.medicare.modelo.Hospitalizacion hospitalizacion = data.getValue(com.example.medicare.modelo.Hospitalizacion.class);
                    if(hospitalizacion.getEmailPaciente().equals(emailPaciente)) {
                        misHospitalizaciones.add(hospitalizacion);
                        Collections.sort(misHospitalizaciones);
                        if (getActivity()!=null){
                            asigar = new HospitalizacionP(getActivity(), misHospitalizaciones);
                            hospitalizaciones.setAdapter(asigar);
                        }
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
