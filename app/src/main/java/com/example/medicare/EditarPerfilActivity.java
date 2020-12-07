package com.example.medicare;

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

import com.example.medicare.modelo.Paciente;
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

public class EditarPerfilActivity extends AppCompatActivity {
    private static final int Pick_Image_Request = 1;
    AnimacionReveal mAnimacionReveal;
    CircleImageView circleImageView;
    private Uri mImageUri;
    EditText nombreC, nss, email, celular, cumpleaños, estadoMar;
    String recibnombreC, recibnss, recibEmail, recibcelular, recibcumpleaños, recibestadoMar;
    String recibImageUri;
    StorageReference mStorageReference;
    FirebaseDatabase basedatos;
    FirebaseUser usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_perfil);

            nombreC = findViewById(R.id.nombreCompleto);
            nss = findViewById(R.id.nss);
            email = findViewById(R.id.email);
            celular = findViewById(R.id.celular);
            cumpleaños = findViewById(R.id.cumpleaños);
            estadoMar = findViewById(R.id.estado);
            circleImageView = findViewById(R.id.imagen_perfil);

            mStorageReference = FirebaseStorage.getInstance().getReference("imagen_perfil");

            Intent intent = this.getIntent();   //get the intent to recieve the x and y coords, that you passed before


            recibnombreC = intent.getStringExtra("nombreCompleto");
            recibnss = intent.getStringExtra("nss");
            recibEmail = intent.getStringExtra("email");
            recibcelular = intent.getStringExtra("celular");
            recibcumpleaños = intent.getStringExtra("cumpleaños");
            recibestadoMar = intent.getStringExtra("estado");
            recibImageUri = intent.getStringExtra("imageUri");
            Uri uri = Uri.parse(recibImageUri);

            nombreC.setText(recibnombreC);
            nss.setText(recibnss);
            email.setText(recibEmail);
            celular.setText(recibcelular);
            cumpleaños.setText(recibcumpleaños);
            estadoMar.setText(recibestadoMar);

            if (uri.toString() != "") {
                Picasso.get().load(uri).into(circleImageView);
            }
            FrameLayout rootLayout = findViewById(R.id.root); //there you have to get the root layout of your second activity
            mAnimacionReveal = new AnimacionReveal(rootLayout, intent, this);

        } catch (Exception e) {

        }
    }

    public void onBackPressed() {
        mAnimacionReveal.unRevealActivity();
    }

    public void editProfilePicture(View view) {
        abrirArchivos();
    }

    private void abrirArchivos() {
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
            Picasso.get().load(mImageUri).into(circleImageView);
            subirImagen();
        }

    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void subirImagen() {
        StorageReference ref = mStorageReference.child(recibEmail + "." + getExtension(mImageUri));
        ref.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> Toast.makeText(EditarPerfilActivity.this, "Imagen subida con exito", Toast.LENGTH_SHORT).show());


    }

    public void actualizar(View view) {
        basedatos = FirebaseDatabase.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = usuario.getUid();
        DatabaseReference dbRef = basedatos.getReference("Pacientes");
        String[] fn = nombreC.getText().toString().split(" ");
        final Paciente paciente = new Paciente(fn[0], fn[1], cumpleaños.getText().toString(), celular.getText().toString(), email.getText().toString()
                , nss.getText().toString(), estadoMar.getText().toString());
        dbRef.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(paciente);
                //Intent intent = new Intent(EditarPerfilActivity.this, PerfilPacienteInfo.class);
                //startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
