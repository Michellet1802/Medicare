package com.example.medicare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicare.DoctorUI.DoctorMenuActivity;
import com.example.medicare.modelo.Doctor;
import com.example.medicare.modelo.Paciente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrearCuenta extends AppCompatActivity {
    //Doctor datos
    LinearLayout doctorLinearLayout;
    RadioButton doctorRadioButton;
    EditText doctorNombreC, doctorCodigo, doctorcelular, doctorEmail, doctorCiudad, doctorDireccion, doctorEspecialidad, doctorContra1, doctorContra2;
    Button doctorBtn;
    // Paciente datos
    LinearLayout pacienteLinearL;
    RadioButton pacienteRadioButton;
    EditText pacienteNombre, pacienteApellidos, pacienteCumple, pacienteCelular, patientEmail, pacienteNSS, pacienteContra1, pacienteContra2;
    Spinner estadoMartital;
    Button pacienteBtn;
    //Cargar pantalla
    CargarDialogo ld;
    //Selector de fechas
    DatePickerDialog.OnDateSetListener DateSetListener;

    private FirebaseAuth fbAuth ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        //dialogo de loading
         ld = new CargarDialogo(CrearCuenta.this);

        // Paciente datos
        pacienteNombre = findViewById(R.id.pacienteNombres);
        pacienteApellidos = findViewById(R.id.pacienteApellido);
        pacienteCumple = findViewById(R.id.pacienteCumple);
        pacienteCelular = findViewById(R.id.pacienteCelular);
        patientEmail = findViewById(R.id.pacienteEmail);
        pacienteNSS = findViewById(R.id.patientNSS);
        pacienteContra1 = findViewById(R.id.pacienteContra1);
        pacienteContra2 = findViewById(R.id.pacienteContra2);
        pacienteRadioButton=(RadioButton)findViewById(R.id.pacienteRadioButton);
        pacienteLinearL=(LinearLayout)findViewById(R.id.pacienteLinearL);
        estadoMartital = (Spinner) findViewById(R.id.estado);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.estado_marital, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoMartital.setAdapter(adapter);
        pacienteBtn = findViewById(R.id.pacienteBtn);

        // Doctor datos
        doctorRadioButton=(RadioButton)findViewById(R.id.doctorRadioButton);
        doctorLinearLayout=(LinearLayout)findViewById(R.id.doctorLinearLayout);
        doctorNombreC = findViewById(R.id.doctorNombreC);
        doctorCodigo = findViewById(R.id.Codigo);
        doctorcelular = findViewById(R.id.doctorCelular);
        doctorEmail = findViewById(R.id.doctorEmail);
        doctorCiudad = findViewById(R.id.Ciudad);
        doctorDireccion = findViewById(R.id.Direccion);
        doctorEspecialidad = findViewById(R.id.Especialidad);
        doctorContra1 = findViewById(R.id.doctorContra1);
        doctorContra2 = findViewById(R.id.doctorContra2);
        doctorBtn = findViewById(R.id.doctorBtn);



        // Inicializa la bd
        fbAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(fbAuth.getCurrentUser() != null)
        {
            //usuario ya conectado
        }
    }

    public void registroPaciente(View view) {
        if(pacienteRadioButton.isChecked())
        {
            doctorLinearLayout.setVisibility(View.GONE);
            pacienteLinearL.setVisibility(View.VISIBLE);
        }
    }

    public void registroDoctor(View view) {
        if(doctorRadioButton.isChecked())
        {
            pacienteLinearL.setVisibility(View.GONE);
            doctorLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void registroPaciente() {
        final String primerNombre = pacienteNombre.getText().toString().trim();
        final String apellidos = pacienteApellidos.getText().toString().trim();
        final String cumpleaños = pacienteCumple.getText().toString().trim();
        final String celular = pacienteCelular.getText().toString().trim();
        final String email = patientEmail.getText().toString().trim();
        final String nss = pacienteNSS.getText().toString().trim();
        final String estado = estadoMartital.getSelectedItem().toString().trim();
        String contra = pacienteContra1.getText().toString().trim();
        String contra2 = pacienteContra2.getText().toString().trim();
        if(
                TextUtils.isEmpty(primerNombre)
                || TextUtils.isEmpty(apellidos)
                || TextUtils.isEmpty(cumpleaños)
                || TextUtils.isEmpty(celular)
                || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(nss)
                || TextUtils.isEmpty(estado)
                || TextUtils.isEmpty(contra)
                || TextUtils.isEmpty(contra2)
        )
        {
            Toast.makeText(CrearCuenta.this, "Todos los campos son requeridos !", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(!elEmailesValido(email)) {
                patientEmail.setError("Email invalido !");
                return;
            }
            if(!contra.equals(contra2))
            {
                pacienteContra1.setError("Las 2 contraseñas no coinciden");
                return;
            }
            else
            {
                if(contra.length()<=3) {
                    pacienteContra1.setError("La contraseña debe contener mas caracteres");
                    return;
                }

            }
            if(!fechaValida(cumpleaños))
            {
                pacienteCumple.setError("La fecha debe contener el siguiente formato YYYY-MM-DD !");
                return;
            }
            ld.startLoadingDialog();
            fbAuth.createUserWithEmailAndPassword(email,contra).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Paciente paciente = new Paciente(primerNombre, apellidos, cumpleaños, celular, email, nss, estado);
                    FirebaseDatabase.getInstance().getReference("Pacientes")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(paciente).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(CrearCuenta.this, "Usuario creado con exito", Toast.LENGTH_LONG).show();
                                ld.dismissDialog();
                            }
                            else {
                                Toast.makeText(CrearCuenta.this, "Error al crear el usuario", Toast.LENGTH_LONG).show();
                                ld.dismissDialog();
                            }
                        }
                    });
                }
                else {
                    ld.dismissDialog();
                    Toast.makeText(CrearCuenta.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }




        }

    private void registroDoctor() {

        final String nombreC = doctorNombreC.getText().toString().trim();
        final String codigo = doctorCodigo.getText().toString().trim();
        final String celular = doctorcelular.getText().toString().trim();
        final String Email = doctorEmail.getText().toString().trim();
        final String ciudad = doctorCiudad.getText().toString().trim();
        final String direccion = doctorDireccion.getText().toString().trim();
        final String especialidad = doctorEspecialidad.getText().toString().trim();
        String contra1 = doctorContra1.getText().toString().trim();
        String contra2 = doctorContra2.getText().toString().trim();
        if(
                TextUtils.isEmpty(nombreC)
                        || TextUtils.isEmpty(codigo)
                        || TextUtils.isEmpty(celular)
                        || TextUtils.isEmpty(Email)
                        || TextUtils.isEmpty(ciudad)
                        || TextUtils.isEmpty(direccion)
                        || TextUtils.isEmpty(especialidad)
                        || TextUtils.isEmpty(contra1)
                        || TextUtils.isEmpty(contra2)
        )
        {
            Toast.makeText(CrearCuenta.this, "Todos los campos son requeridos !", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(!elEmailesValido(Email)) {
                patientEmail.setError("El email es invalido !");
                return;
            }
            if(!contra1.equals(contra2))
            {
                pacienteContra1.setError("Las 2 contraseñas no coinciden !");
                return;
            }
            else
            {
                if(contra1.length()<=3) {
                    pacienteContra1.setError("La contraseña debe contener mas caracteres");
                    return;
                }

            }
            ld.startLoadingDialog();
            fbAuth.createUserWithEmailAndPassword(Email,contra1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Doctor doctor = new Doctor(nombreC, codigo, celular, Email, ciudad, direccion, especialidad);
                        FirebaseDatabase.getInstance().getReference("Doctores")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(CrearCuenta.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                                    ld.dismissDialog();
                                }
                                else {
                                    Toast.makeText(CrearCuenta.this, "Fallo al registrase, intentelo mas tarde", Toast.LENGTH_LONG).show();
                                    ld.dismissDialog();
                                }
                            }
                        });
                    }
                    else {
                        ld.dismissDialog();
                        Toast.makeText(CrearCuenta.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }


    public void registrarPaciente(View view) {
        registroPaciente();

    //    Intent intent = new Intent( this, MenuActivity.class );
     //   startActivity ( intent );  ME LANZA ERROR SI LE DEJO EL INTENT
       // Toast.makeText(CrearCuenta.this, "Registro exitoso, inicie sesion", Toast.LENGTH_LONG).show();
       // ld.dismissDialog();
    }

    public void registrarDoctor(View view) {
        registroDoctor();

       // Intent intent = new Intent( this, DoctorMenuActivity.class );
        //startActivity ( intent );

    }

    public static boolean elEmailesValido(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean fechaValida(String date)
    {
        String expression = "^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();

    }
}
