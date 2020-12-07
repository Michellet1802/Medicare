package com.example.medicare.DoctorUI.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.medicare.DoctorUI.DoctorAceptar;
import com.example.medicare.DoctorUI.DoctorCancelar;
import com.example.medicare.DoctorUI.DoctorEnEsperaFrag;
import com.example.medicare.R;

public class LocalizadorSecciones extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;

    public LocalizadorSecciones(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0 :
                return new DoctorEnEsperaFrag();
            case 1 :
                return new DoctorAceptar();
            case 2 :
                return new DoctorCancelar();
            default:
                return null;

        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}