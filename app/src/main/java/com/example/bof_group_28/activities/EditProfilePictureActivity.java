package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bof_group_28.R;
import com.example.bof_group_28.databinding.ActivityEditProfilePictureBinding;
import com.example.bof_group_28.utility.classes.Converters;
import com.example.bof_group_28.utility.classes.FetchImage;

/**
 * This activity lets us edit the profile picture
 * We learned how to use Bitmaps here
 * https://youtu.be/oz3uGdi3f8Q
 */
public class EditProfilePictureActivity extends AppCompatActivity {

    ActivityEditProfilePictureBinding binding;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_picture);

        binding = ActivityEditProfilePictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        byte[] pfp = user.getProfilePic();
        if(pfp != null){
            Bitmap pfpBitmap = Converters.byteArrToBitmap(pfp);
            ((ImageView) findViewById(R.id.editProfilePicture)).setImageBitmap(pfpBitmap);
        }

        Context c = this;

        binding.saveProfilePicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //FIXME CHECK VALID LINK OR CHECK IN FETCH IMAGE CLASS BELOW - THIS MIGHT ALR BE DONE
                //FIXME HAVE CONSISTENT PROFILE PICTURE SIZES
                String url = binding.URLTextField.getText().toString();
                FetchImage fetchImage = new FetchImage(url, c);
                fetchImage.start();

                handler.post (new Runnable() {
                   @Override
                   public void run() {
                       if (fetchImage.isAlive()) {
                           Log.v(TAG, "Still processing fetch image");
                           //FIXME: not elegant way to check if done
                           handler.postDelayed(this, 1000);
                       } else {
                           Bitmap bitmap = fetchImage.getBitmap();
                           if(bitmap != null){
                               //FIXME: fix if file is too large for db
                               Log.v(TAG, "Converted Bitmap to Byte Array");
                               byte[] byteArr = Converters.bitmapToByteArr(bitmap);
                               databaseHandler.updateAndSaveUser(user.getName(), byteArr);
                               binding.editProfilePicture.setImageBitmap(bitmap);
                           } else {
                               Log.e(TAG, "Bitmap is null!");
                               Toast.makeText(c, "Invalid image URL", Toast.LENGTH_SHORT).show();
                           }
                       }
                       //FIXME: update pfp from menu page once returning
                   }
                });
            }
        });
    }


    public void onDoneButtonClicked(View view) {
        finish();
    }
}