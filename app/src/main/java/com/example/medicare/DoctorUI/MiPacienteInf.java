package com.example.medicare.DoctorUI;

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

import com.example.medicare.Imagen;
import com.example.medicare.AnimacionBounceInterpolator;
import com.example.medicare.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MiPacienteInf extends AppCompatActivity {
    private static int REQUEST_CALL = 1;
    FrameLayout frameLayout;
    CircleImageView circleImageView;
    TextView nombreC, cumpleaños;
    ProgressBar progressBar;
    String nombreCRec, recibEmail, cumpleRecibido, celularRecibido, nssRecibido, estadoRecibido, relacionRecibida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_paciente_info);
        nombreC = findViewById(R.id.nombreCompleto);
        cumpleaños = findViewById(R.id.cumpleaños);
        frameLayout = findViewById(R.id.myFrameLayout);
        circleImageView = findViewById(R.id.imagen_perfil);
        progressBar = findViewById(R.id.myProgressBar);

        Bitmap bMap = Imagen.decodeSampledBitmapFromResource(getResources(), R.drawable.background, 100, 100);
        BitmapDrawable dr = new BitmapDrawable(bMap);
        frameLayout.setBackground(dr);

        Intent intent = getIntent();
        nombreCRec = intent.getStringExtra("nombreCompleto");
        recibEmail = intent.getStringExtra("email");
        cumpleRecibido = intent.getStringExtra("cumpleaños");
        celularRecibido = intent.getStringExtra("celular");
        nssRecibido = intent.getStringExtra("nss");
        estadoRecibido = intent.getStringExtra("estado");
        relacionRecibida = intent.getStringExtra("relacionRecibida");

        nombreC.setText(nombreCRec);
        cumpleaños.setText(cumpleRecibido);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("Imagen de Perfil").child(recibEmail + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(circleImageView);
                progressBar.setVisibility(View.GONE);
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
        if (ContextCompat.checkSelfPermission(MiPacienteInf.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MiPacienteInf.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

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
        String[] recipient = {recibEmail};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Enviar un email al paciente"));
    }

    public void displayMF(View view) {
        ImageView medicalFolder = findViewById(R.id.expedienteMedico);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        AnimacionBounceInterpolator interpolator = new AnimacionBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        medicalFolder.startAnimation(animation);
        Intent intent = new Intent(MiPacienteInf.this, MiPacienteExpediente.class);
        MiPacienteConsultaFrag.setEmailPaciente(recibEmail);
        MiPacienteHospitalizacion.setEmailPaciente(recibEmail);
        startActivity(intent);

    }

    public void deletePatient(View view) {
        new SweetAlertDialog(MiPacienteInf.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Eliminar paciente")
                .setContentText("Estas seguro de eliminar a  "+nombreCRec+" de tu lista de pacientes ?")
                .setConfirmText("Si, seguro !")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Relacion");
                        ref.child(relacionRecibida).removeValue();
                        MiPacienteActivity.notifyAdapter();
                        sweetAlertDialog.dismiss();
                        Intent intent = new Intent(MiPacienteInf.this, MiPacienteActivity.class);
                        startActivity(intent);
                    }
                })
                .show();

    }
}
