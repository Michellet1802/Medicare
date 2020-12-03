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

public class EditarHospitalizacionDialogo extends AppCompatDialogFragment {
    private EditText nombreHospital, enfermedad, precio, fecha;
    private Button editHospitalBtn;
    private Hospitalizacion hospitalizacion;
    private String hospitalId;


    public EditarHospitalizacionDialogo(Hospitalizacion hospitalizacion, String hospitalId)
    {
        this.hospitalizacion = hospitalizacion;
        this.hospitalId = hospitalId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editar_hospitalizacion_dialog, null);
        nombreHospital = view.findViewById(R.id.nombreHospital);
        enfermedad = view.findViewById(R.id.enfermedad);
        precio = view.findViewById(R.id.precioHosp);
        fecha = view.findViewById(R.id.fecha);
        nombreHospital.setText(hospitalizacion.getnombreHospital());
        enfermedad.setText(hospitalizacion.getenfermedad());
        precio.setText(hospitalizacion.getprecio());
        fecha.setText(hospitalizacion.getfecha());
        editHospitalBtn = view.findViewById(R.id.editHospButton);
        editHospitalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nombreHospital.getText()) || TextUtils.isEmpty(enfermedad.getText()) || TextUtils.isEmpty(precio.getText())) {
                    Toast.makeText(getActivity(), "Un campo esta vacio", Toast.LENGTH_SHORT).show();
                } else {
                    Hospitalizacion mHospitalizacion = new Hospitalizacion(hospitalizacion.getnombreHospital(), hospitalizacion.getEmailPaciente(), fecha.getText().toString(),
                     enfermedad.getText().toString(), precio.getText().toString());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Hospitalizacion");
                    ref.child(hospitalId).setValue(mHospitalizacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("Editar hospitalizacion");
                                alertDialog.setContentText("Hospitalizacion exitosa !");
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
