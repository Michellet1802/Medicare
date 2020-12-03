package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilPacienteInfo extends AppCompatActivity {
    TextView nombreC, nss, email, celular, estadoMar, cumple;
    CircleImageView circleImageView;
    FirebaseUser usuario;
    String uid;
    String Uri;
    DatabaseReference dbReferen;
    String nombreRecibido, apellidoRecibido, nssRecibido, emailRecibido, celularRecibido, estadoMarRecibido, cumpleRecibido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_perfil_informacion);

        nombreC = findViewById(R.id.nombreCompleto);
        nss = findViewById(R.id.nss);
        email = findViewById(R.id.email);
        celular = findViewById(R.id.celular);
        estadoMar = findViewById(R.id.estado);
        cumple = findViewById(R.id.cumpleaños);
        circleImageView = findViewById(R.id.imagen_perfil);

        usuario = FirebaseAuth.getInstance().getCurrentUser();
        uid = usuario.getUid();

        dbReferen = FirebaseDatabase.getInstance().getReference("Pacientes");

        dbReferen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombreRecibido =  dataSnapshot.child(uid).child("nombres").getValue(String.class);
                apellidoRecibido =  dataSnapshot.child(uid).child("apellidos").getValue(String.class);
                nombreC.setText(nombreRecibido+" "+apellidoRecibido);
                nssRecibido = dataSnapshot.child(uid).child("nss").getValue(String.class);
                nss.setText(nssRecibido);
                emailRecibido = dataSnapshot.child(uid).child("email").getValue(String.class);
                email.setText(emailRecibido);
                celularRecibido = dataSnapshot.child(uid).child("celular").getValue(String.class);
                celular.setText(celularRecibido);
                cumpleRecibido = dataSnapshot.child(uid).child("cumpleaños").getValue(String.class);
                cumple.setText(cumpleRecibido);
                estadoMarRecibido = dataSnapshot.child(uid).child("estado").getValue(String.class);
                estadoMar.setText(estadoMarRecibido);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference profileRef = storageReference.child("imagen_perfil").child(emailRecibido + ".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                    @Override
                    public void onSuccess(android.net.Uri uri) {
                        Uri = uri.toString();
                        Picasso.get().load(uri).into(circleImageView);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void empezarActividad(View v) {
        // calcula el centro de la vista v que está pasando
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        // crea una intención, que lanza la segunda actividad y pasa las coordenadas xey
        Intent intent = new Intent(this, EditarPerfilActivity.class);
        intent.putExtra(AnimacionReveal.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(AnimacionReveal.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra("nombreC", nombreRecibido+" "+apellidoRecibido);
        intent.putExtra("nss", nssRecibido);
        intent.putExtra("email", emailRecibido);
        intent.putExtra("celular", celularRecibido);
        intent.putExtra("cumpleaños", cumpleRecibido);
        intent.putExtra("estado", estadoMarRecibido);
        intent.putExtra("imageUri", Uri);

        // simplemente comience la actividad como una transición compartida, pero establezca el paquete de opciones en nulo
        ActivityCompat.startActivity(this, intent, null);

        // para evitar comportamientos extraños anule las transiciones pendientes
        overridePendingTransition(0, 0);
    }

    public void editarPerfil(View view) {
        empezarActividad(view);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PerfilPacienteInfo.this, MenuActivity.class);
        MenuActivity.setToken(1);
        startActivity(intent);
    }
}
