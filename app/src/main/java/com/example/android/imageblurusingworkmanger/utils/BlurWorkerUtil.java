package com.example.android.imageblurusingworkmanger.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;

public class BlurWorkerUtil{

    private static final float BITMAP_SCALE = 0.4f;
    private static final float BITMAP_RADIUS = 20.5f;

    public static Bitmap blurImage(Bitmap image, Context context){
        int width = Math.round(image.getWidth() *BITMAP_SCALE);
        int height = Math.round(image.getHeight() *BITMAP_SCALE);

        Bitmap input = Bitmap.createScaledBitmap(image,width,height,false);
        Bitmap output = Bitmap.createBitmap(input);
        RenderScript renderScript = RenderScript.create(context);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8(renderScript));
        Allocation allocation = Allocation.createFromBitmap(renderScript,input);
        Allocation allocation1 = Allocation.createFromBitmap(renderScript,output);
        intrinsicBlur.setRadius(BITMAP_RADIUS);
        intrinsicBlur.setInput(allocation);
        intrinsicBlur.forEach(allocation1);
        allocation1.copyTo(output);
        return output;
    }

    public static Uri writeBitmapToFile(Context context,Bitmap bitmapImage){
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String imageName = "snap_"+timeStamp+".jpg";
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File file = contextWrapper.getDir("images",Context.MODE_PRIVATE);
        file = new File(file,"snap_"+imageName+".jpg");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();

        }catch (IOException e){
            e.fillInStackTrace();
        }

        Uri imageUri = Uri.parse(file.getAbsolutePath());
        return imageUri;

    }


}
