package com.example.medicare.DoctorUI;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.medicare.R;
import com.example.medicare.modelo.Paciente;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class MiPacienteAsignar extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<Paciente> miPacienteLista;
    ArrayList<Paciente> arrayList;

    public MiPacienteAsignar(Context context, List<Paciente> miPacienteLista) {
        mContext = context;
        this.miPacienteLista = miPacienteLista;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList();
        this.arrayList.addAll(miPacienteLista);
    }
    @Override
    public int getCount() {
        return miPacienteLista.size();
    }

    @Override
    public Object getItem(int posicion) {
        return miPacienteLista.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.mipaciente_row, null);

        final CircleImageView doctorPicture = convertView.findViewById(R.id.imagen_perfil);


        final TextView pacienteNombreC = convertView.findViewById(R.id.nombreCompleto);
        final Paciente paciente = miPacienteLista.get(posicion);
        final String emailPaciente = paciente.getEmail();

        pacienteNombreC.setText(paciente.getNombreC());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference perfilRef = storageReference.child("imagen_perfil").child(emailPaciente+".jpg");
        perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(doctorPicture);

            }
        });
        return convertView;
    }
}
