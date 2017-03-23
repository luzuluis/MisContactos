package com.luis.espol.miscontactos.util;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.luis.espol.miscontactos.CrearContactoFragment;
import com.luis.espol.miscontactos.ListaContactosFragment;

/*import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;*/

/**
 * Created by Luis on 18/03/2017.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    int PAGE_COUNT ;
    String[] names_tab = {"TAB 1", "TAB 2"};

    public TabsPagerAdapter(FragmentManager fm,int page_count)
    {
        super(fm);
        this.PAGE_COUNT=page_count;
    }

    public void setNames_tab(String[] names_tab) {
        this.names_tab = names_tab;
    }

    @Override
    public Fragment getItem(int position) {
        // recibimos la posición por parámetro y en función de ella
        // devolvemos el Fragment correspondiente a esa sección.
        switch (position){
            case 0:return new CrearContactoFragment();
            case 1: return new ListaContactosFragment();
            // si la posición recibida no se corresponde a ninguna sección
            default:return null;
        }
    }
    /*  getCount() devuelve el nº de secciones, dato que recibiremos cuando instanciemos el adaptador
        desde nuestra actividad principal */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        // recibimos la posición por parámetro y en función de ella devolvemos el titulo correspondiente.
        switch (position) {

            case 0: // siempre empieza desde 0, la primera Tab es 0
                //return "Sección 1";
                return names_tab[0];
            case 1:
                //return "Sección 2";
                return names_tab[1];
            /*case 2:
                return "Sección 3";*/

            // si la posición recibida no se corresponde a ninguna sección
            default:
                return null;
        }

    }

}
