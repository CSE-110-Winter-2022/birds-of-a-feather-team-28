package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;
import static com.example.bof_group_28.viewAdapters.StudentViewAdapter.PFP;
import static com.example.bof_group_28.viewAdapters.StudentViewAdapter.SELECTED_STUDENT_COURSES;
import static com.example.bof_group_28.viewAdapters.StudentViewAdapter.SELECTED_STUDENT_NAME;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.R;
import com.example.bof_group_28.utility.classes.Converters;
import com.example.bof_group_28.utility.classes.DownloadImageTask;
import com.example.bof_group_28.viewAdapters.CourseViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StudentSelectedActivity extends AppCompatActivity {

    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager courseLayoutManager;
    private CourseViewAdapter courseViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_selected);

        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        TextView name = findViewById(R.id.studentName);
        name.setText(preferences.getString(SELECTED_STUDENT_NAME, "Invalid Name"));
        Set<String> courseSet = preferences.getStringSet(SELECTED_STUDENT_COURSES, new ArraySet<>());

        List<String> courses = new ArrayList<>(courseSet);

        courseRecyclerView = findViewById(R.id.sharedCoursesView);
        courseLayoutManager = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseViewAdapter = new CourseViewAdapter(courses);
        courseRecyclerView.setAdapter(courseViewAdapter);

        if (getIntent().hasExtra(PFP)) {
            new DownloadImageTask((ImageView) findViewById(R.id.largeProfilePicture)).execute(getIntent().getStringExtra(PFP));
        }
    }

    public void goBack(View view) {
        finish();
    }
}
