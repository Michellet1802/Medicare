package com.example.medicare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicare.DoctorUI.DoctorMenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText login;
    private EditText contra;
    private TextView errorMensaje;
    CheckBox recordar;
    SharedPreferences sp;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        contra = findViewById(R.id.contra);
        errorMensaje = findViewById(R.id.errorMessage);
        recordar = findViewById(R.id.rememberMe);
        sp = getSharedPreferences("Iniciar Sesion", MODE_PRIVATE);
        String username = sp.getString("user", "");
        String password = sp.getString("pass", "");
        if (sp.getBoolean("Paciente registrado", false)) {
            recordar.setChecked(true);
            login.setText(username);
            contra.setText(password);
            login(username, password);
        }
        if(sp.getBoolean("Doctor registrado", false))
        {
            recordar.setChecked(true);
            login.setText(username);
            contra.setText(password);
            login(username, password);
        }

    }

    public void crearCuenta(View view) {
        Intent createAccountIntent = new Intent(this, CrearCuenta.class);
        startActivity(createAccountIntent);
    }


    public void iniciarSesion(View view) {
        errorMensaje.setVisibility(View.GONE);
        String inicioSesion = login.getText().toString().trim();
        String tempContrasena = contra.getText().toString().trim();
        if (
                TextUtils.isEmpty(inicioSesion)
                        || TextUtils.isEmpty(tempContrasena)
        ) {
            Toast.makeText(MainActivity.this, "El inicio de sesión o la contraseña están vacíos", Toast.LENGTH_SHORT).show();
        } else {
            login(inicioSesion, tempContrasena);
        }
    }

    public void login(String username, String password){
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#33aeb6"));
        pDialog.setTitleText("Cargando");
        pDialog.setCancelable(false);
        pDialog.show();
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    ref = FirebaseDatabase.getInstance().getReference("Doctores");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String email = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").getValue(String.class);
                            if (email == null) {
                                pDialog.hide();
                                if (recordar.isChecked()) {
                                    sp.edit().putBoolean("Paciente registrado", true).apply();
                                    sp.edit().putString("user", username).apply();
                                    sp.edit().putString("pass", password).apply();
                                } else
                                    sp.edit().putBoolean("Paciente registrado", false).apply();
                                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                MainActivity.this.startActivity(intent);
                            } else {
                                pDialog.hide();
                                if (recordar.isChecked()) {
                                    sp.edit().putBoolean("Doctor registrado", true).apply();
                                    sp.edit().putString("user", username).apply();
                                    sp.edit().putString("pass", password).apply();
                                } else
                                    sp.edit().putBoolean("Doctor registrado", false).apply();
                                Intent intent = new Intent(MainActivity.this, DoctorMenuActivity.class);
                                MainActivity.this.startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    errorMensaje.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void cambiarContra(View view) {
        CambiarContraseña cambiarContraseña = new CambiarContraseña();
        cambiarContraseña.show(getSupportFragmentManager(), "Cambiar contreña");

    }
}
