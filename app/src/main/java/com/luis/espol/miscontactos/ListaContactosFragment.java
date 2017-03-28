package com.luis.espol.miscontactos;

//import android.app.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.luis.espol.miscontactos.util.Contacto;
import com.luis.espol.miscontactos.util.ContactoListAdapter;
import com.luis.espol.miscontactos.util.ContactoReceiver;

import java.util.ArrayList;

/**
 * Created by Luis on 18/03/2017.
 */
public class ListaContactosFragment extends Fragment {
    private ArrayAdapter<Contacto> adapterContacto;
    private ListView contactoListView;
    private ContactoReceiver receiver;
    private SparseBooleanArray array;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_lista_contactos,container,false);
        inicializarComponentes(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_eliminar_contacto:eliminarContacto(item);return true;
            default:return super.onOptionsItemSelected(item);
        }

    }

    private void eliminarContacto(MenuItem item) {
        array=contactoListView.getCheckedItemPositions();
        ArrayList<Contacto>seleccion=new ArrayList<Contacto>();
        if(array==null || array.size()==0)
            Toast.makeText(this.getContext(), "Seleccione algun item para poder eliminar", Toast.LENGTH_SHORT).show();
        else {
            int numContacto=array.size();
            for (int i = 0; i < numContacto; i++) {
                int posicion = array.keyAt(i);
                if (array.valueAt(i)){
                    seleccion.add(adapterContacto.getItem(posicion));
                }
            }
            Intent intent = new Intent("ListaContactos");
            intent.putExtra("operacion", ContactoReceiver.CONTACTO_ELIMINADO);
            intent.putExtra("datos", seleccion);
            //obtenemos la actividad actual y le enviamos al controlador
            //Receiver que accion hay que hacer(eliminar)
            getActivity().sendBroadcast(intent);
            /*String nombre = String.valueOf(seleccion.get(i).getNombre());
            String msg=String.format("%s ha sido eliminado de la lista!",nombre);
            //contactoListView.clearChoices();
            Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();*/
            contactoListView.clearChoices();
        }
    }

    private void inicializarComponentes(View view) {
        contactoListView=(ListView)view.findViewById(R.id.listView);
        adapterContacto=new ContactoListAdapter(getActivity(),new ArrayList<Contacto>());
        //no hace falata porque lo manejamos nosotros con el broadcast!!
        //adapterContacto.setNotifyOnChange(true);
        //Muy importante para que la lista se pueda seleccionar
        contactoListView.setItemsCanFocus (false);
        //Nos permite elegir si la seleccion de mi lista
        // va ser unica(Single) o varias(multiple)
        contactoListView.setChoiceMode (ListView.CHOICE_MODE_MULTIPLE);
        //le paso el control de mi lista al adaptador
        contactoListView.setAdapter(adapterContacto);
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver=new ContactoReceiver(adapterContacto);
        getActivity().registerReceiver(receiver,new IntentFilter("ListaContactos"));

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }
}
