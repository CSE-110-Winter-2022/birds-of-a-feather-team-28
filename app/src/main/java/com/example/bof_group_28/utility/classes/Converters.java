package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * This converter class lets us convert from Bitmap to Byte Array
 * https://www.youtube.com/watch?v=adGU0A80EJ0&ab_channel=Stevdza-San
 */
public class Converters {

    public static byte[] bitmapToByteArr(Bitmap bitmap){
        OutputStream oStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, oStream);
        Log.v(TAG, "Converted Bitmap to Byte Array");
        return ((ByteArrayOutputStream) oStream).toByteArray();
    }

    public static Bitmap byteArrToBitmap(byte[] byteArr){
        Log.v(TAG, "Converted Byte Array to Bitmap");
        return BitmapFactory.decodeByteArray(byteArr,0, byteArr.length);
    }
}
