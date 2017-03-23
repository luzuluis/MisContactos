package com.luis.espol.miscontactos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luis.espol.miscontactos.util.Contacto;
import com.luis.espol.miscontactos.util.ContactoReceiver;
import com.luis.espol.miscontactos.util.TextChangedListener;

import static com.luis.espol.miscontactos.util.Contacto.getRoundedCornerBitmap;

//import android.app.Fragment;
//import android.support.v4.app.Fragment;

/*
 * Created by Luis on 18/03/2017.
 */
public class CrearContactoFragment extends Fragment implements View.OnClickListener {
    private EditText txtNombre,txtTelefono,txtEmail,txtDireccion;
    private ImageView imgViewContacto;
    private Button btnAgregar;
    private int request_code=1;
    @Override
    public void onClick(View view) {
        if(view==btnAgregar){
            agregarContacto(txtNombre.getText().toString(),
                    txtTelefono.getText().toString(),
                    txtEmail.getText().toString(),
                    txtDireccion.getText().toString(),
                    String.valueOf(imgViewContacto.getTag())
            );
            String msg=String.format("%s ha sido agregado a la lista!",txtNombre.getText());
            Toast.makeText(view.getContext(),msg,Toast.LENGTH_SHORT).show();
            btnAgregar.setEnabled(false);
            limpiarCampos();
        }else if(view==imgViewContacto){
            Intent intent=null;
            if(Build.VERSION.SDK_INT<19){
                intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
            }else {
                intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            }
            intent.setType("image/*");
            startActivityForResult(intent,request_code);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK && requestCode==request_code){
            imgViewContacto.setImageURI(data.getData());
            imgViewContacto.setTag(data.getData());
            imgViewContacto.setImageBitmap(getRoundedCornerBitmap(imgViewContacto,true));
        }
    }

    private void agregarContacto(String nombre, String telefono, String email, String direccion, String uriTag) {
        Contacto nuevo=new Contacto(nombre,telefono,email,direccion,uriTag);
        //adapter.add(nuevo);
        Intent intent=new Intent("ListaContactos");
        intent.putExtra("operacion", ContactoReceiver.CONTACTO_AGREGADO);
        intent.putExtra("datos",nuevo);
        getActivity().sendBroadcast(intent);
    }

    private void limpiarCampos() {
        txtNombre.getText().clear();
        txtTelefono.getText().clear();
        txtEmail.getText().clear();
        txtDireccion.getText().clear();
        imgViewContacto.setImageResource(R.drawable.usuario);
        txtNombre.requestFocus();
    }
    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_crear_contacto,container,false);
        inicializarComponentes(rootView);
        return rootView;
    }

    private void inicializarComponentes(final View view) {
        txtNombre=(EditText)view.findViewById(R.id.cmpNombre);
        txtTelefono=(EditText)view.findViewById(R.id.cmpTelefono);
        txtEmail=(EditText)view.findViewById(R.id.cmpEmail);
        txtDireccion=(EditText)view.findViewById(R.id.cmpDireccion);
        imgViewContacto=(ImageView)view.findViewById(R.id.imgViewContacto);
        //evento click de la imagen para seleccionar desde el dispositivo
        // y otro tipo de almacenamiento
        imgViewContacto.setOnClickListener(this);
        btnAgregar=(Button)view.findViewById(R.id.btnAgregar);
        //se activa el boton de agregar cuando el campo nombre no esta vacio
        //de lo contrario se desactiva,asi se grabara por lo menos un dato
        txtNombre.addTextChangedListener(new TextChangedListener(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnAgregar.setEnabled(!s.toString().trim().isEmpty());

            }
        });
        //evento click de agregar los datos del contacto
        btnAgregar.setOnClickListener(this);
        //asignamos la imagen de usuario en un int
        //como parametro en la funcion que nos redondea la imagen
        int imageUsuario = R.drawable.usuario;
        //asignamos la imagen redonda que nos devuelve la funcion(imageRound)
        // en el ImageView de la actividad
        imgViewContacto.setImageDrawable(imageRound(imageUsuario));
    }
    public RoundedBitmapDrawable imageRound(int drawable){
        //extraemos el drawable en un bitmap
        Drawable originalDrawable = getResources().getDrawable(drawable);
        Bitmap originalBitmap;
        originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        if (originalBitmap.getWidth() > originalBitmap.getHeight()){
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getHeight(), originalBitmap.getHeight());
        }else if (originalBitmap.getWidth() < originalBitmap.getHeight()) {
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getWidth());
        }
        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
        RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);
        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());
        return roundedDrawable;
    }
}
