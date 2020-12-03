package com.example.medicare.DoctorUI;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.medicare.R;
import com.example.medicare.modelo.Consulta;
import com.example.medicare.modelo.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MiPacienteConsultaAdap extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<Consulta> consultaList;
    ArrayList<Consulta> arrayList;
    DatabaseReference ref;


    public MiPacienteConsultaAdap(Context context, List<Consulta> consultaList) {
        mContext = context;
        this.consultaList = consultaList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Consulta>();
        this.arrayList.addAll(consultaList);
    }


    @Override
    public int getCount() {
        return consultaList.size();
    }

    @Override
    public Object getItem(int posicion) {
        return consultaList.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.mipaciente_consulta_row, null);

        final CircleImageView doctorImagen = convertView.findViewById(R.id.imagen_perfil);
        final TextView doctorNombreC = convertView.findViewById(R.id.nombreCompleto);
        final TextView dia = convertView.findViewById(R.id.dia);
        final ImageView eliminarView = convertView.findViewById(R.id.eliminarView);
        final ImageView editarView = convertView.findViewById(R.id.editarView);
        editarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        eliminarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final String[] consultaId = new String[1];
        final Consulta consulta = consultaList.get(posicion);
        final String emailDoctor = consulta.getDoctorEmail();
        final String emailPaciente = consulta.getpacienteEmail();
        ref = FirebaseDatabase.getInstance().getReference("Consultas");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot data : dataSnapshot.getChildren())
                {
                    Consulta consulta = data.getValue(Consulta.class);
                    if(consulta.getDoctorEmail().equals(emailDoctor) && consulta.getpacienteEmail().equals(emailPaciente))
                    {
                        consultaId[0] = data.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctores");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    if(doctor.getEmail().equals(emailDoctor))
                    {
                        if(doctor.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        {
                            editarView.setImageResource(R.drawable.ic_edit);
                            editarView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditarConsultaDialogo editarConsultaDialogo = new EditarConsultaDialogo(consulta, consultaId[0]);
                                    editarConsultaDialogo.show(((FragmentActivity)mContext).getSupportFragmentManager(), "edit consultation");
                                }
                            });
                            eliminarView.setImageResource(R.drawable.ic_delete);
                            eliminarView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Estas segur@?")
                                            .setContentText("No podrás recuperar esta consulta!")
                                            .setConfirmText("Si, Eliminar!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    ref.child(consultaId[0]).removeValue();
                                                    notifyDataSetChanged();
                                                    sweetAlertDialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            });

                        }
                        doctorNombreC.setText("Dr. "+doctor.getnombreC());
                        dia.setText(consulta.getfecha());
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
                Glide.with(mContext).load(uri).into(doctorImagen);

            }
        });
        return convertView;
    }



}
