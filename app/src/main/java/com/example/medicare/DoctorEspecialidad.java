package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicare.modelo.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DoctorEspecialidad extends AppCompatActivity {
    TextView doctorEspecialidad;
    ListView miDoctorSegunEspecialidadListView;
    List<Doctor> misDoctores;
    AsignarVistaLista asignar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctores_especialidad);
        Intent intent = getIntent();
        final String speciality = intent.getStringExtra("especialidad");
        miDoctorSegunEspecialidadListView = findViewById(R.id.miDoctorSegunEspecialidad);
        doctorEspecialidad = findViewById(R.id.especialidad);
        doctorEspecialidad.setText(speciality);
        misDoctores = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctores");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                misDoctores.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    if(doctor.getespecialidad().equals(speciality))
                    {
                        misDoctores.add(doctor);
                        Collections.sort(misDoctores);
                        asignar = new AsignarVistaLista(getApplicationContext(), misDoctores);
                        miDoctorSegunEspecialidadListView.setAdapter(asignar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        miDoctorSegunEspecialidadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DisplayDoctorInfo.class);
                intent.putExtra("nombreCompleto",misDoctores.get(position).getnombreC());
                intent.putExtra("email",misDoctores.get(position).getEmail());
                intent.putExtra("especialidad",misDoctores.get(position).getespecialidad());
                intent.putExtra("celular",misDoctores.get(position).getcelular());
                intent.putExtra("direccion", misDoctores.get(position).getdireccion());
                intent.putExtra("city", misDoctores.get(position).getciudad());
                startActivity(intent);
            }
        });
    }
}
