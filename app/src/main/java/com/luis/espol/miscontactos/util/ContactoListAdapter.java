package com.luis.espol.miscontactos.util;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luis.espol.miscontactos.R;

import java.util.List;

/**
 * Created by Luis on 18/03/2017.
 */
public class ContactoListAdapter extends ArrayAdapter<Contacto> {
    private Activity ctx;

    //private List<Contacto> contactos=new ArrayList<Contacto>();
    public ContactoListAdapter(Activity context, List<Contacto> contactos) {
        super(context, R.layout.listview_item, contactos);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = ctx.getLayoutInflater().inflate(R.layout.listview_item, parent, false);
        }
        Contacto actual = this.getItem(position);
        InicializarCamposdeTexto(view, actual);
        return view;
    }


    private void InicializarCamposdeTexto(View view, Contacto actual) {
        TextView textview=(TextView)view.findViewById(R.id.viewNombre);
        textview.setText(actual.getNombre());
        textview=(TextView)view.findViewById(R.id.viewTelefono);
        textview.setText(actual.getTelefono());
        textview=(TextView)view.findViewById(R.id.viewEmail);
        textview.setText(actual.getEmail());
        textview=(TextView)view.findViewById(R.id.viewDireccion);
        textview.setText(actual.getDireccion());
        ImageView imgContactoImage=(ImageView)view.findViewById(R.id.imgViewContacto);
        String isImages=String.valueOf(actual.getImageUri());
         if(isImages.equals("")){
            //Toast.makeText(view.getContext(), "No hay imagen", Toast.LENGTH_SHORT).show();
            imgContactoImage.setImageResource(R.drawable.usuario);
        } else {
            Uri uriImage= Uri.parse(actual.getImageUri());
            String pathImage=actual.getImageUri();
            //String msg=String.format(pathImage,"%s ha sido agregada al Contacto de la lista!");
            //Toast.makeText(view.getContext(), "imagen "+pathImage, Toast.LENGTH_SHORT).show();
            imgContactoImage.setImageURI(uriImage);
        }
    }
}