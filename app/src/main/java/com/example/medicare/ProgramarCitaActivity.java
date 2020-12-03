package com.example.medicare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.medicare.modelo.Cita;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProgramarCitaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView hora,dia;
    Intent intent;
    DatabaseReference bdReferencia;
    FirebaseUser usuario;
    List<Cita> citas;
    String emailPaciente, emailDoctor;
    String horaCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_citas);
        dia = findViewById(R.id.fecha);
        hora = findViewById(R.id.hora);
        intent = getIntent();
        emailDoctor = intent.getStringExtra("email");
        citas = new ArrayList<>();

    }

    public void elegirDia(View view) {
        DialogFragment datePicker = new SelectorFechas();
        datePicker.show(getSupportFragmentManager(),"Elegir dia");
    }

    @Override
    public void onDateSet(DatePicker view, int año, int mes, int diaDelMes) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, año);
        c.set(Calendar.MONTH, mes);
        c.set(Calendar.DAY_OF_MONTH, diaDelMes);
        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            new SweetAlertDialog(ProgramarCitaActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No puedes programar una cita el domingo !")
                    .show();
            double number = 1.3;
            System.out.println((int) number);
            System.out.println(number - (int)number);
            return;
        }
        final String currentDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Citas");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                citas.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Cita cita = data.getValue(Cita.class);
                    if( /*cita.getEmailDoctor().equals(emailDoctor) && */ cita.getfecha().equals(currentDateString))
                    {
                        if(cita.getEmailPaciente().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        {
                            new SweetAlertDialog(ProgramarCitaActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("No puede tener más de una cita con el mismo médico en un día")
                                    .show();
                            return;
                        }
                        citas.add(cita);
                    }
                }
                if(citas.size() == 14)
                {
                    new SweetAlertDialog(ProgramarCitaActivity.this)
                            .setTitleText("Programar una cita!")
                            .setContentText("el dia elegido esta lleno, por favor elija otro dia !")
                            .show();
                    return;
                }
                else
                {
                    double timeFull = (double)(citas.size()*30)/60;
                    int hour = 9 + (int)timeFull;
                    double minutes = timeFull - (int)timeFull;
                    System.out.println(minutes);
                    if(minutes >= 0.5) {
                        horaCita = hour+":30";
                    }
                    else
                    {
                        horaCita = hour+":00";
                    }
                }
                new SweetAlertDialog(ProgramarCitaActivity.this)
                        .setTitleText("Programar Cita")
                        .setContentText("Si confirma a continuación, su cita se programará el dia "+currentDateString+" a las "+horaCita+" !")
                        .show();
                dia.setText(currentDateString);
                dia.setVisibility(View.VISIBLE);
                hora.setText(horaCita);
                hora.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void confirm(View view) {
        if(TextUtils.isEmpty(dia.getText()) && TextUtils.isEmpty(hora.getText()))
        {
            new SweetAlertDialog(ProgramarCitaActivity.this)
                    .setTitleText("Programación de cita")
                    .setContentText("Debe elegir un dia por favor")
                    .show();
            return;
        }
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        bdReferencia = FirebaseDatabase.getInstance().getReference("Pacientes");
        bdReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailPaciente = dataSnapshot.child(usuario.getUid()).child("email").getValue(String.class);
                Cita cita = new Cita(dia.getText().toString(),hora.getText().toString(), emailDoctor, emailPaciente);
                DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("Citas");
                referencia.push().setValue(cita).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new SweetAlertDialog(ProgramarCitaActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Felicidades")
                                .setContentText("Su cita fue registrada exitosamente")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(ProgramarCitaActivity.this, MenuActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new SweetAlertDialog(ProgramarCitaActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Ocurrio un error!")
                                .show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void cancelar(View view) {
        finish();
    }
}
