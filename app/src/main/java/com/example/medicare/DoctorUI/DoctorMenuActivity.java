package com.example.medicare.DoctorUI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.medicare.MainActivity;
import com.example.medicare.R;
import com.example.medicare.modelo.Cita;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorMenuActivity extends AppCompatActivity {
    TextView nombreC, especialidad;
    CircleImageView imagenPerfil;
    DatabaseReference bdReferencia;
    FirebaseUser usuario;
    SharedPreferences sp;
    String uid, fechaHoy;
    int numeroCitas;
    String[] numeros = {"uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve","diez"};
    NotificationCompat.Builder builder;
    static int token = 0;

    public static void setToken(int number)
    {
        token = number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_menu);
        numeroCitas = 0;
        Calendar c = Calendar.getInstance();
        fechaHoy = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        sp = getSharedPreferences("Iniciar Sesion",MODE_PRIVATE);
        nombreC = findViewById(R.id.nombreCompleto);
        especialidad = findViewById(R.id.especialidad);
        imagenPerfil = findViewById(R.id.imagen_perfil);
        uid = usuario.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Citas");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Cita cita = data.getValue(Cita.class);
                    if(cita.getEmailPaciente().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) &&
                            cita.getestado().equals("Aceptado") &&
                            cita.getfecha().equals(fechaHoy)
                    )
                        numeroCitas++;
                }
                if (numeroCitas == 1) {
                    builder = new NotificationCompat.Builder(DoctorMenuActivity.this)
                            .setSmallIcon(R.drawable.ic_heart_beats)
                            .setContentTitle("Citas diarias")
                            .setContentText("Tienes una cita el dia de hoy")
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#33AEB6"))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);
                }
                if (numeroCitas > 1) {
                    builder = new NotificationCompat.Builder(DoctorMenuActivity.this)
                            .setSmallIcon(R.drawable.ic_heart_beats)
                            .setContentTitle("Cita diaria")
                            .setContentText("Tu tienes " + numeros[numeroCitas - 1] + " citas por el dia de hoy")
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#33AEB6"))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                }
                if (numeroCitas == 0) {
                    builder = new NotificationCompat.Builder(DoctorMenuActivity.this)
                            .setSmallIcon(R.drawable.ic_heart_beats)
                            .setContentTitle("Citas diarias")
                            .setContentText("No tienes citas por el dia de hoy")
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#33AEB6"))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                }
                if(token == 0) {
                    Intent intent = new Intent(DoctorMenuActivity.this, DoctorCita.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(DoctorMenuActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(
                            Context.NOTIFICATION_SERVICE
                    );

                    notificationManager.notify(0, builder.build());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bdReferencia = FirebaseDatabase.getInstance().getReference("Doctores");
        bdReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombreC.setText(dataSnapshot.child(uid).child("nombreC").getValue(String.class));
                especialidad.setText(dataSnapshot.child(uid).child("especialidad").getValue(String.class));
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference profileRef = storageReference.child("imagen_perfil").child(FirebaseAuth.getInstance().getCurrentUser().getEmail() + ".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imagenPerfil);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed() {

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setConfirmText("Si");
        dialog.setCancelText("No");
        dialog.setContentText("Estas seguro que deseas salir de Medicare ?");
        dialog.setTitleText("Cerrar Aplicacion");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finishAffinity();
                System.exit(0);
            }
        });
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });
        dialog.show();
    }

    public void buscarDoctor(View view) {
        Intent intent = new Intent(DoctorMenuActivity.this, BuscarPaciente.class);
        startActivity(intent);
    }

    public void miPacientes(View view) {
        Intent intent = new Intent(DoctorMenuActivity.this, MiPacienteActivity.class);
        startActivity(intent);
    }

    public void perfilInfo(View view) {
        Intent intent = new Intent(DoctorMenuActivity.this, PantallaDoctorPerfilInf.class);
        startActivity(intent);
    }

    public void misCitas(View view) {
        Intent intent = new Intent(DoctorMenuActivity.this, DoctorCita.class);
        startActivity(intent);
    }
    public void cerrarSesion(View view) {
        sp.edit().putBoolean("Doctor registrado",false).apply();
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(DoctorMenuActivity.this, MainActivity.class));

    }
}
