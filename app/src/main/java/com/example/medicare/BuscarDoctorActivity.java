package com.example.medicare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicare.modelo.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuscarDoctorActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Doctor> misDoctores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_doctor);
        recyclerView= findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        misDoctores = new ArrayList<>();
        recyclerView.setAdapter(new doctorAdapters(misDoctores));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctores");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    misDoctores.add(doctor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });
    }
    class doctorAdapters extends RecyclerView.Adapter<DoctorViewHolder>{
        List<Doctor> misDoctores;
        public doctorAdapters(List<Doctor> misDoctores) {
            super();
            this.misDoctores=misDoctores;
        }
        @NonNull
        @Override
        public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DoctorViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
            holder.bind(this.misDoctores.get(position));

        }

        @Override
        public int getItemCount() {
            return misDoctores.size();
        }
    }
    class DoctorViewHolder extends RecyclerView.ViewHolder{
        private TextView nombreC;
        private TextView emailDire;
        public DoctorViewHolder(ViewGroup container)
        {
            super(LayoutInflater.from(BuscarDoctorActivity.this).inflate(R.layout.lista_layout, container, false));
            nombreC=(TextView)itemView.findViewById(R.id.nombreCompleto);
            emailDire=(TextView)itemView.findViewById(R.id.emailDir);
        }
        public void bind(Doctor doctor)
        {
            nombreC.setText(doctor.getnombreC());
            emailDire.setText(doctor.getEmail());
        }
    }
}

