package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import android.content.Context;
import android.content.Intent;
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
import com.example.bof_group_28.utility.classes.DownloadImageTask;
import com.example.bof_group_28.utility.classes.FetchImage;

/**
 * This activity lets us edit the profile picture
 * We learned how to use Bitmaps here
 * https://youtu.be/oz3uGdi3f8Q
 */
public class EditProfilePictureActivity extends AppCompatActivity {

    ActivityEditProfilePictureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_picture);

        binding = ActivityEditProfilePictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new DownloadImageTask((ImageView) findViewById(R.id.editProfilePicture)).execute(user.getProfilePic());

        binding.saveProfilePicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String url = binding.URLTextField.getText().toString();
                databaseHandler.updateAndSaveUser(user.getName(), url);
                Log.d(TAG, "New PFP added: " + url);
                new DownloadImageTask((ImageView) findViewById(R.id.editProfilePicture)).execute(user.getProfilePic());
            }
        });
    }


    public void onDoneButtonClicked(View view) {
        Intent intent=new Intent();
        setResult(10,intent);
        finish();
    }
}