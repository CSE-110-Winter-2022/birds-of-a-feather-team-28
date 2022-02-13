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

public class EditProfileActivity extends AppCompatActivity {

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        TextView nameField = findViewById(R.id.personsNameField);
        this.name = preferences.getString(USER_NAME, "Invalid Name");
        nameField.setText(name);

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

    public void onEditClassesButtonClicked(View view) {
        //implement
    }

    public void onRemoveClassesButtonClicked(View view) {
        //implement
    }

    public void saveNameClicked(View view) {

        TextView nameField = findViewById(R.id.personsNameField);
        if (nameField.getText().toString().isEmpty()) {
            Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nameField.getText().toString().equals(name)) {
            Toast.makeText(this, "You can't change your name to the same name.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra(USER_NAME, nameField.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}