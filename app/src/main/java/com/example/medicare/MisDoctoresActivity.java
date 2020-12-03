package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicare.modelo.Doctor;
import com.example.medicare.modelo.Relacion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MisDoctoresActivity extends AppCompatActivity {
    ListView misDoctoresLista;
    List<Doctor> Doctors;
    List<Doctor> misDoctores;
    MisDoctoresAsignacion adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_doctores);
        misDoctoresLista = findViewById(R.id.misDoctores);
        Doctors = new ArrayList<>();
        misDoctores = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctores");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctors.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    Doctors.add(doctor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Citas");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                misDoctores.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Relacion relacion = data.getValue(Relacion.class);
                    for(int i=0; i<Doctors.size(); i++)
                    {
                        Doctor doc = Doctors.get(i);
                        if(/*relacion.getEmailDoctor().equals(doc.getEmail()) && */ relacion.getEmailPaciente().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        {
                            misDoctores.add(doc);
                            adapter = new MisDoctoresAsignacion(MisDoctoresActivity.this, misDoctores);
                            misDoctoresLista.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        misDoctoresLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MisDoctoresActivity.this, MiDoctorInfoActivity.class);
                intent.putExtra("nombreC",misDoctores.get(position).getnombreC());
                intent.putExtra("email",misDoctores.get(position).getEmail());
                intent.putExtra("especialidad",misDoctores.get(position).getespecialidad());
                intent.putExtra("celular",misDoctores.get(position).getcelular());
                intent.putExtra("direccion", misDoctores.get(position).getdireccion());
                intent.putExtra("ciudad", misDoctores.get(position).getciudad());
                startActivity(intent);
            }
        });



    }
}
