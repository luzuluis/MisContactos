package com.luis.espol.miscontactos.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Luis on 18/03/2017.
 */
public class ContactoReceiver extends BroadcastReceiver {
    public static final int CONTACTO_AGREGADO=1;
    public static final int CONTACTO_ELIMINADO=2;
    public static final int CONTACTO_ACTUALIZADO=3;
    private  final ArrayAdapter<Contacto> adapterContacto;
    private Context contextActivity;

    public ContactoReceiver(ArrayAdapter<Contacto> adapter) {
        this.adapterContacto = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int operacion=intent.getIntExtra("operacion",-1);
        this.contextActivity=context;
        switch(operacion){
            case CONTACTO_AGREGADO:agregarContacto(intent);break;
            case CONTACTO_ELIMINADO:eliminarContacto(intent);break;
            case CONTACTO_ACTUALIZADO:actualizarContacto(intent);break;

        }
    }
    private void agregarContacto(Intent intent){
        Contacto contacto=(Contacto)intent.getSerializableExtra("datos");
        adapterContacto.add(contacto);
    }

    private void  eliminarContacto(Intent intent){
        ArrayList<Contacto> lista=(ArrayList<Contacto>) intent.getSerializableExtra("datos");
        if(lista==null){
            Toast.makeText(this.contextActivity,"No se puede eliminar si no esta seleccionado el item",Toast.LENGTH_SHORT).show();
        }else {
            for (Contacto c : lista) adapterContacto.remove(c);
        }
    }

    private void actualizarContacto(Intent intent){
        Contacto contacto=(Contacto)intent.getSerializableExtra("datos");
        int posicion=adapterContacto.getPosition(contacto);
        adapterContacto.insert(contacto,posicion);

    }
}
