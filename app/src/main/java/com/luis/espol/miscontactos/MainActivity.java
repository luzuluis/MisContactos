package com.luis.espol.miscontactos;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TabHost;

import com.luis.espol.miscontactos.util.TabsPagerAdapter;

//import android.support.v4.app.FragmentActivity;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabsPagerAdapter adapterFragment;
    private ActionBar actionBar;
    private TabHost tabHost;
    private String[] titulos_tab = {"Crear Contacto", "Lista Contactos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Iniciamos la barra de herramientas.
        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarPrincipal);
        //actionBar.setHomeButtonEnabled(false);
        setSupportActionBar(toolbar);
        inicializarTabHost();
    }
    //viewPager.setCurrentItem(tab.getPosition());
    // Tabs Creation
    private void inicializarTabHost() {
        // Iniciamos la barra de tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.TabLayoutPrincipal);

        // Añadimos las 2 tabs de las secciones.
        // Le damos modo "fixed" para que todas las tabs tengan el mismo tamaño. También le asignamos una gravedad centrada.
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //agregamos las dos pestañas
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        // Iniciamos el viewPager.
        viewPager = (ViewPager) findViewById(R.id.ViewPagerPrincipal);
        // Creamos el adaptador, al cual le pasamos por parámtro el gestor de Fragmentos y muy importante, el nº de tabs o secciones que hemos creado.
        adapterFragment = new TabsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        //seteamos el nombre de cada pestaña
        adapterFragment.setNames_tab(titulos_tab);
        // Y los vinculamos.
        viewPager.setAdapter(adapterFragment);
        // Y por último, vinculamos el viewpager con el control de tabs para sincronizar ambos.
        tabLayout.setupWithViewPager(viewPager);

        /*viewPager = (ViewPager)findViewById(R.id.viewpager);
        adapter = new TabsPagerAdapter(getSupportFragmentManager());
        actionBar = getActionBar();

        viewPager.setAdapter(adapter);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(false);
        // TODO Put here your Tabs
        for(String nombre:titulos) {
            ActionBar.Tab tab=actionBar.newTab().setText(nombre);
            tab.setTabListener(this);
            actionBar.addTab(tab);
        }*/
        /*TabHost tabHost=(TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab=tabHost.newTabSpec("tab1");
        tab.setContent(android.R.id.tab1);
        tab.setIndicator("Crear Contacto");
        tabHost.addTab(tab);*/
        //viewPager.setOnPageChangeListener(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }

/*
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
*/
    /*
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }*/
}
