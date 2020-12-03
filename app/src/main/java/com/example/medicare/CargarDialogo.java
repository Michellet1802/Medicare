package com.example.medicare;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class CargarDialogo {
    private Activity activity;
    private AlertDialog dialog;
    public CargarDialogo(Activity activity)
    {
        this.activity=activity;
    }
    void startLoadingDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialogo_personalizado, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();


    }
    void dismissDialog()
    {
        dialog.dismiss();
    }
}
