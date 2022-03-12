package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bof_group_28.R;
import com.example.bof_group_28.utility.enums.QuarterName;
import com.example.bof_group_28.utility.enums.SizeName;

import java.util.Arrays;
import java.util.UUID;

import model.db.CourseEntry;

public class AddClassActivity extends AppCompatActivity {
    //https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
    //Major resource for spinners
    Spinner quarterSpinner;
    Spinner sizeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("AddClassActivity", "AddClassActivity Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // Create dropdown for Quarters
        quarterSpinner = findViewById(R.id.quarter_dd);
        ArrayAdapter<String>quarterAdapter = new ArrayAdapter<>(AddClassActivity.this,
                android.R.layout.simple_spinner_item, QuarterName.types());
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);
        quarterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                TextView text = findViewById(R.id.quarter_txt);
                if (position != 0) {
                    text.setText(QuarterName.types()[position]);
                } else {
                    text.setText("");
                }
                Log.d("AddClassActivity", "Quarter Chosen");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Create dropdown for Size
        sizeSpinner = findViewById(R.id.size_dd);
        ArrayAdapter<String>sizeAdapter = new ArrayAdapter<>(AddClassActivity.this,
                android.R.layout.simple_spinner_item, SizeName.types());
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                TextView text = findViewById(R.id.size_txt);
                if (position != 0) {
                    text.setText(SizeName.types()[position]);
                } else {
                    text.setText("");
                }
                Log.d("AddClassActivity", "Size Chosen");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        loadPreviousEntry();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void savePreviousEntry(){
        //Save currently entered strings in views to sharedPrefs
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        TextView subjectView = findViewById(R.id.subject_entry);
        editor.putString("subject", subjectView.getText().toString());

        TextView courseNumView = findViewById(R.id.course_num_entry);
        editor.putString("courseNum", courseNumView.getText().toString());

        TextView yearView = findViewById(R.id.year_entry);
        editor.putString("year", yearView.getText().toString());

        TextView quarterView = findViewById(R.id.quarter_txt);
        editor.putString("quarter", quarterView.getText().toString());

        TextView sizeView = findViewById(R.id.size_txt);
        editor.putString("size", sizeView.getText().toString());
        editor.apply();

        Log.d(TAG, "Saved Add Class in preferences");

    }

    public void loadPreviousEntry() {
        Log.d(TAG, "Loading previous Add Class entry from Preferences");
        //Load previously entered strings in views
        SharedPreferences preferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);

        String subject = preferences.getString("subject","");
        TextView subjectView = findViewById(R.id.subject_entry);
        subjectView.setText(subject);

        String courseNum = preferences.getString("courseNum","");
        TextView courseNumView = findViewById(R.id.course_num_entry);
        courseNumView.setText(courseNum);

        String year = preferences.getString("year","");
        TextView yearView = findViewById(R.id.year_entry);
        yearView.setText(year);

        String quarter = preferences.getString("quarter","");
        quarterSpinner.setSelection(Arrays.asList(QuarterName.types()).indexOf(quarter));

        String size = preferences.getString("size","");
        sizeSpinner.setSelection(Arrays.asList(SizeName.types()).indexOf(size));

    }

    public void onDoneClicked(View view) {
        savePreviousEntry();
        finish();
    }

    public void onAddClicked(View view){
        savePreviousEntry();
        //Create new courseEntry object from views to add to database
        Log.d("AddClassActivity", "Add Course Clicked");
        TextView text;

        text = findViewById(R.id.subject_entry);
        String subjectTxt = text.getText().toString();

        text = findViewById(R.id.course_num_entry);
        String courseNumTxt = text.getText().toString();

        text = findViewById(R.id.year_entry);
        String yearTxt = text.getText().toString();

        text = findViewById(R.id.quarter_txt);
        String quarterTxt = text.getText().toString();

        text = findViewById(R.id.size_txt);
        String sizeTxt = text.getText().toString();

        UUID newCourseID = UUID.randomUUID();
        CourseEntry courseToAdd = new CourseEntry(newCourseID, user.getId(), yearTxt, quarterTxt, subjectTxt, courseNumTxt, sizeTxt);

        if (validateCourse(courseToAdd)) {
            Log.d("AddClassActivity", "Course Successfully Added");
            databaseHandler.insertCourse(courseToAdd);
            finish();
        }
        Log.d("AddClassActivity", "Course Not Added");
    }

    //FIXME Add more validation here if needed
    private boolean validateCourse(CourseEntry courseToAdd) {
        //Check that fields are not empty
        boolean flag = true;
        if (courseToAdd.year == null || courseToAdd.year == ""
            || courseToAdd.quarter == null || courseToAdd.quarter == ""
            || courseToAdd.subject == null || courseToAdd.subject == ""
            || courseToAdd.courseNum == null || courseToAdd.courseNum == ""
            || courseToAdd.size == null || courseToAdd.size == "") {

            Toast.makeText(this, "Make sure no fields are empty.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        //Check year is a 4-digit number
        if (courseToAdd.year.length() != 4 || !(courseToAdd.year.matches("[0-9]+"))) {
            Toast.makeText(this, "Make sure the year is a 4-digit number.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        //Check subject is a 3-4 letter code
        if (courseToAdd.subject.length() < 3 || courseToAdd.subject.length() > 4 || !(courseToAdd.subject.matches("^[a-zA-Z]+$"))) {
            Toast.makeText(this, "Make sure the subject is a 3 or 4 letter alphabetic code.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        //Check courseNum is a code between 2 to 4 characters
        if (courseToAdd.courseNum.length() < 2 || courseToAdd.courseNum.length() > 4) {
            Toast.makeText(this, "Make sure the Course Number is a 2 to 4 digit code.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        //Make sure duplicate courses can't be entered
        if (databaseHandler.getPersonsCourses(courseToAdd.personId).contains(courseToAdd)) {
            Toast.makeText(this, "Cannot add a duplicate course.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        if (flag) {
            Log.d("AddClassActivity", "Successfully Validated New Course");
        } else {
            Log.d("AddClassActivity", "New Course Not Validated");
        }

        return flag;
    }

}
