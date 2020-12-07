package com.example.medicare.DoctorUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicare.R;
import com.example.medicare.modelo.Paciente;
import com.example.medicare.modelo.Relacion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MiPacienteActivity extends AppCompatActivity {
    ListView misPacientesListView;
    List<Paciente> pacientes;
    List<Paciente> misPacientes;
    List<String> misRelaciones;
    static MiPacienteAsignar asignar;

    public static void notifyAdapter()
    {
        asignar.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_pacientes);
        misPacientesListView = findViewById(R.id.misPacientes);
        pacientes = new ArrayList<>();
        misPacientes = new ArrayList<>();
        misRelaciones = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pacientes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pacientes.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Paciente paciente = data.getValue(Paciente.class);
                    pacientes.add(paciente);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Relacion");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                misPacientes.clear();
                misRelaciones.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Relacion relacion = data.getValue(Relacion.class);
                    for(int i = 0; i< pacientes.size(); i++)
                    {
                        Paciente paciente = pacientes.get(i);
                        if(relacion.getEmailPaciente().equals(paciente.getEmail()) && relacion.getEmailDoctor().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        {
                            misRelaciones.add(data.getKey());
                            misPacientes.add(paciente);
                            asignar = new MiPacienteAsignar(MiPacienteActivity.this, misPacientes);
                            misPacientesListView.setAdapter(asignar);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        misPacientesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MiPacienteActivity.this, MiPacienteInf.class);
                intent.putExtra("nombreC", misPacientes.get(position).getNombreC());
                intent.putExtra("email", misPacientes.get(position).getEmail());
                intent.putExtra("NSS", misPacientes.get(position).getnss());
                intent.putExtra("celular", misPacientes.get(position).getcelular());
                intent.putExtra("Estado", misPacientes.get(position).getestado());
                intent.putExtra("Cumpleaños", misPacientes.get(position).getcumpleaños());
                intent.putExtra("relacionId", misRelaciones.get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(MiPacienteActivity.this, DoctorMenuActivity.class);
        DoctorMenuActivity.setToken(1);
        //startActivity(intent);
        finish();
    }
}
