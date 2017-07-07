package com.luis.espol.miscontactos.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import static android.telephony.TelephonyManager.CALL_STATE_IDLE;
import static android.telephony.TelephonyManager.CALL_STATE_OFFHOOK;

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
    private static final String TAG = "PhoneStateBroadcastReceiver";
    String incoming_number;
    private int prev_state;
    TelephonyManager telephony;

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
        //TelephonyManager object
        telephony = (TelephonyManager)contextActivity.getSystemService(Context.TELEPHONY_SERVICE);
        CustomPhoneStateListener customPhoneListener = new CustomPhoneStateListener();
        //Register our listener with TelephonyManager
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        Bundle bundle = intent.getExtras();
        String phoneNr = bundle.getString("incoming_number");
        Toast.makeText(contextActivity,"telefono numero"+phoneNr,Toast.LENGTH_SHORT).show();

        ArrayList<Contacto> lista=(ArrayList<Contacto>) intent.getSerializableExtra("datos");
        if(lista==null){
            Toast.makeText(contextActivity,"No se puede llamar al contacto,si no esta seleccionado el item",Toast.LENGTH_SHORT).show();
        }else {
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
                //Pregunta si esta vacio el campo telefono
                if(c.getTelefono()==""){
                    String nombre=c.getNombre();
                    String msg=String.format("%s Hola no se te puede llamar porque faltan los datos de tu telefono!!",nombre);
                    Toast.makeText(contextActivity, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                //Llama a la actividad que se encargara de hacer la llamada!!
                contextActivity.startActivity(llamadaIntent);
            }
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

    /* Custom PhoneStateListener */
    public class CustomPhoneStateListener extends PhoneStateListener {
        private static final String TAG = "CustomPhoneStateListener";

        @Override
        public void onCallStateChanged(int state, String incomingNumber){

            if( incomingNumber != null && incomingNumber.length() > 0 )
                incoming_number = incomingNumber;

            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    //Log.d(TAG, "CALL_STATE_RINGING");
                    Toast.makeText(contextActivity,TAG+" CALL_STATE_RINGING",Toast.LENGTH_SHORT).show();
                    prev_state=state;
                    break;

                case CALL_STATE_OFFHOOK:
                    //Log.d(TAG, "CALL_STATE_OFFHOOK");
                    Toast.makeText(contextActivity,TAG+" CALL_STATE_OFFHOOK",Toast.LENGTH_SHORT).show();
                    prev_state=state;
                    break;

                case CALL_STATE_IDLE:
                    //Log.d(TAG, "CALL_STATE_IDLE==>"+incoming_number);
                    Toast.makeText(contextActivity,TAG+"CALL_STATE_IDLE==>"+incoming_number,Toast.LENGTH_SHORT).show();
                    if((prev_state == CALL_STATE_OFFHOOK)){
                        prev_state=state;
                        //Answered Call which is ended
                    }
                    if((prev_state == TelephonyManager.CALL_STATE_RINGING)){
                        prev_state=state;
                        //Rejected or Missed call
                    }
                    break;
            }
        }
    }
}
