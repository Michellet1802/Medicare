package com.example.medicare.DoctorUI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicare.R;
import com.example.medicare.modelo.Paciente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuscarPaciente extends AppCompatActivity {
    ListView listView;
    BuscarPacienteAsignar asignar;
    List<Paciente> miPacientes;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_paciente);
        listView = findViewById(R.id.pacientesBusq);
        searchView = findViewById(R.id.busqueda);
        miPacientes = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pacientes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                miPacientes.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Paciente paciente = data.getValue(Paciente.class);
                    miPacientes.add(paciente);
                    Collections.sort(miPacientes);
                    asignar = new BuscarPacienteAsignar(BuscarPaciente.this, miPacientes);
                    listView.setAdapter(asignar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                    if (TextUtils.isEmpty(s)) {
                        asignar.filter("");
                        listView.clearTextFilter();
                    } else {
                        asignar.filter(s);
                    }
                return true;
        }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BuscarPaciente.this, MostrarInfPaciente.class);
                intent.putExtra("nombreCompleto", miPacientes.get(position).getNombreC());
                intent.putExtra("email", miPacientes.get(position).getEmail());
                intent.putExtra("Cumpleaños", miPacientes.get(position).getcumpleaños());
                intent.putExtra("Celula", miPacientes.get(position).getcelular());
                intent.putExtra("NSS", miPacientes.get(position).getnss());
                intent.putExtra("Estado", miPacientes.get(position).getestado());
                startActivity(intent);
            }
        });
            }

}
