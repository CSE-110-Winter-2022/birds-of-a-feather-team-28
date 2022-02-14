package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bof_group_28.R;

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
        startActivity(intent);
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

        databaseHandler.updatePerson(personToUpdate.personId, name, personToUpdate.profilePic);
        Toast.makeText(this, "Name updated successfully", Toast.LENGTH_SHORT).show();
    }
}