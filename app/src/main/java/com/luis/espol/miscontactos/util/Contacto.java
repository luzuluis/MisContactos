package com.luis.espol.miscontactos.util;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Luis on 18/03/2017.
 */
public class Contacto implements Serializable {
    private String nombre,telefono,email,direccion;
    private String imageUri;
    private ImageView imageView;


    public Contacto(String nombre, String telefono, String email, String direccion, String imageUri) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.imageUri = imageUri;
    }
    //region Metodos Get
    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getImageUri() {
        return imageUri;
    }
    //endregion
    //region Metodos Set
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
        this.imageView.setImageResource(Integer.parseInt(imageUri));
    }

    //public static Bitmap getRoundedCornerBitmap(Drawable drawable, boolean square) {
    public static Bitmap getRoundedCornerBitmap(ImageView drawable, boolean square) {
        int width = 0;
        int height = 0;
        //convierto mi ImageView a Bitmap
        drawable.buildDrawingCache();
        Bitmap bitmap = drawable.getDrawingCache();
        //
        //Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap() ;
        if(square){
            if(bitmap.getWidth() < bitmap.getHeight()){
                width = bitmap.getWidth();
                height = bitmap.getWidth();
            } else {
                width = bitmap.getHeight();
                height = bitmap.getHeight();
            }
        } else {
            height = bitmap.getHeight();
            width = bitmap.getWidth();
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = 90;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contacto contacto = (Contacto) o;
        if (!nombre.equals(contacto.nombre)) return false;
        if (telefono != null ? !telefono.equals(contacto.telefono) : contacto.telefono != null)
            return false;
        if (email != null ? !email.equals(contacto.email) : contacto.email != null) return false;
        if (direccion != null ? !direccion.equals(contacto.direccion) : contacto.direccion != null)
            return false;
        return imageUri != null ? imageUri.equals(contacto.imageUri) : contacto.imageUri == null;

    }

    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + (telefono != null ? telefono.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
        result = 31 * result + (imageUri != null ? imageUri.hashCode() : 0);
        return result;
    }
}
