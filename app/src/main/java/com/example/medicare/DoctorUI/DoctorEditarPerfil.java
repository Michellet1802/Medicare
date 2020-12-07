package com.example.medicare.DoctorUI;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicare.R;
import com.example.medicare.AnimacionReveal;
import com.example.medicare.modelo.Doctor;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorEditarPerfil extends AppCompatActivity {
    private static final int Pick_Image_Request = 1;
    AnimacionReveal mAnimacionReveal;
    CircleImageView circuloImageView;
    private Uri mImageUri;
    EditText nombreC, especialidad, email, celular, direccion, ciudad;
    String nombreCRecibido, especialidadRecibida, emailRecibido, celularRecibido, direccionRecibido, ciudadRecibida, codigoRecibido;
    String receivedImageUri;
    StorageReference mStorageReference;
    FirebaseDatabase database;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_doctor_editar_perfil_info);

            nombreC = findViewById(R.id.nombreCompleto);
            especialidad = findViewById(R.id.especialidad);
            email = findViewById(R.id.email);
            celular = findViewById(R.id.celular);
            direccion = findViewById(R.id.direccion);
            ciudad = findViewById(R.id.ciudadDoc);
            circuloImageView = findViewById(R.id.imagen_perfil);

            mStorageReference = FirebaseStorage.getInstance().getReference("imagen_perfil");

            Intent intent = this.getIntent();   //get the intent to recieve the x and y coords, that you passed before

            nombreCRecibido = intent.getStringExtra("nombreC");
            especialidadRecibida = intent.getStringExtra("especialidad");
            emailRecibido = intent.getStringExtra("email");
            celularRecibido = intent.getStringExtra("celular");
            direccionRecibido = intent.getStringExtra("direccion");
            ciudadRecibida = intent.getStringExtra("ciudad");
            codigoRecibido = intent.getStringExtra("code");
            receivedImageUri = intent.getStringExtra("imageUri");
            Uri uri = Uri.parse(receivedImageUri != null ? receivedImageUri : "");

            nombreC.setText(nombreCRecibido);
            especialidad.setText(especialidadRecibida);
            email.setText(emailRecibido);
            celular.setText(celularRecibido);
            direccion.setText(direccionRecibido);
            ciudad.setText(ciudadRecibida);
            if (uri.toString() != "") {
                Picasso.get().load(uri).into(circuloImageView);
            }

            FrameLayout rootLayout = findViewById(R.id.root); //there you have to get the root layout of your second activity
            mAnimacionReveal = new AnimacionReveal(rootLayout, intent, this);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void onBackPressed() {
        mAnimacionReveal.unRevealActivity();
    }

    public void editProfilePicture(View view) {
        openFileChooser();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Pick_Image_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_Image_Request && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(circuloImageView);
            uploadImage();
        }

    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage() {
        StorageReference ref = mStorageReference.child(emailRecibido + "." + getExtension(mImageUri));
        ref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(DoctorEditarPerfil.this, "Imagen se subio exitosamente !", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void actualizar(View view) {
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = user.getUid();
        DatabaseReference dbRef = database.getReference("Doctores");
        final Doctor doctor = new Doctor(nombreC.getText().toString(), codigoRecibido, celular.getText().toString()
                , email.getText().toString(), ciudad.getText().toString(), direccion.getText().toString(), especialidadRecibida);
        dbRef.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(doctor);
                //Intent intent = new Intent(DoctorEditarPerfil.this, PantallaDoctorPerfilInf.class);
                //startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
