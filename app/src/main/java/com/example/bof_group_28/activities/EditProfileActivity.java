package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bof_group_28.R;
import com.example.bof_group_28.utility.classes.Converters;
import com.example.bof_group_28.utility.classes.DownloadImageTask;

import model.db.Person;


public class EditProfileActivity extends AppCompatActivity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        userName = user.getName();

        TextView nameField = findViewById(R.id.personsNameField);
        nameField.setText(userName);

        new DownloadImageTask((ImageView) findViewById(R.id.profilePictureProfile)).execute(user.getProfilePic());
    }

    public void onHomeButtonClicked(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onAddClassesButtonClicked(View view) {
        Intent intent = new Intent(this, AddClassActivity.class );
        startActivity(intent);
    }

    public void onManageClassesButtonClicked(View view) {
        Intent intent = new Intent(this, ManageClassesActivity.class );
        startActivity(intent);
    }

    public void onEditProfilePicButtonClicked(View view) {

        Intent intent = new Intent(this, EditProfilePictureActivity.class);
        startActivityForResult(intent, 10);
    }

    /**
     * Check if a user changes their name
     * @param view the view
     */
    public void saveNameClicked(View view) {
        //FIXME Add view to update profile pic?
        TextView nameField = findViewById(R.id.personsNameField);
        String name = nameField.getText().toString();

        Person personToUpdate = user.person;

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.equals(userName)) {
            Toast.makeText(this, "New name cannot be the same as the current name.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseHandler.updateAndSaveUser(name, personToUpdate.profilePic);
        Toast.makeText(this, "Name updated successfully", Toast.LENGTH_SHORT).show();
    }


    /**
     * Handle returning from the view session to update the view
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            Log.d(TAG, "Updating PFP view");

            new DownloadImageTask((ImageView) findViewById(R.id.profilePictureProfile)).execute(user.getProfilePic());
        }
    }
}