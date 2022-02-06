package com.example.bof_group_28;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BirdsOfAFeatherActivity extends AppCompatActivity {


    private RecyclerView studentRecyclerView;
    private RecyclerView.LayoutManager studentLayoutManager;
    private StudentViewAdapter studentViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBofButtonClick(View view) {
        // update recycler view
        Person user = new DummyStudent("Bob");
        BirdsOfAFeatherHandleNearbyStudents handler = new BirdsOfAFeatherHandleNearbyStudents(user);

        List<Person> students = new ArrayList<>();
        students.add(new DummyStudent("Joe"));

        studentRecyclerView = findViewById(R.id.personRecyclerView);
        studentLayoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(studentLayoutManager);
        studentViewAdapter = new StudentViewAdapter(students);
        studentRecyclerView.setAdapter(studentViewAdapter);
    }
}