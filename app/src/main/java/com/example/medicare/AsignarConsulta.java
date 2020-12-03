package com.example.medicare;

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
import com.example.medicare.modelo.Consulta;
import com.example.medicare.modelo.Doctor;
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

public class AsignarConsulta extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<Consulta> listaConsulta;
    ArrayList<Consulta> arrayList;


    public AsignarConsulta(Context context, List<Consulta> listaConsulta) {
        mContext = context;
        this.listaConsulta = listaConsulta;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Consulta>();
        this.arrayList.addAll(listaConsulta);
    }

    @Override
    public int getCount() {
        return listaConsulta.size();
    }

    @Override
    public Object getItem(int position) {
        return listaConsulta.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.consulta_r, null);

        final CircleImageView doctorPicture = convertView.findViewById(R.id.imagen_perfil);


        final TextView doctorNombreC = convertView.findViewById(R.id.nombreCompleto);
        final TextView dia = convertView.findViewById(R.id.dia);


        final Consulta Consulta = listaConsulta.get(position);
        final String emailDoctor = Consulta.getDoctorEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctores");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    if(doctor.getEmail().equals(emailDoctor))
                    {
                        doctorNombreC.setText("Dr. "+doctor.getnombreC());
                        dia.setText(Consulta.getfecha());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference profileRef = storageReference.child("imagen_perfil").child(emailDoctor+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(doctorPicture);

            }
        });
        return convertView;
    }


}
