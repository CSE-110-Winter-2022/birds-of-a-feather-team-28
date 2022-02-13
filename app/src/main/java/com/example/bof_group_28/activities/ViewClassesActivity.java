package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.SELF_COURSES;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.USER_NAME;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.R;
import com.example.bof_group_28.viewAdapters.CourseViewAdapter;
import com.example.bof_group_28.viewAdapters.StudentViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import model.db.AppDatabase;

public class ViewClassesActivity extends AppCompatActivity {

    private RecyclerView classRecyclerView;
    private RecyclerView.LayoutManager classLayoutManager;
    private CourseViewAdapter classViewAdapter;
    private AppDatabase db;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_view);
        db = AppDatabase.singleton(this);
        userName = db.personWithCoursesDAO().get(1).getName();

        classRecyclerView = findViewById(R.id.viewCoursesCourseView);
        classLayoutManager = new LinearLayoutManager(this);
        classRecyclerView.setLayoutManager(classLayoutManager);

        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        //classViewAdapter = new CourseViewAdapter(new ArrayList<>(preferences.getStringSet(SELF_COURSES, new ArraySet<>())));
        classViewAdapter = new CourseViewAdapter(new ArrayList<>(preferences.getStringSet(SELF_COURSES, new ArraySet<>())));
        classRecyclerView.setAdapter(classViewAdapter);

        TextView studentName = findViewById(R.id.viewCoursesStudentName);
        studentName.setText(userName);
    }

    public void onClickViewCourseBackButton(View view) {
        finish();
    }
}
