package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.example.bof_group_28.activities.EditProfilePictureActivity;
import com.example.bof_group_28.databinding.ActivityEditProfilePictureBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Class used to fetch bitmap from url
 * We learned how to use Bitmaps here
 * https://youtu.be/oz3uGdi3f8Q
 */
public class FetchImage extends Thread{

    Handler handler = new Handler();
    ProgressDialog progressDialog;

    String URL;
    Bitmap bitmap;

    Context context;

    /**
     * Override constructor for no context to update to
     * @param URL
     */
    public FetchImage(String URL){
        this.URL = URL;
    }

    /**
     * Default constructor
     * @param URL the url of image
     * @param context context to display progress dialog
     */
    public FetchImage(String URL, Context context){
        this.URL = URL;
        this.context = context;
    }

    public Bitmap getBitmap(){
        return this.bitmap;
    }

    @Override
    public void run() {
        handler.post(new Runnable(){
            @Override
            public void run(){
                Log.v(TAG, "Processing image conversion");
                if (context != null) {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading profile picture");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }
        });

        InputStream inputStream = null;

        try{
            inputStream = new URL(URL).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch(IOException e){
            e.printStackTrace();
        }

        handler.post(new Runnable(){
            @Override
            public void run(){
                if(context != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });

    }
}