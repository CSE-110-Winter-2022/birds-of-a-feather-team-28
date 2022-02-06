package com.example.bof_group_28;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BirdsOfAFeatherActivity extends AppCompatActivity {


    private RecyclerView studentRecyclerView;
    private RecyclerView.LayoutManager studentLayoutManager;
    private StudentViewAdapter studentViewAdapter;
    private boolean bofStarted;

    private static final String BOF_START_BTN_TEXT = "START";
    private static final String BOF_STOP_BTN_TEXT = "STOP";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bofStarted = false;
    }

    public void onBofButtonClick(View view) {
        Button bofButton = findViewById(R.id.bofButton);
        if(bofStarted){
            BofStop();
            bofButton.setText(BOF_START_BTN_TEXT);
            //change button color to green
            this.bofStarted = false;
        }else{
            BofStart();
            bofButton.setText(BOF_STOP_BTN_TEXT);
            //change button color to red
            this.bofStarted = true;

        }

    }

    public void BofStart(){
        // update recycler view
        Person user = new DummyStudent("Bob");
        BirdsOfAFeatherHandleNearbyStudents handler = new BirdsOfAFeatherHandleNearbyStudents(user);

        List<Person> students = new ArrayList<>();
        students.add(new DummyStudent("Jimmy"));
        studentRecyclerView = findViewById(R.id.personRecyclerView);
        studentLayoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(studentLayoutManager);
        studentViewAdapter = new StudentViewAdapter(students);
        studentRecyclerView.setAdapter(studentViewAdapter);

    }

    public void BofStop(){
        studentViewAdapter.clear();
        studentRecyclerView.setAdapter(studentViewAdapter);
    }
}