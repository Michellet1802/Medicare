package com.example.medicare.DoctorUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.medicare.R;
import com.example.medicare.AnimacionReveal;
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

public class PantallaDoctorPerfilInf extends AppCompatActivity {
    TextView nombreC, especialidad, email, celular, direccion;
    CircleImageView circleImageView;
    FirebaseUser user;
    String uid;
    String Uri;
    DatabaseReference databaseReference;
    String nombreCRecuperado, especialidadRecuperado, emailRecuperado, celularRecuperado, direccionRecuperado, ciudadRecuperado, codigoRecuperado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_doctor_perfil_info);
        nombreC = findViewById(R.id.nombreCompleto);
        especialidad = findViewById(R.id.especialidad);
        email = findViewById(R.id.email);
        celular = findViewById(R.id.celular);
        direccion = findViewById(R.id.direccionCompleta);
        circleImageView = findViewById(R.id.imagen_perfil);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Doctores");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombreCRecuperado =  dataSnapshot.child(uid).child("nombreC").getValue(String.class);
                nombreC.setText(nombreCRecuperado);
                especialidadRecuperado = dataSnapshot.child(uid).child("especialidad").getValue(String.class);
                especialidad.setText(especialidadRecuperado);
                emailRecuperado = dataSnapshot.child(uid).child("email").getValue(String.class);
                email.setText(emailRecuperado);
                celularRecuperado = dataSnapshot.child(uid).child("celular").getValue(String.class);
                celular.setText(celularRecuperado);
                direccionRecuperado = dataSnapshot.child(uid).child("direccion").getValue(String.class);
                ciudadRecuperado = dataSnapshot.child(uid).child("ciudad").getValue(String.class);
                direccion.setText(direccionRecuperado+", "+ciudadRecuperado);

                codigoRecuperado = dataSnapshot.child(uid).child("codigo").getValue(String.class);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference profileRef = storageReference.child("imagen_perfil").child(emailRecuperado + ".jpg");
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

    private void startRevealActivity(View v) {
        //// calcula el centro de la vista v que está pasando
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, DoctorEditarPerfil.class);
        intent.putExtra(AnimacionReveal.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(AnimacionReveal.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra("nombreC", nombreCRecuperado);
        intent.putExtra("especialidad", especialidadRecuperado);
        intent.putExtra("email", emailRecuperado);
        intent.putExtra("celular", celularRecuperado);
        intent.putExtra("direccion", direccionRecuperado);
        intent.putExtra("city", ciudadRecuperado);
        intent.putExtra("code", codigoRecuperado);
        intent.putExtra("imageUri", Uri);

        //// simplemente comienza la actividad como una transición compartida, pero configura las opciones
        ActivityCompat.startActivity(this, intent, null);

        // para evitar comportamientos extraños anule las transiciones pendientes
        overridePendingTransition(0, 0);
    }

    public void editarPerfil(View view) {
        startRevealActivity(view);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PantallaDoctorPerfilInf.this, DoctorMenuActivity.class);
        startActivity(intent);
    }
}
