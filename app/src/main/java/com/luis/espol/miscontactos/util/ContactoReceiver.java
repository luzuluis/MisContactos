package com.luis.espol.miscontactos.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

//import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Luis on 18/03/2017.
 */
public class ContactoReceiver extends BroadcastReceiver {
    public static final int CONTACTO_AGREGADO=1;
    public static final int CONTACTO_ELIMINADO=2;
    public static final int CONTACTO_ACTUALIZADO=3;
    public static final int CONTACTO_LLAMAR=4;
    private  final ArrayAdapter<Contacto> adapterContacto;
    private Context contextActivity;
    private final int request_code=1;

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
            case CONTACTO_LLAMAR:llamarContacto(intent);break;
        }
    }

    private void llamarContacto(Intent intent) {
        ArrayList<Contacto> lista=(ArrayList<Contacto>) intent.getSerializableExtra("datos");
        if(lista==null){
            Toast.makeText(contextActivity,"No se puede llamar al contacto,si no esta seleccionado el item",Toast.LENGTH_SHORT).show();
        }else {
            int contador=0;
            for (Contacto c : lista){
                //String nombre = String.valueOf(c.getNombre());
                //adapterContacto.remove(c);
                String telefono="tel:";
                telefono+=c.getTelefono();
                Intent llamadaIntent=new Intent(Intent.ACTION_CALL, Uri.parse(telefono));
                if(ActivityCompat.checkSelfPermission(contextActivity, android.Manifest.permission.CALL_PHONE)!=
                        PackageManager.PERMISSION_GRANTED){
                    return;
                }
                //Retorna a la actividad
                contextActivity.startActivity(llamadaIntent);
                //contador++;
                String nombre=c.getNombre();
                String msg=String.format("%s ha colgada la llamda,hasta la proxima!!",nombre);
                Toast.makeText(contextActivity, msg, Toast.LENGTH_SHORT).show();
            }/*
            if (contador==1) {
                String msg = String.format("%s item ha sido eliminado de la lista!", contador);
                Toast.makeText(contextActivity, msg, Toast.LENGTH_SHORT).show();
            }else if(contador>1){
                String msg = String.format("%s items han sido eliminados de la lista!", contador);
                Toast.makeText(contextActivity, msg, Toast.LENGTH_SHORT).show();
            }*/

        }
    }

    //@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            //si se ha seleccionado de la camara
            case request_code:
                if(resultCode== Activity.RESULT_OK ){
                    //Bundle extras=data.getExtras();
                    //obtiene y guarda la imagen en ImageView
                    //imgViewContacto.setImageURI(Uri.parse(rutaArchivo));
                    //guarda la imagen para que no se pierda en el Tag

                }
                break;

        }
    }

    private void agregarContacto(Intent intent){
        Contacto contacto=(Contacto)intent.getSerializableExtra("datos");
        adapterContacto.add(contacto);
    }

    private void  eliminarContacto(Intent intent){
        ArrayList<Contacto> lista=(ArrayList<Contacto>) intent.getSerializableExtra("datos");
        if(lista==null){
            Toast.makeText(this.contextActivity,"No se podido eliminar si no esta seleccionado el item",Toast.LENGTH_SHORT).show();
        }else {
            int contador=0;
            for (Contacto c : lista){
                //String nombre = String.valueOf(c.getNombre());
                adapterContacto.remove(c);
                contador++;
                //String msg=String.format("%s ha sido eliminado de la lista!",nombre);
                //Toast.makeText(contextActivity, msg, Toast.LENGTH_SHORT).show();
            }
            if (contador==1) {
                String msg = String.format("%s item ha sido eliminado de la lista!", contador);
                Toast.makeText(contextActivity, msg, Toast.LENGTH_SHORT).show();
            }else if(contador>1){
                String msg = String.format("%s items han sido eliminados de la lista!", contador);
                Toast.makeText(contextActivity, msg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void actualizarContacto(Intent intent){
        Contacto contacto=(Contacto)intent.getSerializableExtra("datos");
        int posicion=adapterContacto.getPosition(contacto);
        adapterContacto.insert(contacto,posicion);

    }
}
