package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.CHANGED_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.SELF_COURSES;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.USER_NAME;
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


public class EditProfileActivity extends AppCompatActivity {

    private AppDatabase db;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        db = AppDatabase.singleton(this);
        userName = db.personWithCoursesDAO().get(1).getName();

        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        TextView nameField = findViewById(R.id.personsNameField);
        nameField.setText(userName);

    }

    public void onHomeButtonClicked(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onViewClassesButtonClicked(View view) {
        Intent intent = new Intent(this, ViewClassesActivity.class );
        startActivity(intent);
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

    public void saveNameClicked(View view) {
        //FIXME Add view to update profile pic?
        TextView nameField = findViewById(R.id.personsNameField);
        Person personToUpdate = new Person(1, nameField.getText().toString(), null);

        if (personToUpdate.name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (personToUpdate.name.equals(userName)) {
            Toast.makeText(this, "New name cannot be the same as the current name.", Toast.LENGTH_SHORT).show();
            return;
        }
        db.personWithCoursesDAO().update(1,personToUpdate.name, personToUpdate.profilePic);

        //Intent returnIntent = new Intent();
        //returnIntent.putExtra(USER_NAME, nameField.getText().toString());
        //setResult(Activity.RESULT_OK, returnIntent);
        //finish();
    }
}