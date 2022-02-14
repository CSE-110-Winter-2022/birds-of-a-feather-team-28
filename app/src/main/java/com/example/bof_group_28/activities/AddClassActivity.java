package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import model.db.AppDatabase;
import model.db.CourseEntry;

public class AddClassActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner spinner;
    //Acceptable quarters, empty string is default but validation prevents database insertion
    private static final String[] quarterNames =
            {"","FA","WI", "SP", "SS1", "SS2", "SSS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("AddClassActivity", "AddClassActivity Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        spinner = (Spinner)findViewById(R.id.quarter_dd);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(AddClassActivity.this,
                android.R.layout.simple_spinner_item,quarterNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        loadPreviousEntry();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        savePreviousEntry();
    }

    public void savePreviousEntry(){
        //Save currently entered strings in views to sharedPrefs
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        TextView subjectView = findViewById(R.id.subject_entry);
        editor.putString("subject", subjectView.getText().toString());
        editor.apply();

        TextView courseNumView = findViewById(R.id.course_num_entry);
        editor.putString("courseNum", courseNumView.getText().toString());
        editor.apply();

        TextView yearView = findViewById(R.id.year_entry);
        editor.putString("year", yearView.getText().toString());
        editor.apply();

        TextView quarterView = findViewById(R.id.quarter_txt);
        editor.putString("quarter", quarterView.getText().toString());
        editor.apply();

    }

    public void loadPreviousEntry() {
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
        TextView quarterView = findViewById(R.id.quarter_txt);
        quarterView.setText(quarter);


    }


    public void onDoneClicked(View view) {
        finish();
    }

    public void onAddClicked(View view){
        //Create new courseEntry object from views to add to database
        Log.v("AddClassActivity", "Add Course Clicked");
        TextView text;

        text = findViewById(R.id.subject_entry);
        String subjectTxt = text.getText().toString();

        text = findViewById(R.id.course_num_entry);
        String courseNumTxt = text.getText().toString();

        text = findViewById(R.id.year_entry);
        String yearTxt = text.getText().toString();

        text = findViewById(R.id.quarter_txt);
        String quarterTxt = text.getText().toString();

        int newCourseID = databaseHandler.db.courseEntryDAO().maxId()+1;
        CourseEntry courseToAdd = new CourseEntry(newCourseID, user.getId(), yearTxt, quarterTxt, subjectTxt, courseNumTxt);

        if (validateCourse(courseToAdd)) {
            Log.v("AddClassActivity", "Course Successfully Added");
            databaseHandler.insertCourse(courseToAdd);
            finish();
        }
        Log.v("AddClassActivity", "Course Not Added");
    }

    //FIXME Add more validation here if needed
    private boolean validateCourse(CourseEntry courseToAdd) {
        //Check that fields are not empty
        boolean flag = true;
        if (courseToAdd.year == null || courseToAdd.year == ""
            || courseToAdd.quarter == null || courseToAdd.quarter == ""
            || courseToAdd.subject == null || courseToAdd.subject == ""
            || courseToAdd.courseNum == null || courseToAdd.courseNum == "") {

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
        //Check courseNum is a code beteween 2 to 4 characters
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
            Log.v("AddClassActivity", "Successfully Validated New Course");
        } else {
            Log.v("AddClassActivity", "New Course Not Validated");
        }

        return flag;
    }

    @Override
    //https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        TextView text = findViewById(R.id.quarter_txt);
        Log.v("AddClassActivity", "Quarter Chosen");
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                //text.setText("FA");
                break;
            case 1:
                // Whatever you want to happen when the first item gets selected
                text.setText("FA");
                break;
            case 2:
                // Whatever you want to happen when the second item gets selected
                text.setText("WI");
                break;
            case 3:
                // Whatever you want to happen when the third item gets selected
                text.setText("SP");
                break;
            case 4:
                // Whatever you want to happen when the second item gets selected
                text.setText("SS1");
                break;
            case 5:
                // Whatever you want to happen when the second item gets selected
                text.setText("SS2");
                break;
            case 6:
                // Whatever you want to happen when the second item gets selected
                text.setText("SSS");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

}
