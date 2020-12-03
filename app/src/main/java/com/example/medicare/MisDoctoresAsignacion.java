package com.example.medicare;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.medicare.modelo.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MisDoctoresAsignacion extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<Doctor> misDoctoresLista;
    ArrayList<Doctor> arrayList;


    public MisDoctoresAsignacion(Context context, List<Doctor> misDoctoresLista) {
        mContext = context;
        this.misDoctoresLista = misDoctoresLista;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList();
        this.arrayList.addAll(misDoctoresLista);
    }

    @Override
    public int getCount() {
        return misDoctoresLista.size();
    }

    @Override
    public Object getItem(int position) {
        return misDoctoresLista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.midoctor_row, null);

        final CircleImageView fotoDoctor = convertView.findViewById(R.id.imagen_perfil);


        final TextView doctorFullName = convertView.findViewById(R.id.nombreCompleto);
        final TextView speciality = convertView.findViewById(R.id.especialidad);
        final Doctor doctor = misDoctoresLista.get(position);
        final String emailDoctor = doctor.getEmail();

        doctorFullName.setText("Dr. "+doctor.getnombreC());
        speciality.setText(doctor.getespecialidad());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference profileRef = storageReference.child("imagen_perfil").child(emailDoctor+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(fotoDoctor);

            }
        });
    return convertView;
    }
    }