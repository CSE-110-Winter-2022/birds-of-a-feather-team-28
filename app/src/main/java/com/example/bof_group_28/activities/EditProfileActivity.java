package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;
import static com.example.bof_group_28.viewAdapters.StudentViewAdapter.SELECTED_STUDENT_COURSES;
import static com.example.bof_group_28.viewAdapters.StudentViewAdapter.SELECTED_STUDENT_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bof_group_28.R;
import com.example.bof_group_28.utility.Utilities;
import com.example.bof_group_28.utility.services.NearbyStudentsService;

import java.util.Set;

import model.db.AppDatabase;
import model.db.Person;
import model.db.PersonWithCourses;


public class EditProfileActivity extends AppCompatActivity {

    private AppDatabase db;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        db = AppDatabase.singleton(this);
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
        //FIXME implement or remove
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

        db.personWithCoursesDAO().update(personToUpdate.personId, name, personToUpdate.profilePic);
        Toast.makeText(this, "Name updated successfully", Toast.LENGTH_SHORT).show();
    }
}