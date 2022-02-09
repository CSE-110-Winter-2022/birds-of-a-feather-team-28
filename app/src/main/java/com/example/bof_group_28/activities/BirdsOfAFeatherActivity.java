package com.example.bof_group_28.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bof_group_28.utility.classes.DummyCourse;
import com.example.bof_group_28.utility.classes.DummyStudentFinder;
import com.example.bof_group_28.utility.classes.NearbyStudentsHandler;
import com.example.bof_group_28.utility.classes.DummyStudent;
import com.example.bof_group_28.utility.interfaces.Person;
import com.example.bof_group_28.R;
import com.example.bof_group_28.viewAdapters.StudentViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BirdsOfAFeatherActivity extends AppCompatActivity {

    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;

    private RecyclerView studentRecyclerView;
    private RecyclerView.LayoutManager studentLayoutManager;
    private StudentViewAdapter studentViewAdapter;
    private boolean bofStarted;

    private Person user;

    private NearbyStudentsHandler handler;

    private static final String TAG = "BoF: ";
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
        ((DummyStudent) user).addCourse(new DummyCourse("2020","fall", "CSE", "11"));
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
        List<Person> fakeNearby = new ArrayList<>();
        Person bob = new DummyStudent("Bob");
        Person lily = new DummyStudent("Lily");
        ((DummyStudent) bob).addCourse(new DummyCourse("2020","fall", "CSE", "11"));
        ((DummyStudent) lily).addCourse(new DummyCourse("2020","fall", "CSE", "11"));
        fakeNearby.add(bob);
        fakeNearby.add(lily);
        handler = new NearbyStudentsHandler(user, new DummyStudentFinder(fakeNearby));

        studentRecyclerView = findViewById(R.id.personRecyclerView);
        studentLayoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(studentLayoutManager);
        studentViewAdapter = new StudentViewAdapter(handler.getStudentsList(), handler);
        studentRecyclerView.setAdapter(studentViewAdapter);

        Handler runHandler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                if (bofStarted) {
                    handler.refreshStudentClassMap();

                    Log.v(TAG, "Refreshed student class map.");

                    studentViewAdapter.clear();
                    studentViewAdapter = new StudentViewAdapter(handler.getStudentsList(), handler);
                    studentRecyclerView.setAdapter(studentViewAdapter);

                    Log.v(TAG, "Updated nearby students view.");
                    runHandler.postDelayed(this, 10000);
                }
            }
        };

        runHandler.postDelayed(r, 10000);

        /*this.future = backgroundThreadExecutor.submit(() -> {
            do {
                handler.refreshStudentClassMap();

                Log.v(TAG, "Refreshed student class map.");

                studentViewAdapter.clear();
                studentViewAdapter = new StudentViewAdapter(handler.getStudentsList(), handler);
                studentRecyclerView.setAdapter(studentViewAdapter);

                Log.v(TAG, "Updated nearby students view.");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (bofStarted);
            return null;
        });*/
    }

    public void stopBirdsOfFeather(){
        handler.stop();
        //this.future.cancel(true);

        Log.v(TAG, "Stopped Birds of a Feather.");
    }

    public void onClear(View view) {
        if (handler != null) {
            handler.clear();
            Log.v(TAG, "Cleared Birds of a Feather handler.");
        }

        if (studentViewAdapter != null) {
            studentViewAdapter.clear();
            studentRecyclerView.setAdapter(studentViewAdapter);
            Log.v(TAG, "Cleared Birds of a Feather student view.");
        }
    }
}