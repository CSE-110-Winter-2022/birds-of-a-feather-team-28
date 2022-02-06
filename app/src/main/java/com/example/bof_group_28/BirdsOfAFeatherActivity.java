package com.example.bof_group_28;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class BirdsOfAFeatherActivity extends AppCompatActivity {

    private RecyclerView studentRecyclerView;
    private RecyclerView.LayoutManager studentLayoutManager;
    private StudentViewAdapter studentViewAdapter;
    private boolean bofStarted;

    private BirdsOfAFeatherHandleNearbyStudents handler;

    private static final String BOF_START_BTN_TEXT = "START";
    private static final String BOF_STOP_BTN_TEXT = "STOP";
    private static final int BOF_START_BTN_COLOR = Color.rgb(76, 175, 80);
    private static final int BOF_STOP_BTN_COLOR = Color.RED;

    public static final String PREF_NAME = "preferences";

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
            setButtonColor(BOF_START_BTN_COLOR, bofButton);
            this.bofStarted = false;
        }else{
            BofStart();
            bofButton.setText(BOF_STOP_BTN_TEXT);
            setButtonColor(BOF_STOP_BTN_COLOR, bofButton);
            this.bofStarted = true;
        }
    }

    public void setButtonColor(int color, Button button) {
        Drawable buttonDrawable = button.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, color);
        button.setBackground(buttonDrawable);
    }

    public void BofStart(){
        // update recycler view
        Person user = new DummyStudent("Bob");
        handler = new BirdsOfAFeatherHandleNearbyStudents(user);

        List<Person> students = new ArrayList<>();
        students.add(new DummyStudent("Jimmy"));
        students.add(new DummyStudent("Doug"));
        students.add(new DummyStudent("Jimmy"));



        studentRecyclerView = findViewById(R.id.personRecyclerView);
        studentLayoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(studentLayoutManager);
        studentViewAdapter = new StudentViewAdapter(students, handler);
        studentRecyclerView.setAdapter(studentViewAdapter);

    }

    public void BofStop(){
        handler.stop();
        studentViewAdapter.clear();
        studentRecyclerView.setAdapter(studentViewAdapter);
    }
}