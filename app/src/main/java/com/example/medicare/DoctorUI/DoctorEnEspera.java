package com.example.medicare.DoctorUI;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.medicare.R;
import com.example.medicare.modelo.Cita;
import com.example.medicare.modelo.Paciente;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorEnEspera extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<Cita> citaList;
    ArrayList<Cita> arrayList;
    String emailPaciente;
    String citaId;


    public DoctorEnEspera(Context context, List<Cita> citaList) {
        mContext = context;
        this.citaList = citaList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(citaList);
    }

    @Override
    public int getCount() {
        return citaList.size();
    }

    @Override
    public Object getItem(int position) {
        return citaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.enespera_cita_row, null);

        final CircleImageView patientPicture = convertView.findViewById(R.id.imagen_perfil);

        final TextView patientFullName = convertView.findViewById(R.id.nombreCompleto);
        final TextView day = convertView.findViewById(R.id.dia);
        final TextView time = convertView.findViewById(R.id.hora);
        Button acceptButton = convertView.findViewById(R.id.aceptarBtn);
        Button declineButton = convertView.findViewById(R.id.cancelarBtn);

        final Cita cita = citaList.get(position);
        citaId = "";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Citas");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Cita appt = data.getValue(Cita.class);
                    if(appt.getfecha().equals(cita.getfecha()) && appt.gethora().equals(cita.gethora())) {
                        citaId = data.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Citas").child(citaId);
                databaseReference.child("Estado").setValue("Aceptado");
                notifyDataSetChanged();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RazonCancelar razonCancelar = new RazonCancelar(citaId, DoctorEnEspera.this);
                razonCancelar.show(((FragmentActivity)mContext).getSupportFragmentManager(), "razon de cancelacion");
            }
        });
        emailPaciente = cita.getEmailDoctor();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pacientes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Paciente paciente = data.getValue(Paciente.class);
                    if (paciente.getEmail().equals(cita.getEmailPaciente())) {
                        patientFullName.setText(paciente.getNombreC());
                        day.setText(cita.getfecha());
                        time.setText(cita.gethora());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference profileRef = storageReference.child("imagen_perfil").child(emailPaciente + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(patientPicture);

            }
        });
        return convertView;
    }


}
