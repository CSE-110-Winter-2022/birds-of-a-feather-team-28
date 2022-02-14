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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bof_group_28.R;
import com.example.bof_group_28.databinding.ActivityEditProfilePictureBinding;
import com.example.bof_group_28.databinding.ActivityMainBinding;
import com.example.bof_group_28.utility.classes.Converters;

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

        binding = ActivityEditProfilePictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //FIXME SHOULD WE HAVE DEFAULT PFP ?

        byte[] pfp = user.getProfilePic();
        if(pfp != null){
            Bitmap pfpBitmap = Converters.byteArrToBitmap(pfp);
            ((ImageView) findViewById(R.id.profilePicture)).setImageBitmap(pfpBitmap);
        }

        binding.saveProfilePicture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                //FIXME CHECK VALID LINK OR CHECK IN FETCH IMAGE CLASS BELOW - THIS MIGHT ALR BE DONE
                //FIXME HAVE CONSISTENT PROFILE PICTURE SIZES
                String url = binding.URLTextField.getText().toString();
                FetchImage fetchImage = new FetchImage(url);
                fetchImage.start();
                Bitmap bitmap = fetchImage.getBitmap();
                if(bitmap != null){
                    byte[] byteArr = Converters.bitmapToByteArr(bitmap);
                    databaseHandler.updatePerson(1, "Jimmy", byteArr);
                    System.out.println("un poggers sheeesh");
                }

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