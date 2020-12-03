package com.example.medicare.DoctorUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.medicare.R;
import com.example.medicare.modelo.Hospitalizacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AnadirHospitalizacionDialog extends AppCompatDialogFragment {
    private EditText nombreHospital, enfermedad, precio, fecha;
    private Button añadirHospitalizacionButton;
    private String patientEmail;


    public AnadirHospitalizacionDialog(String patientEmail)
    {
        this.patientEmail = patientEmail;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.anadir_hospitalizacion_dialogo, null);
        nombreHospital = view.findViewById(R.id.nombreHospital);
        enfermedad = view.findViewById(R.id.enfermedad);
        precio = view.findViewById(R.id.precioHosp);
        fecha = view.findViewById(R.id.fecha);
        añadirHospitalizacionButton = view.findViewById(R.id.añadirHosp);
        añadirHospitalizacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nombreHospital.getText()) || TextUtils.isEmpty(enfermedad.getText()) || TextUtils.isEmpty(precio.getText()) || TextUtils.isEmpty(fecha.getText())) {
                    Toast.makeText(getActivity(), "A field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Hospitalizacion hospitalizacion = new Hospitalizacion(nombreHospital.getText().toString(), patientEmail, fecha.getText().toString(),
                     enfermedad.getText().toString(), precio.getText().toString()+" DH");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Hospitalizacion");
                    ref.push().setValue(hospitalizacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("Añadir Hospitalizacion");
                                alertDialog.setContentText("Hospitalizacion añadida exitosamente !");
                                alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                    });


                }

            }
        });
        builder.setView(view);
        return builder.create();
    }
}
