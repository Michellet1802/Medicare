package com.example.medicare.DoctorUI;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

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

public class DoctorCitaAdaptador extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<Cita> citaList;
    ArrayList<Cita> arrayList;
    String emailPaciente;


    public DoctorCitaAdaptador(Context context, List<Cita> citaList) {
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
        convertView = inflater.inflate(R.layout.citas_columna, null);

        final CircleImageView patientPicture = convertView.findViewById(R.id.imagen_perfil);


        final TextView pacienteNombreC = convertView.findViewById(R.id.nombreCompleto);
        final TextView dia = convertView.findViewById(R.id.dia);
        final TextView hora = convertView.findViewById(R.id.hora);


        final Cita cita = citaList.get(position);
        emailPaciente = cita.getEmailDoctor();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pacientes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Paciente paciente = data.getValue(Paciente.class);
                    if(paciente.getEmail().equals(cita.getEmailPaciente()))
                    {
                        pacienteNombreC.setText(paciente.getNombreC());
                        dia.setText(cita.getfecha());
                        hora.setText(cita.gethora());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference profileRef = storageReference.child("imagen_perfil").child(emailPaciente+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(patientPicture);

            }
        });
        return convertView;
    }


}
