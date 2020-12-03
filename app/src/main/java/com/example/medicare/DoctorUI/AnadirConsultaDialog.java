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

public class AnadirConsultaDialog extends AppCompatDialogFragment {
    private EditText enfermedad, precio, fecha, prescripcion;
    private Button añadirConsultaBtn;
    private String nombreDr, doctorEmail, pacienteEmail;


    public AnadirConsultaDialog(String nombreDr, String doctorEmail, String pacienteEmail)
    {
        this.nombreDr = nombreDr;
        this.doctorEmail = doctorEmail;
        this.pacienteEmail = pacienteEmail;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.anadir_consulta_dialogo, null);
        enfermedad = view.findViewById(R.id.enfermedad);
        precio = view.findViewById(R.id.precioHosp);
        fecha = view.findViewById(R.id.fecha);
        prescripcion = view.findViewById(R.id.prescription);
        añadirConsultaBtn = view.findViewById(R.id.añadirConsultaBtn);
        añadirConsultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(prescripcion.getText()) || TextUtils.isEmpty(enfermedad.getText()) || TextUtils.isEmpty(precio.getText())) {
                    Toast.makeText(getActivity(), "A field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Consulta consulta = new Consulta(nombreDr, doctorEmail, pacienteEmail, enfermedad.getText().toString(), fecha.getText().toString(),
                            precio.getText().toString()+" MXN ", prescripcion.getText().toString());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Consultas");
                    ref.push().setValue(consulta).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("Añadir consulta");
                                alertDialog.setContentText("Consulta añadida exitosamente !");
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
