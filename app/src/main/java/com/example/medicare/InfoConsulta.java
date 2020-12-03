package com.example.medicare;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicare.modelo.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoConsulta extends AppCompatActivity {
    CircleImageView imagen_perfil;
    TextView nombreC, especialidad, fecha, precio, enfemermedad;
    String prescripcionRecibida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_info);
        imagen_perfil = findViewById(R.id.imagen_perfil);
        nombreC = findViewById(R.id.nombreCompleto);
        especialidad = findViewById(R.id.especialidad);
        fecha = findViewById(R.id.fecha);
        precio = findViewById(R.id.precioHosp);
        enfemermedad = findViewById(R.id.enfermedad);
        Intent intent = getIntent();
        String receivednombreC = intent.getStringExtra("nombreDoc");
        final String emailDoctorRecibido = intent.getStringExtra("doctorEmail");
        String fechaRecibida = intent.getStringExtra("fecha");
        String precioRecibido = intent.getStringExtra("precioHosp");
        String enfermedadRecibida = intent.getStringExtra("enfermedad");
        prescripcionRecibida = intent.getStringExtra("prescripcion");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("imagen_perfil").child(emailDoctorRecibido + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imagen_perfil);
            }
        });

        nombreC.setText(receivednombreC);
        fecha.setText(fechaRecibida);
        precio.setText(precioRecibido);
        enfemermedad.setText(enfermedadRecibida);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctores");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    if(doctor.getEmail().equals(emailDoctorRecibido)) {
                        especialidad.setText(doctor.getespecialidad());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer : " + databaseError.getCode());
            }
        });
    }

    public void prescription(View view) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setTitleText("Prescripcion");
        sweetAlertDialog.setContentText(prescripcionRecibida);
        sweetAlertDialog.show();
        Button button = sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM);
        button.setBackgroundColor(Color.parseColor("#33aeb6"));

    }
}
