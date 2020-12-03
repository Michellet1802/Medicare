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
import com.example.medicare.modelo.Consulta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditarConsultaDialogo extends AppCompatDialogFragment {
    private EditText enfermedad, precio, fecha, prescripcion;
    private Button editarConsultaBtn;
    private Consulta consulta;
    private String consultaId;


    public EditarConsultaDialogo(Consulta consulta, String consultaId) {
        this.consulta = consulta;
        this.consultaId = consultaId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editar_consulta_dialog, null);
        enfermedad = view.findViewById(R.id.enfermedad);
        precio = view.findViewById(R.id.precioHosp);
        fecha = view.findViewById(R.id.fecha);
        prescripcion = view.findViewById(R.id.prescription);
        enfermedad.setText(consulta.getenfermedad());
        System.out.println(consultaId);
        precio.setText(consulta.getprecio());
        fecha.setText(consulta.getfecha());
        prescripcion.setText(consulta.getprescripcion());
        editarConsultaBtn = view.findViewById(R.id.editConsultaBtn);
        editarConsultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(prescripcion.getText()) || TextUtils.isEmpty(enfermedad.getText()) || TextUtils.isEmpty(precio.getText())) {
                    Toast.makeText(getActivity(), "El campo esta vacio", Toast.LENGTH_SHORT).show();
                } else {
                    final Consulta consult = new Consulta(consulta.getdoctorNombre(), consulta.getDoctorEmail(), consulta.getpacienteEmail(), enfermedad.getText().toString(), fecha.getText().toString(),
                            precio.getText().toString(), prescripcion.getText().toString());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Consultas");
                    ref.child(consultaId).setValue(consult).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("Editar Consulta");
                                alertDialog.setContentText("La consulta fue editada con exito !");
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
