package com.luis.espol.miscontactos;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by Luis on 11/04/2017.
 */
public class BitmapImagen {
    private Context context;
    public BitmapImagen(Context getResources) {
        context=getResources;
    }

    public Bitmap giraImagen(Bitmap mBitmap, String pathImage, int newWidth, int newHeight){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        //convierte el path de String a Uri y me devuelve el path completo en String
        String path=getPathUri(Uri.parse(pathImage));
        //variable que me indicara la orientacion de mi Imagen
        ExifInterface exif = null;
        try {
            //ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());
            //Es una instancia que me verifica la orientacion de mi Imagen
            exif = new ExifInterface(path);
            //me devuelve la orientacion de mi Imagen
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            //Log.d("EXIF", "Exif: " + orientation);
            // creamos una matrix para manipular la imegen(hacer escala y girarla)
            Matrix matrix = new Matrix();
            // redimenciona el bitmap de mi Imagen
            matrix.postScale(scaleWidth, scaleHeight);
            //matrix.preScale(-1.0f, 1.0f);
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            //verificamos si la imagen es cuadrada(ancho/altura)
            if (width > height) {
                width=mBitmap.getHeight();
            } else {
                height=mBitmap.getWidth();
            }
            Bitmap output=null;
            output=Bitmap.createBitmap(mBitmap,0,0,width,height,matrix,true);
            //liberamos memoria del mapa de bits
            mBitmap.recycle();
            //devolvemos el bitmap de la imagen(escaladav y girado normal)
            return output;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }
    public float convertDpToPixel(float dp){
        //Activity context=getActivity();
        Resources resources;
        resources= context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
    public float convertPixelsToDp(float px){
        //Activity context=getActivity();
        Resources resources;
        resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
    public RoundedBitmapDrawable imageCircle(Uri imagenPath, int width, int height){

        Bitmap originalBitmap = null;
        //is = getActivity().getContentResolver().openInputStream(imagenPath);
        String path = null;
        path = getPathUri(imagenPath);
        if (path != null) {
            originalBitmap = BitmapFactory.decodeFile(path);
        }else{
            return null;
        }
        Bitmap destinoBitmap = null;
        destinoBitmap=Bitmap.createScaledBitmap(originalBitmap,width,height,true);

        Bitmap output=null;
        output = giraImagen(destinoBitmap,path, width, height);
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(context.getResources(), output);
        //asignamos el CornerRadius
        //roundedDrawable.setCornerRadius(rd.getHeight());
        //hacemos el circulo de la imagen
        roundedDrawable.setCircular(true);
        return roundedDrawable;
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
    public Bitmap getCroppedBitmap(Bitmap bitmap,int width,int height) {
        Bitmap output=null;
        //output = Bitmap.createBitmap(bitmap.getWidth(),
        //bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        //output=bitmap;
        //output=bitmap.copy(bitmap.getConfig(), true);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        //final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final Rect rect = new Rect(0, 0, width,height);

        float r = 0;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        }else{
            r = bitmap.getWidth() / 2;
        }
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        //canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
        //      bitmap.getWidth() / 2, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public Bitmap imagenCircular(Drawable drawable, String path, int width, int height){
        //copiamos el drawable
        try {
            Bitmap originalBitmap,destinoBitmap;
            //convertimos el drawable en un Bitmap
            originalBitmap = ((BitmapDrawable) drawable).getBitmap();
            destinoBitmap=giraImagen(originalBitmap,path,width,height);
            Canvas canvas = new Canvas(destinoBitmap);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect;
            rect = new Rect(0, 0, width, height);
            final RectF rectF = new RectF(rect);
            final float roundPx = 360;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(originalBitmap, rect, rect, paint);

            return destinoBitmap;
        }catch(Exception ex){
            String msg="Error: "+ex;
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    //region Metodo Imagen Redondeada
    //metodo para redondear una imagen desde un path
    public RoundedBitmapDrawable imageRound(Drawable drawable,String path){
        int width=0;
        int height=0;
        //copiamos el drawable
        try {
            Bitmap originalBitmap;
            //convertimos el drawable en un Bitmap
            originalBitmap = ((BitmapDrawable) drawable).getBitmap();
            //obtenemos el ancho y altura de la imagen
            width=originalBitmap.getWidth();
            height=originalBitmap.getHeight();
            //verificamos si la imagen es cuadrada(ancho/altura)
            Bitmap giraBitmap=null;
            if (width > height) {
                originalBitmap = createBitmap(originalBitmap, 0, 0, height, height);
                giraBitmap=giraImagen(originalBitmap,path,height,height);

            } else {
                originalBitmap = createBitmap(originalBitmap, 0, 0, width, width);
                giraBitmap=giraImagen(originalBitmap,path,width,width);

            }
            //creamos el drawable redondeado
            RoundedBitmapDrawable roundedDrawable =
                    RoundedBitmapDrawableFactory.create(context.getResources(), giraBitmap);
            //asignamos el CornerRadius
            roundedDrawable.setCornerRadius(originalBitmap.getHeight());
            //retornamos la imagen redondeada(RoundBitmapDrawable)
            return roundedDrawable;
        }catch(Exception ex){
            String msg="Error: "+ex;
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    //metodo para redondear una imagen desde un Recurso(R.drawable.imagen)
    public RoundedBitmapDrawable imageRound(int drawable) {
        int width=0;
        int height=0;
        //extraemos el Resource(R.drawable.imagen) en un drawable
        try {
            Drawable originalDrawable = context.getResources().getDrawable(drawable);
            Bitmap originalBitmap;
            //convertimos el drawable en un Bitmap
            assert ((BitmapDrawable) originalDrawable) != null;
            originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
            //obtenemos el ancho y altura de la imagen
            width=originalBitmap.getWidth();
            height=originalBitmap.getHeight();
            //verificamos si la imagen es cuadrada
            if (width > height) {
                originalBitmap = createBitmap(originalBitmap, 0, 0, height, height);
            } else {
                originalBitmap = createBitmap(originalBitmap, 0, 0, width, width);
            }
            //creamos el drawable redondeado
            RoundedBitmapDrawable roundedDrawable =
                    RoundedBitmapDrawableFactory.create(context.getResources(), originalBitmap);
            //asignamos el CornerRadius
            roundedDrawable.setCornerRadius(originalBitmap.getHeight());
            //retornamos la imagen redondeada(RoundedBitmapDrawable)
            return roundedDrawable;
        } catch (Exception ex) {
            String msg = "Error: " + ex;
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    //endregion
    public static Bitmap getRoundedCornerBitmap( Drawable drawable, boolean square) {
        int width = 0;
        int height = 0;

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap() ;

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

        Bitmap output = createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = 360;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    public Bitmap getRoundedCornerBitmap(Uri imagenPath, boolean square) {
        int width = 0;
        int height = 0;
        InputStream is = null;
        //Activity context=getActivity();
        try {
            is = context.getContentResolver().openInputStream(imagenPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        Bitmap bitmap =null;
        bitmap = BitmapFactory.decodeStream(bis);

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

        Bitmap output = createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = 360;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public String getPathUri(Uri contenidoUri){
        String result;
        //Activity context=getActivity();
        Cursor cursor=context.getContentResolver().query(contenidoUri,null,null,null,null);
        if (cursor==null){
            result=contenidoUri.getPath();
        }else{
            cursor.moveToFirst();
            int indice=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result=cursor.getString(indice);
        }
        return result;
    }

}
