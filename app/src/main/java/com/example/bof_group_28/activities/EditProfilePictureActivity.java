package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.bof_group_28.R;
import com.example.bof_group_28.databinding.ActivityEditProfilePictureBinding;
import com.example.bof_group_28.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class EditProfilePictureActivity extends AppCompatActivity {

    ActivityEditProfilePictureBinding binding;
    Handler handler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_picture);
        //FIXME place current pfp into pfpview






        binding = ActivityEditProfilePictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveProfilePicture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                //FIXME CHECK VALID LINK OR CHECK IN FETCH IMAGE CLASS BELOW

                String url = binding.URLTextField.getText().toString();
                FetchImage fetchImage = new FetchImage(url);
                fetchImage.start();
                Bitmap bitmap = fetchImage.getBitmap();

                OutputStream oStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
                byte[] byteArr = ((ByteArrayOutputStream) oStream).toByteArray();
                databaseHandler.updatePerson(user.getId(), user.getName(), byteArr);

            }

        });

    }



    public void onDoneButtonClicked(View view) {
        finish();
    }

    class FetchImage extends Thread{
        String URL;
        Bitmap bitmap;

        FetchImage(String URL){
            this.URL = URL;
        }

        public Bitmap getBitmap(){
            return this.bitmap;
        }

        @Override
        public void run(){

            handler.post(new Runnable(){
                @Override
                public void run(){
                    progressDialog = new ProgressDialog(EditProfilePictureActivity.this);
                    progressDialog.setMessage("Getting your picture");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
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
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    binding.profilePicture.setImageBitmap(bitmap);
                }
            });

        }
    }
}