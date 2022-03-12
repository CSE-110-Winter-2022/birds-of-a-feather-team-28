package com.example.bof_group_28.utility.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.bof_group_28.activities.BirdsOfAFeatherActivity;

import java.io.InputStream;
//https://stackoverflow.com/questions/5776851/load-image-from-url
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(BirdsOfAFeatherActivity.TAG, e.getMessage());
            Log.e(BirdsOfAFeatherActivity.TAG, "FAILED TO LOAD PROFILE PICTURE");
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
