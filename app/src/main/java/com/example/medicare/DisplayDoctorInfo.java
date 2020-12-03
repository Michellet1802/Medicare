package com.example.medicare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayDoctorInfo extends AppCompatActivity {
    public static int REQUEST_CALL = 1;
    FrameLayout frameLayout;
    ImageView imageView;
    CircleImageView circleImageView;
    TextView nombreC, especialidad;
    String emailRecibido, celularRecibido, direccionRecibida, ciudadRecibida;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_doctor_info);
        nombreC = findViewById(R.id.nombreCompleto);
        especialidad = findViewById(R.id.especialidad);
        frameLayout = findViewById(R.id.myFrameLayout);
        imageView = findViewById(R.id.topside);
        circleImageView = findViewById(R.id.imagen_perfil);
        progressBar = findViewById(R.id.myProgressBar);
        imageView.setImageBitmap(Imagen.decodeSampledBitmapFromResource(getResources(), R.drawable.topside, 250, 500));
        Bitmap bMap = Imagen.decodeSampledBitmapFromResource(getResources(), R.drawable.background, 100, 100);
        BitmapDrawable dr = new BitmapDrawable(bMap);
        frameLayout.setBackground(dr);


        Intent intent = getIntent();

        String receivednombreC = intent.getStringExtra("nombreCompleto");
        emailRecibido = intent.getStringExtra("email");
        String receivedSpeciality = intent.getStringExtra("especialidad");
        celularRecibido = intent.getStringExtra("celular");
        direccionRecibida = intent.getStringExtra("direccion");
        ciudadRecibida = intent.getStringExtra("ciudad");

        nombreC.setText(receivednombreC);
        especialidad.setText(receivedSpeciality);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("imagen_perfil").child(emailRecibido + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(circleImageView);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void llamada(View view) {
        ImageView phoneCallImage = findViewById(R.id.llamada);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        AnimacionBounceInterpolator interpolator = new AnimacionBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        phoneCallImage.startAnimation(animation);
        llamarAlCelular();
    }

    public void Gmail(View view) {
        ImageView gmailImage = findViewById(R.id.gmail);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        AnimacionBounceInterpolator interpolator = new AnimacionBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        gmailImage.startAnimation(animation);
        enviarEmail();
    }


    public void localizacion(View view) {
        ImageView localisationImage = findViewById(R.id.localizacion);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        AnimacionBounceInterpolator interpolator = new AnimacionBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        localisationImage.startAnimation(animation);
        Intent intent = new Intent(com.example.medicare.DisplayDoctorInfo.this, DoctorLocalizacion.class);
        intent.putExtra("direccion", direccionRecibida);
        intent.putExtra("ciudad",ciudadRecibida);
        startActivity(intent);
    }

    private void enviarEmail() {
        String[] recipient = {emailRecibido};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Elige un cliente de correo electrónico"));
    }

    private void llamarAlCelular(){
        if(ContextCompat.checkSelfPermission(com.example.medicare.DisplayDoctorInfo.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(com.example.medicare.DisplayDoctorInfo.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        }
        else
        {
            String dial = "Cel:"+celularRecibido;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                llamarAlCelular();
            else
            {
                Toast.makeText(this, "Permiso DENEGADO", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void programarCita(View view) {
        Intent intent = new Intent(com.example.medicare.DisplayDoctorInfo.this, ProgramarCitaActivity.class);
        intent.putExtra("Email",emailRecibido);
        startActivity(intent);
    }
}
