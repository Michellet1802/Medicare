package com.example.medicare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MostrarActividad extends AppCompatActivity {

    TextView nombreC, email, speciality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lista_informacion);

        nombreC = findViewById(R.id.usuarioNombreC);
        email = findViewById(R.id.emailDir);
        speciality = findViewById(R.id.usuarioEspe);

        Intent intent = getIntent();

        String recibidoNombreC = intent.getStringExtra("nombreC");
        String recibidoEmail = intent.getStringExtra("email");
        String recibidaEspecialidad = intent.getStringExtra("especialidad");
        System.out.println(recibidoEmail);

        nombreC.setText(recibidoNombreC);
        email.setText(recibidoEmail);
        speciality.setText(recibidaEspecialidad);


    }
}
