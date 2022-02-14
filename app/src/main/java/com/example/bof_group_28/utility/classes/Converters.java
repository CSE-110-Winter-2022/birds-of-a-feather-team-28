package com.example.bof_group_28.utility.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class Converters {

    public static byte[] bitmapToByteArr(Bitmap bitmap){
        OutputStream oStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
        return ((ByteArrayOutputStream) oStream).toByteArray();
    }

    public static Bitmap byteArrToBitmap(byte[] byteArr){
        return BitmapFactory.decodeByteArray(byteArr,0, byteArr.length);
    }
}
