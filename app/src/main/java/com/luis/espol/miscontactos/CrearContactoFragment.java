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
            //se debe poner null para refrescar la imagen y no quede la anterior
            imgViewContacto.setImageURI(null);
            //obtiene y guarda la imagen en ImageView
            imgViewContacto.setImageURI(data.getData());
            //guarda la imagen para que no se pierda en el Tag
            imgViewContacto.setTag(data.getData());
            //metodo que hace redonda la imagen
            imgViewContacto.setImageDrawable(imageRound(imgViewContacto.getDrawable()));
            //imgViewContacto.setSize();

        }else{
           // imgViewContacto.setImageResource(R.drawable.usuario);
            imgViewContacto.setTag("");

        }
    }

    private void agregarContacto(String nombre, String telefono, String email, String direccion, String uriTag) {
        Contacto nuevo=new Contacto(nombre,telefono,email,direccion,uriTag);
        //crea un intent para la comunicacion entre fragmentos
        Intent intent=new Intent("ListaContactos");
        //le dice al broadcast lo que debe hacer(agregar contacto)
        intent.putExtra("operacion", ContactoReceiver.CONTACTO_AGREGADO);
        //guarda los datos del contacto para ser enviado por el broadcast
        intent.putExtra("datos",nuevo);
        //envia informacion del contacto como nombre telefono,etc
        // al broadcast para realizar la operacion de agregar
        getActivity().sendBroadcast(intent);
    }

    private void limpiarCampos() {
        txtNombre.getText().clear();
        txtTelefono.getText().clear();
        txtEmail.getText().clear();
        txtDireccion.getText().clear();
        imgViewContacto.setImageResource(R.drawable.usuario);
        imgViewContacto.setTag("");
        //metodo que hace redonda la imagen
        imgViewContacto.setImageDrawable(imageRound(R.drawable.usuario));
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
        imgViewContacto.setTag("");
        //muestra la imagen
        imgViewContacto.setImageResource(R.drawable.usuario);
        //metodo que hace redonda la imagen
        imgViewContacto.setImageDrawable(imageRound(R.drawable.usuario));
    }
    //region Metodo Imagen Redondeada
    //metodo para redondear una imagen desde un path
    public RoundedBitmapDrawable imageRound(Drawable drawable){
        //copiamos el drawable
        Drawable originalDrawable = drawable;
        Bitmap originalBitmap;
        //convertimos el drawable en un Bitmap
        originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        //verificamos si la imagen es cuadrada(ancho/altura)
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
        //retornamos la imagen redondeada(RoundBitmapDrawable)
        return roundedDrawable;
    }
    //metodo para redondear una imagen desde un Recurso(R.drawable.imagen)
    public RoundedBitmapDrawable imageRound(int drawable){
        //extraemos el Resource(R.drawable.imagen) en un drawable
        Drawable originalDrawable = getResources().getDrawable(drawable);
        Bitmap originalBitmap;
        //convertimos el drawable en un Bitmap
        originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        //verificamos si la imagen es cuadrada
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
        //retornamos la imagen redondeada(RoundedBitmapDrawable)
        return roundedDrawable;
    }
    //endregion

}
