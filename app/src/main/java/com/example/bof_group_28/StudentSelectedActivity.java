package com.example.bof_group_28;

import static com.example.bof_group_28.BirdsOfAFeatherActivity.PREF_NAME;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        name.setText(preferences.getString("name", "Invalid Name"));
        Set<String> courseSet = preferences.getStringSet("courses", new ArraySet<>());

        List<String> courses = new ArrayList<>();
        courses.addAll(courseSet);
        //TODO: Unfake
        courses.add("CSE 101, WI 22");
        courses.add("CSE 101, WI 25");

        courseRecyclerView = findViewById(R.id.sharedCoursesView);
        courseLayoutManager = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);
        courseViewAdapter = new CourseViewAdapter(courses);
        courseRecyclerView.setAdapter(courseViewAdapter);
    }

    public void goBack(View view) {
        finish();
    }
}
