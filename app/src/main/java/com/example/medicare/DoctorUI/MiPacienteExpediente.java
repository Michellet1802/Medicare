package com.example.medicare.DoctorUI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.medicare.DoctorUI.ui.main.LocalizadorSeccionesPaciente;
import com.example.medicare.R;
import com.google.android.material.tabs.TabLayout;

public class MiPacienteExpediente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_paciente_expediente_medico);
        LocalizadorSeccionesPaciente sectionsPagerAdapter = new LocalizadorSeccionesPaciente(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}