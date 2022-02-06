package com.example.bof_group_28.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import com.example.bof_group_28.utility.BirdsOfAFeatherHandleNearbyStudents;
import com.example.bof_group_28.utility.DummyStudent;
import com.example.bof_group_28.utility.Person;
import com.example.bof_group_28.R;
import com.example.bof_group_28.viewAdapters.StudentViewAdapter;

public class BirdsOfAFeatherActivity extends AppCompatActivity {

    private RecyclerView studentRecyclerView;
    private RecyclerView.LayoutManager studentLayoutManager;
    private StudentViewAdapter studentViewAdapter;
    private boolean bofStarted;

    private Person user;

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

        user = new DummyStudent("Jimmy");
    }

    public void onBofButtonClick(View view) {
        Button bofButton = findViewById(R.id.bofButton);
        if(bofStarted){
            stopBirdsOfFeather();
            bofButton.setText(BOF_START_BTN_TEXT);
            setButtonColor(BOF_START_BTN_COLOR, bofButton);
            this.bofStarted = false;
        }else{
            startBirdsOfFeather(user);
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

    public void startBirdsOfFeather(Person user) {
        handler = new BirdsOfAFeatherHandleNearbyStudents(user);

        studentRecyclerView = findViewById(R.id.personRecyclerView);
        studentLayoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(studentLayoutManager);
        studentViewAdapter = new StudentViewAdapter(handler.getStudentsList(), handler);
        studentRecyclerView.setAdapter(studentViewAdapter);

    }

    public void stopBirdsOfFeather(){
        handler.stop();
        studentViewAdapter.clear();
        studentRecyclerView.setAdapter(studentViewAdapter);
    }
}