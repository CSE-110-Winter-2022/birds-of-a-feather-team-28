package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.CHANGED_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
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

import com.example.bof_group_28.R;
import com.example.bof_group_28.utility.Utilities;

import java.util.Set;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        TextView nameField = findViewById(R.id.personsNameField);
        nameField.setText(preferences.getString(USER_NAME, "Invalid Name"));
    }

    public void onHomeButtonClicked(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onViewClassesButtonClicked(View view) {
        //implement
    }

    public void onAddClassesButtonClicked(View view) {
        Intent intent = new Intent(this,AddClassActivity.class );
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
            Utilities.showAlert(this, "Name cannot be empty!");
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra(USER_NAME, nameField.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}