package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

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

public class ViewClassesActivity extends AppCompatActivity {

    private RecyclerView classRecyclerView;
    private RecyclerView.LayoutManager classLayoutManager;
    private CourseViewAdapter classViewAdapter;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_view);
        userName = user.getName();

        classRecyclerView = findViewById(R.id.viewCoursesCourseView);
        classLayoutManager = new LinearLayoutManager(this);
        classRecyclerView.setLayoutManager(classLayoutManager);

        classViewAdapter = new CourseViewAdapter(new ArrayList<>(user.getCoursesString()));
        classRecyclerView.setAdapter(classViewAdapter);

        TextView studentName = findViewById(R.id.viewCoursesStudentName);
        studentName.setText(userName);
    }

    public void onClickViewCourseBackButton(View view) {
        finish();
    }
}
