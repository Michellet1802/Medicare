package com.example.medicare.DoctorUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.medicare.Imagen;
import com.example.medicare.AnimacionBounceInterpolator;
import com.example.medicare.R;
import com.example.medicare.modelo.Relacion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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

public class MostrarInfPaciente extends AppCompatActivity {
    private static int REQUEST_CALL = 1;
    FrameLayout frameLayout;
    CircleImageView circleImageView;
    TextView nombreC, cumpleaños;
    ProgressBar progressBar;
    FloatingActionButton fab;
    String nombreCRecibido, emailRecibido, cumpleañosRecibido, celularRecibido, nssRecibido, estadoRecibido;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_paciente_info);
        nombreC = findViewById(R.id.nombreCompleto);
        cumpleaños = findViewById(R.id.cumpleaños);
        frameLayout = findViewById(R.id.myFrameLayout);
        circleImageView = findViewById(R.id.imagen_perfil);
        progressBar = findViewById(R.id.myProgressBar);
        fab = findViewById(R.id.fab);

        Bitmap bMap = Imagen.decodeSampledBitmapFromResource(getResources(), R.drawable.background, 100, 100);
        BitmapDrawable dr = new BitmapDrawable(bMap);
        frameLayout.setBackground(dr);

        Intent intent = getIntent();
        nombreCRecibido = intent.getStringExtra("nombreCompleto");
        emailRecibido = intent.getStringExtra("email");
        cumpleañosRecibido = intent.getStringExtra("cumpleaños");
        celularRecibido = intent.getStringExtra("celular");
        nssRecibido = intent.getStringExtra("nss");
        estadoRecibido = intent.getStringExtra("estado");

        nombreC.setText(nombreCRecibido);
        cumpleaños.setText(cumpleañosRecibido);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("imagen_perfil").child(emailRecibido + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(circleImageView);
                progressBar.setVisibility(View.GONE);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Relacion");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Relacion relacion = data.getValue(Relacion.class);
                    if (relacion.getEmailPaciente().equals(emailRecibido) && relacion.getEmailDoctor().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        fab.setVisibility(View.GONE);
                        SweetAlertDialog alertDialog = new SweetAlertDialog(MostrarInfPaciente.this,SweetAlertDialog.WARNING_TYPE);
                        alertDialog.setContentText(nombreCRecibido+" es uno de tus pacientes !");
                        alertDialog.show();
                        Button btn = alertDialog.findViewById(R.id.confirm_button);
                        btn.setBackgroundColor(Color.parseColor("#33aeb6"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error al leer: " + databaseError.getCode());
            }
        });


    }

    public void phoneCall(View view) {
        ImageView phoneCallImage = findViewById(R.id.llamada);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        AnimacionBounceInterpolator interpolator = new AnimacionBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        phoneCallImage.startAnimation(animation);
        makePhoneCall();
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(MostrarInfPaciente.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MostrarInfPaciente.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        } else {
            String dial = "tel:" + celularRecibido;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                makePhoneCall();
            else {
                Toast.makeText(this, "Permiso DENEGADO", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Gmail(View view) {
        ImageView gmailImage = findViewById(R.id.gmail);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        AnimacionBounceInterpolator interpolator = new AnimacionBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        gmailImage.startAnimation(animation);
        sendMail();
    }

    private void sendMail() {
        String[] recipient = {emailRecibido};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Enviar un email al paciente"));
    }

    public void addPatient(View view) {
        Relacion relacion = new Relacion(FirebaseAuth.getInstance().getCurrentUser().getEmail(),emailRecibido);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Relacion");
        reference.push().setValue(relacion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new SweetAlertDialog(MostrarInfPaciente.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Listo")
                        .setContentText(nombreCRecibido+" fue agregado exitosamente a tu lista de pacientes !")
                        .show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new SweetAlertDialog(MostrarInfPaciente.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Intentalo de nuevo!")
                        .show();
            }
        });
    }
}
