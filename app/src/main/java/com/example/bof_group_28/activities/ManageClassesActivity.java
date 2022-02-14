package com.example.bof_group_28.activities;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bof_group_28.R;
import com.example.bof_group_28.viewAdapters.ManageCourseViewAdapter;

import model.db.AppDatabase;
import model.db.CourseEntry;

import java.util.ArrayList;

public class ManageClassesActivity extends AppCompatActivity{
    private RecyclerView courseRV;
    private AppDatabase db;
    // Arraylist for storing data
    private ArrayList<CourseEntry> courseEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_classes_view);

        TextView nameView = findViewById(R.id.manageCoursesStudentName);
        nameView.setText(user.getName());

        courseRV = findViewById(R.id.courseListView);

        //FIXME refactor to singleton person
        db = AppDatabase.singleton(this);

        //query list of courses for user form database
        courseEntries = (ArrayList<CourseEntry>) db.personWithCoursesDAO().get(1).getCourses();

        // initialize adapter and pass list of courses
        ManageCourseViewAdapter courseAdapter = new ManageCourseViewAdapter(this, courseEntries);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //setting layout manager and adapter to recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);
    }

    public void onClickManageCourseBackButton(View view) {
        finish();
    }
}
