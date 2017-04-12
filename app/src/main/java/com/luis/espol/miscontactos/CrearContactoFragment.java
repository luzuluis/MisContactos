package com.luis.espol.miscontactos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import android.app.Fragment;
//import android.support.v4.app.Fragment;

/*
 * Created by Luis on 18/03/2017.
 */
public class CrearContactoFragment extends Fragment implements View.OnClickListener {
    private EditText txtNombre,txtTelefono,txtEmail,txtDireccion;
    private ImageView imgViewContacto;
    private Button btnAgregar;
    private final int request_code=1;
    private final int request_select=2;
    private String rutaArchivo;
    //instancear clase que controla la imagen
    private BitmapImagen imgContacto;
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
            final CharSequence[] opciones={"Tomar Foto","Elegir Galeria","Elegir Open Document","Cancelar"};
            final AlertDialog.Builder dialogo=new AlertDialog.Builder(view.getContext());
            dialogo.setTitle("Elige una opcion!!");
            dialogo.setItems(opciones,new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int seleccion) {
                    Intent intent=null;
                    switch (opciones[seleccion].toString()){
                        case "Tomar Foto":
                            activaCamara();
                            break;
                        case "Elegir Galeria":
                            intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            //startActivityForResult(intent,request_select);
                            startActivityForResult(intent.createChooser(intent,"Seleccione App de Imagen"),request_select);
                            break;
                        case "Elegir Open Document":
                            if(Build.VERSION.SDK_INT<19){
                                intent=new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                            }else {
                                intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                            }
                            intent.setType("image/*");
                            //startActivityForResult(intent,request_select);
                            startActivityForResult(intent.createChooser(intent,"Seleccione App de Imagen"),request_select);
                            break;
                        default:
                            //cierra la ventana de dialogo
                            dialog.dismiss();
                            break;
                    }
                }
            });
            dialogo.show();
        }

    }

    //Convierte una url en un Bitmap
    // imgViewContacto.setImageBitmap(bitmap);
    private Bitmap decodeBitmap(Uri dir){
        Bitmap bitmap;
        bitmap= BitmapFactory.decodeFile(String.valueOf(dir));
        return bitmap;
    }
    private void activaCamara() {
        String APP_DIRECTORY = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        String MEDIA_DIRECTORY = "media";
        File archivo=new File(APP_DIRECTORY, MEDIA_DIRECTORY);
        final boolean mkdir = archivo.mkdir();
        String EXTENSION_NAME = ".jpg";
        rutaArchivo= APP_DIRECTORY +File.separator+ MEDIA_DIRECTORY +File.separator+getDateFormat()+ EXTENSION_NAME;
        File mi_foto=new File(rutaArchivo);
        try {
            final boolean newFile = mi_foto.createNewFile();
        } catch (IOException ex) {
            String msg="Error: "+ex;
            Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
        }
        //
        Uri uri = Uri.fromFile( mi_foto );
        //Abre la camara para tomar la foto
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Guarda imagen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //Retorna a la actividad
        startActivityForResult(cameraIntent, request_code);
    }

    @SuppressLint("SimpleDateFormat")
    private String getDateFormat()
     {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date() );
        //String photoCode = "pic_" + date;
         String photoCode="";
         String FILE_NAME = "pic_";
         photoCode= FILE_NAME + date;
         return photoCode;
     }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            //si se ha seleccionado de la camara
            case request_code:
                if(resultCode== Activity.RESULT_OK ){
                    //Bundle extras=data.getExtras();
                    //obtiene y guarda la imagen en ImageView
                    //imgViewContacto.setImageURI(Uri.parse(rutaArchivo));
                    //guarda la imagen para que no se pierda en el Tag
                    imgViewContacto.setTag(Uri.parse(rutaArchivo));
                    RoundedBitmapDrawable rd=null;
                    //metodo que hace redonda la imagen
                    rd=imgContacto.imageCircle(Uri.parse(rutaArchivo),200,200);

                    if (rd == null){
                        imgViewContacto.setImageResource(R.drawable.usuario);
                        imgViewContacto.setTag("");
                    }else {
                        //imgViewContacto.setImageBitmap(rd);
                        imgViewContacto.setImageDrawable(rd);
                        //imgViewContacto.setImageURI(Uri.parse(rutaArchivo));
                    }
                }else{
                 imgViewContacto.setTag("");
                }
                break;
            //si se ha seleccionado de la galeria
            case request_select:
                if(resultCode== Activity.RESULT_OK ){
                    //obtiene y guarda la imagen en ImageView
                    //imgViewContacto.setImageURI(data.getData());
                    //guarda la imagen para que no se pierda en el Tag
                    imgViewContacto.setTag(data.getData());
                    //metodo que hace redonda la imagen
                    RoundedBitmapDrawable rd=null;
                    rd=imgContacto.imageCircle(data.getData(),200,200);
                    if (rd == null){
                        imgViewContacto.setImageResource(R.drawable.usuario);
                        imgViewContacto.setTag("");
                    }else {
                        //imgViewContacto.setImageBitmap(rd);
                        imgViewContacto.setImageDrawable(rd);
                        //imgViewContacto.setImageURI(data.getData());
                    }
                    break;
                }else {
                    imgViewContacto.setTag("");
                }
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
        imgViewContacto.setImageDrawable(imgContacto.imageRound(R.drawable.usuario));
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
        RoundedBitmapDrawable imagen=null;
        imgContacto=new BitmapImagen(getContext());
        //metodo que hace redonda la imagen
        imagen=imgContacto.imageRound(R.drawable.usuario);
        imgViewContacto.setImageDrawable(imagen);
    }

}
