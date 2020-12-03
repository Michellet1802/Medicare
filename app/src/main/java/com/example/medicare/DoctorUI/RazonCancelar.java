package com.example.medicare.DoctorUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RazonCancelar extends AppCompatDialogFragment {
    private EditText razon;
    private Button submitButton;
    private String citaId;
    private DoctorEnEspera asignar;


    public RazonCancelar(String citaId, DoctorEnEspera asignar)
    {

        this.citaId=citaId;
        this.asignar = asignar;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.razon_layout_dialogo, null);
        razon = view.findViewById(R.id.razon);
        submitButton = view.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(razon.getText())) {
                    Toast.makeText(getActivity(), "razon el campo está vacío", Toast.LENGTH_SHORT).show();
                } else {
                    String razonOfDecline = razon.getText().toString();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Citas").child(citaId);
                    databaseReference.child("Estado").setValue("Declinada");
                    databaseReference.child("razon").setValue(razonOfDecline).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                asignar.notifyDataSetChanged();
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("razon de rechazo");
                                alertDialog.setContentText("a sido completado exitosamente !");
                                alertDialog.show();
                                Button btn = alertDialog.findViewById(R.id.confirm_button);
                                btn.setBackgroundColor(Color.parseColor("#33AEB6"));
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
