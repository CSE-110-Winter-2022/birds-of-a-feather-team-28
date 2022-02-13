package com.example.bof_group_28.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bof_group_28.utility.Utilities;
import com.example.bof_group_28.utility.classes.DummyCourse;
import com.example.bof_group_28.utility.classes.DummyStudentFinder;
import com.example.bof_group_28.utility.classes.NearbyStudentsHandler;
import com.example.bof_group_28.utility.classes.DummyStudent;
import com.example.bof_group_28.utility.interfaces.CourseEntry;
import com.example.bof_group_28.utility.interfaces.Person;
import com.example.bof_group_28.R;
import com.example.bof_group_28.utility.services.NearbyStudentsService;
import com.example.bof_group_28.viewAdapters.StudentViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import model.db.AppDatabase;
import model.db.PersonWithCourses;

public class BirdsOfAFeatherActivity extends AppCompatActivity {

    private RecyclerView studentRecyclerView;
    private RecyclerView.LayoutManager studentLayoutManager;
    private StudentViewAdapter studentViewAdapter;
    private boolean bofStarted;

    private Person user;

    private NearbyStudentsHandler handler;

    public static final String TAG = "BoF: ";
    private static final String BOF_START_BTN_TEXT = "START";
    private static final String BOF_STOP_BTN_TEXT = "STOP";
    private static final int BOF_START_BTN_COLOR = Color.rgb(76, 175, 80);
    private static final int BOF_STOP_BTN_COLOR = Color.RED;
    public static final String USER_NAME = "name";
    public static final String CHANGED_NAME = "nameChanged";
    public static final String SELF_COURSES = "ownCourses";
    public static final int EDIT_PROFILE_CODE = 7;

    private static final int UPDATE_TIME = 10000;

    public static final String PREF_NAME = "preferences";
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bofStarted = false;

        user = new DummyStudent("Jimmy");
        ((DummyStudent) user).addCourse(new DummyCourse("2020","fall", "CSE", "11"));

        db = AppDatabase.singleton(getApplicationContext());
        clearNonUserEntries();
        firstTimeUserInitialize();
    }
    private void firstTimeUserInitialize() {

        if (db.personWithCoursesDAO().maxId() < 1) {
            //FIXME Replace with name from googleAccount
            model.db.Person testStudent = new model.db.Person(1,"Jimmy", null);
            db.personWithCoursesDAO().insert(testStudent);
        }
    }

    private void clearNonUserEntries() {
        db.personWithCoursesDAO().deleteNonUserCourses();
        db.personWithCoursesDAO().deleteNonUserPersons();
    }

    public void onBofButtonClick(View view) {
        Button bofButton = findViewById(R.id.bofButton);
        if(bofStarted){
            stopBirdsOfFeather();
            bofButton.setText(BOF_START_BTN_TEXT);
            setButtonColor(BOF_START_BTN_COLOR, bofButton);
            this.bofStarted = false;

            Intent intent = new Intent(BirdsOfAFeatherActivity.this, NearbyStudentsService.class);
            stopService(intent);
        }else{
            startBirdsOfFeather(user);
            bofButton.setText(BOF_STOP_BTN_TEXT);
            setButtonColor(BOF_STOP_BTN_COLOR, bofButton);
            this.bofStarted = true;

            Intent intent = new Intent(BirdsOfAFeatherActivity.this, NearbyStudentsService.class);
            startService(intent);
        }
    }

    public void setButtonColor(int color, Button button) {
        Drawable buttonDrawable = button.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, color);
        button.setBackground(buttonDrawable);
    }

    public void startBirdsOfFeather(Person user) {

        //TODO: Have students come into range for this "fake startup", so you can demo multiple things.
        //ie. You clear then somebody new shows up, but others dont. Or of course realtime you see somebody show up.
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
                    runHandler.postDelayed(this, UPDATE_TIME);
                }
            }
        };

        runHandler.postDelayed(r, UPDATE_TIME);

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

    public void onEditProfileButtonClicked(View view) {
        //FIXME Remove once view classes is switched to db
        SharedPreferences preferences = view.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> courses = new ArraySet<>();
        for (CourseEntry course : user.getCourses()) {
            courses.add(course.toString());
        }
        editor.putString(USER_NAME, user.getName());
        editor.putStringSet(SELF_COURSES, courses);

        editor.apply();

        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivityForResult(intent, EDIT_PROFILE_CODE);
        //startActivity(intent);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == EDIT_PROFILE_CODE) {
            user.setName(data.getStringExtra(USER_NAME));
            Toast.makeText(this, "Successfully changed name to " + data.getStringExtra(USER_NAME), Toast.LENGTH_SHORT).show();
        }
    }
}