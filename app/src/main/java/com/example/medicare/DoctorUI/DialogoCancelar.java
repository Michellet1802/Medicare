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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogoCancelar extends AppCompatDialogFragment {
    private EditText razon;
    private Button confirmarBtn, cancelarBtn;
    String antrazon;
    private String citaId;
    private DoctorCitaAdaptador adaptador;


    public DialogoCancelar(String citaId, DoctorCitaAdaptador adaptador, String antrazon)
    {
        this.citaId=citaId;
        this.adaptador = adaptador;
        this.antrazon = antrazon;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editar_razon_layout_dialogo, null);
        razon = view.findViewById(R.id.razon);
        razon.setText(antrazon);
        confirmarBtn = view.findViewById(R.id.confirmarBtn);
        cancelarBtn = view.findViewById(R.id.cancelarBtn);
        confirmarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(razon.getText())) {
                    Toast.makeText(getActivity(), "El campo esta vacio", Toast.LENGTH_SHORT).show();
                } else {
                    String razonOfDecline = razon.getText().toString();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("citas").child(citaId);
                    databaseReference.child("razon").setValue(razonOfDecline).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                adaptador.notifyDataSetChanged();
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("Razon de cancelacion");
                                alertDialog.setContentText("Su razon a sido editada exitosamente !");
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
        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }
}
