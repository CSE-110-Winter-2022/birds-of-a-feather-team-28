package com.example.bof_group_28.activities;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import com.example.bof_group_28.utility.classes.Converters;
import com.example.bof_group_28.utility.classes.DatabaseHandler;
import com.example.bof_group_28.utility.classes.DummyStudentFinder;
import com.example.bof_group_28.utility.classes.FetchImage;
import com.example.bof_group_28.utility.classes.NearbyStudentsHandler;
import com.example.bof_group_28.R;
import com.example.bof_group_28.utility.services.NearbyStudentsService;
import com.example.bof_group_28.viewAdapters.StudentViewAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.ArrayList;
import java.util.List;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;

/**
 * This class is the main Birds of a Feather Activity that opens on app startup
 */
public class BirdsOfAFeatherActivity extends AppCompatActivity {

    // User of the App
    public static PersonWithCourses user;

    // Student View Management
    private RecyclerView studentRecyclerView;
    private RecyclerView.LayoutManager studentLayoutManager;
    private StudentViewAdapter studentViewAdapter;

    // Instance Variables
    private boolean bofStarted;
    private NearbyStudentsHandler handler;
    private Intent nearbyStudentService;

    // Constants
    public static final String PREF_NAME = "preferences";
    public static final String TAG = "BoF: ";
    private static final String BOF_START_BTN_TEXT = "START";
    private static final String BOF_STOP_BTN_TEXT = "STOP";
    private static final int BOF_START_BTN_COLOR = Color.rgb(76, 175, 80);
    private static final int BOF_STOP_BTN_COLOR = Color.RED;
    private static final int UPDATE_TIME = 5000;

    // Database
    public static DatabaseHandler databaseHandler;
    private AppDatabase db;

    /**
     * Create the Birds of a Feather Activity on App Startup
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.singleton(getApplicationContext());
        //db.clearAllTables();

        bofStarted = false;
        databaseHandler = new DatabaseHandler(db, getApplicationContext());

        if (GoogleSignIn.getLastSignedInAccount(this) == null) {
            Log.v(TAG, "User is not logged in through Google.");
            Intent googleIntent = new Intent(this, GoogleSignInActivity.class);
            startActivity(googleIntent);
        } else {
            Log.v(TAG, "User is already logged in!");
        }
    }

    /**
     * Handle pressing of the start / stop button
     * @param view the view
     */
    public void onBofButtonClick(View view) {
        if (bofStarted) {
            stopBirdsOfFeather();
            this.bofStarted = false;
            setToStartButton();

            // Stop the Nearby Students Service
            stopService(nearbyStudentService);
        } else {
            startBirdsOfFeather(user);
            this.bofStarted = true;
            setToStopButton();

            // Start the Nearby Students Service to check for nearby students
            nearbyStudentService = new Intent(BirdsOfAFeatherActivity.this, NearbyStudentsService.class);
            startService(nearbyStudentService);
        }
    }

    /**
     * Start running birds of a feather
     * @param user the user
     */
    public void startBirdsOfFeather(PersonWithCourses user) {
        //TODO: Have students come into range for this "fake startup", so you can demo multiple things.
        //ie. You clear then somebody new shows up, but others dont. Or of course realtime you see somebody show up.

        // Faked list of nearby students
        List<PersonWithCourses> fakeNearby = new ArrayList<>();

        if (db.personWithCoursesDAO().maxId() < 3) {

            Person fakePersonOne = new Person(db.personWithCoursesDAO().maxId() + 1, "Bob", null);
            Person fakePersonTwo = new Person(db.personWithCoursesDAO().maxId() + 2, "Lily", null);

            CourseEntry fakeCourseOne = new CourseEntry(db.courseEntryDAO().maxId() + 1, fakePersonOne.personId,
                    "2022", "WI", "CSE", "12", "Tiny");
            databaseHandler.insertCourse(fakeCourseOne);

            CourseEntry fakeCourseTwo = new CourseEntry(db.courseEntryDAO().maxId() + 1, fakePersonTwo.personId,
                    "2022", "WI", "CSE", "12", "Tiny");
            databaseHandler.insertCourse(fakeCourseTwo);

            CourseEntry fakeCourseThree = new CourseEntry(db.courseEntryDAO().maxId() + 1, fakePersonTwo.personId,
                    "2022", "WI", "CSE", "20", "Tiny");
            databaseHandler.insertCourse(fakeCourseThree);

            databaseHandler.insertPersonWithCourses(fakePersonOne);
            databaseHandler.insertPersonWithCourses(fakePersonTwo);

            fakeNearby.add(databaseHandler.getPersonWithCourses(fakePersonOne));
            fakeNearby.add(databaseHandler.getPersonWithCourses(fakePersonTwo));

            // Add default pfp to nearby students
            FetchImage fetchImage = new FetchImage("https://i.imgur.com/OLWcBAL.png");
            fetchImage.start();
            Handler handler = new Handler();

            handler.post (new Runnable() {
                @Override
                public void run() {
                    if (fetchImage.isAlive()) {
                        handler.postDelayed(this, 500);
                    } else {
                        Bitmap bitmap = fetchImage.getBitmap();
                        byte[] byteArr = Converters.bitmapToByteArr(bitmap);
                        databaseHandler.updatePerson(fakePersonOne.personId, fakePersonOne.name, byteArr);
                        databaseHandler.updatePerson(fakePersonTwo.personId, fakePersonTwo.name, byteArr);
                    }
                }
            });

        } else {
            fakeNearby.add(db.personWithCoursesDAO().get(2));
            fakeNearby.add(db.personWithCoursesDAO().get(3));
        }

        // Setup the nearby students handler
        Log.v(TAG, "Attempting to instantiate handler");
        handler = new NearbyStudentsHandler(user, new DummyStudentFinder(fakeNearby, databaseHandler));

        // Setup student view
        studentRecyclerView = findViewById(R.id.personRecyclerView);
        studentLayoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(studentLayoutManager);
        studentViewAdapter = new StudentViewAdapter(handler.getSortedStudentsList(), handler);
        studentRecyclerView.setAdapter(studentViewAdapter);

        // Setup runnable to check nearby students
        Handler runHandler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                if (bofStarted) {
                    handler.refreshStudentClassMap();

                    Log.v(TAG, "Refreshed student class map from main activity.");

                    studentViewAdapter.clear();
                    studentViewAdapter = new StudentViewAdapter(handler.getSortedStudentsList(), handler);
                    studentRecyclerView.setAdapter(studentViewAdapter);

                    Log.v(TAG, "Updated nearby students view in main activity.");
                    runHandler.postDelayed(this, UPDATE_TIME);
                }
            }
        };
        runHandler.postDelayed(r, UPDATE_TIME);
    }

    /**
     * Stop running BoF
     */
    public void stopBirdsOfFeather() {
        Log.v(TAG, "Stopped Birds of a Feather.");
    }

    /**
     * Handle clear button
     * @param view the view
     */
    public void onClearButtonClicked(View view) {
        databaseHandler.clearNonUserEntries();
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

    /**
     * Handle edit profile button
     * @param view the view
     */
    public void onEditProfileButtonClicked(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Change to start button
     */
    public void setToStartButton() {
        Button bofButton = findViewById(R.id.bofButton);
        bofButton.setText(BOF_START_BTN_TEXT);
        setButtonColor(BOF_START_BTN_COLOR, bofButton);
    }

    /**
     * Change to stop button
     */
    private void setToStopButton() {
        Button bofButton = findViewById(R.id.bofButton);
        bofButton.setText(BOF_STOP_BTN_TEXT);
        setButtonColor(BOF_STOP_BTN_COLOR, bofButton);
    }

    /**
     * Set the color of a button
     * @param color the color as rgb
     * @param button the button to set
     */
    public void setButtonColor(int color, Button button) {
        Drawable buttonDrawable = button.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, color);
        button.setBackground(buttonDrawable);
    }
}