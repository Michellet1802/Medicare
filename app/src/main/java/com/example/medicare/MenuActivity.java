package com.example.medicare;

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

public class MenuActivity extends AppCompatActivity {
    TextView nombreC, nss;
    CircleImageView imagenPerfil;
    SharedPreferences sp;
    DatabaseReference dbdReferencia;
    FirebaseUser usuario;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //SE LE AGREGO LA INSTANCIA XQ DABA ERROR
    NotificationCompat.Builder builder;
    String pacienteNombreC, pacienteNss, fechaHoy;
    int numeroCitas;
    static int token = 0;
    String[] numeros = {"uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez"};

    public static void setToken(int number)
    {
        token = number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        numeroCitas = 0;
        sp = getSharedPreferences("Iniciar Sesion", MODE_PRIVATE);
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        nombreC = findViewById(R.id.nombreCompleto);
        nss = findViewById(R.id.nss);
        imagenPerfil = findViewById(R.id.imagen_perfil);
        uid = usuario.getUid();
        Calendar c = Calendar.getInstance();
        fechaHoy = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Citas");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Cita cita = data.getValue(Cita.class);
                    if (  cita.getEmailPaciente().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) &&
                            cita.getEstado().equals("Aceptado") &&
                            cita.getfecha().equals(fechaHoy)
                    )
                        numeroCitas++;
                }
                if (numeroCitas == 1) {
                    builder = new NotificationCompat.Builder(com.example.medicare.MenuActivity.this)
                            .setSmallIcon(R.drawable.ic_heart_beats)
                            .setContentTitle("Cita diaria")
                            .setContentText("Usted tiene una cita el dia de hoy")
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#33AEB6"))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);
                }
                if (numeroCitas > 1) {
                    builder = new NotificationCompat.Builder(com.example.medicare.MenuActivity.this)
                            .setSmallIcon(R.drawable.ic_heart_beats)
                            .setContentTitle("Citas diarias")
                            .setContentText("Usted tiene " + numeros[numeroCitas - 1] + " citas el dia de hoy")
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#33AEB6"))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                }
                if (numeroCitas == 0) {
                    builder = new NotificationCompat.Builder(com.example.medicare.MenuActivity.this)
                            .setSmallIcon(R.drawable.ic_heart_beats)
                            .setContentTitle("Citas diarias")
                            .setContentText("Usted no tiene citas el dia de hoy")
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#33AEB6"))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                }
                if(token == 0) {
                    Intent intent = new Intent(com.example.medicare.MenuActivity.this, AsignarCitaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(com.example.medicare.MenuActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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


        dbdReferencia = FirebaseDatabase.getInstance().getReference("Pacientes");
        dbdReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //pacienteNombreC = dataSnapshot.child(uid).child("apellidos").getValue(String.class) + " " + dataSnapshot.child(uid).child("nombreC").getValue(String.class);
                pacienteNombreC = dataSnapshot.child(uid).child("nombreC").getValue(String.class);
                pacienteNss = dataSnapshot.child(uid).child("nss").getValue(String.class);
                nombreC.setText(pacienteNombreC);
                nss.setText(pacienteNss);
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

    public void buscarDoctor(View view) {
        Intent intent = new Intent(com.example.medicare.MenuActivity.this, BuscarDoctorEspecialidadActivity.class);
        startActivity(intent);
    }

    public void Citas(View view) {
        Intent intent = new Intent(com.example.medicare.MenuActivity.this, AsignarCitaActivity.class);
        startActivity(intent);
    }

    public void perfilInfo(View view) {
        Intent intent = new Intent(com.example.medicare.MenuActivity.this, PerfilPacienteInfo.class);
        startActivity(intent);
    }

    public void abrirExpedienteMedico(View view) {
        Intent intent = new Intent(com.example.medicare.MenuActivity.this, ExpedienteMedico.class);
        startActivity(intent);
    }

    public void cerrarSesion(View view) {
        sp.edit().putBoolean("Sesion cerrada", false).apply();
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(com.example.medicare.MenuActivity.this, MainActivity.class));

    }

    @Override
    public void onBackPressed() {

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setConfirmText("Si");
        dialog.setCancelText("No");
        dialog.setContentText("Estas seguro que deseas salir de edicare ?");
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

    public void misDoctores(View view) {
        Intent intent = new Intent(com.example.medicare.MenuActivity.this, MisDoctoresActivity.class);
        startActivity(intent);
    }
}

