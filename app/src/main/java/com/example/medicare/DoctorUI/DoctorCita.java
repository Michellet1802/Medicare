package com.example.medicare.DoctorUI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.medicare.DoctorUI.ui.main.LocalizadorSecciones;
import com.example.medicare.R;
import com.google.android.material.tabs.TabLayout;

public class DoctorCita extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_citas);
        LocalizadorSecciones localizadorSecciones = new LocalizadorSecciones(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(localizadorSecciones);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }
}