package com.example.bof_group_28.activities;

import static com.example.bof_group_28.utility.classes.SessionManager.NO_SESSION;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bof_group_28.utility.Utilities;
import com.example.bof_group_28.utility.classes.Calendar.DateFinder;
import com.example.bof_group_28.utility.classes.FakePersonParser;
import com.example.bof_group_28.utility.classes.NearbyStudentsFinder;
import com.example.bof_group_28.utility.classes.Prioritizers.DefaultPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.Prioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.RecentPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.SmallClassPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.StudentSorter;
import com.example.bof_group_28.utility.classes.Prioritizers.ThisQuarterPrioritizer;
import com.example.bof_group_28.utility.classes.SessionManager;
import com.example.bof_group_28.utility.classes.Converters;
import com.example.bof_group_28.utility.classes.DatabaseHandler;
import com.example.bof_group_28.utility.classes.DummyStudentFinder;
import com.example.bof_group_28.utility.classes.FetchImage;
import com.example.bof_group_28.utility.classes.NearbyStudentsHandler;
import com.example.bof_group_28.R;
import com.example.bof_group_28.utility.enums.SizeName;
import com.example.bof_group_28.utility.services.NearbyStudentsService;
import com.example.bof_group_28.viewAdapters.StudentViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;

/**
 * This class is the main Birds of a Feather Activity that opens on app startup
 */
public class BirdsOfAFeatherActivity extends AppCompatActivity {

    // User of the App
    public static UUID userId;
    public static PersonWithCourses user;

    // AppDatabase Mediator
    public static SessionManager sessionManager;

    // Student View Management
    private RecyclerView studentRecyclerView;
    private RecyclerView.LayoutManager studentLayoutManager;
    private StudentViewAdapter studentViewAdapter;

    // Instance Variables
    private boolean bofStarted;
    private NearbyStudentsHandler handler;
    private Intent nearbyStudentService;
    private Prioritizer currentPrioritizer;

    // Constants
    public static final String PREF_NAME = "preferences";
    public static final String TAG = "BoF: ";
    private static final String BOF_START_BTN_TEXT = "START";
    private static final String BOF_STOP_BTN_TEXT = "STOP";
    private static final int BOF_START_BTN_COLOR = Color.rgb(76, 175, 80);
    private static final int BOF_STOP_BTN_COLOR = Color.RED;
    private static final int UPDATE_TIME = 2000;

    private static final String DATE_FORMAT = "MM∕dd∕yyyy hh꞉mm:ss aa";

    // Database
    public static DatabaseHandler databaseHandler;

    /**
     * Create the Birds of a Feather Activity on App Startup
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request file permissions for session storing
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        sessionManager = new SessionManager(getApplicationContext());
        bofStarted = false;

        TextView sessionNameField = findViewById(R.id.sessionNameField);
        sessionNameField.setText(sessionManager.getCurrentSession());

        Log.d(TAG, "Sessions Available: " + sessionManager.getSessionsList().toString());

        // Setup the nearby students handler
        Log.d(TAG, "Attempting to instantiate handler");
        StudentSorter sorter = new StudentSorter(user);
        handler = new NearbyStudentsHandler(user, new NearbyStudentsFinder(this.getApplicationContext()), sorter);

        sessionManager.setSorter(sorter);
        currentPrioritizer = new DefaultPrioritizer();

        String[] prioritizers = {"DEFAULT", "RECENT", "SMALL COURSES", "THIS QUARTER"};

        Spinner prioritizerSpinner = findViewById(R.id.prioritizer_dd);
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prioritizers);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritizerSpinner.setAdapter(sizeAdapter);
        prioritizerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                if (position == 0) {
                    currentPrioritizer = new DefaultPrioritizer();
                    Log.d(TAG, "Prioritizing by default order");
                } else if (position == 1) {
                    DateFinder dateFinder = new DateFinder();
                    currentPrioritizer = new RecentPrioritizer(dateFinder.getCurrYear(), dateFinder.getCurrQuarter());
                    Log.d(TAG, "Prioritizing by most recent based on: " + dateFinder.getCurrYear() + " and " + dateFinder.getCurrQuarter());
                } else if (position == 2) {
                    currentPrioritizer = new SmallClassPrioritizer();
                    Log.d(TAG, "Prioritizing off Small Courses");
                } else {
                    DateFinder dateFinder = new DateFinder();
                    currentPrioritizer = new ThisQuarterPrioritizer(dateFinder.getCurrYear(), dateFinder.getCurrQuarter());
                    Log.d(TAG, "Prioritizing by this quarter: " + dateFinder.getCurrYear() + " and " + dateFinder.getCurrQuarter());
                }

                updateStudentsView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Setup student view
        studentRecyclerView = findViewById(R.id.personRecyclerView);
        studentLayoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(studentLayoutManager);
        studentViewAdapter = new StudentViewAdapter(sessionManager.getSortedPeople(currentPrioritizer), handler);
        studentRecyclerView.setAdapter(studentViewAdapter);
    }

    /**
     * Handle pressing of the start / stop button
     * @param view the view
     */
    public void onBofButtonClick(View view) {
        if (bofStarted) {
            // Always prompt the user if they want to save the session
            showSaveCurrentPrompt("Please save this session.");
        } else {
            // certainly start a new session
            startNewSession();
        }
    }

    /**
     * Show prompt for saving the current session
     * @param message message to display in prompt
     */
    public void showSaveCurrentPrompt(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setText(sessionManager.getCurrentSession());
        builder.setMessage(message)
                .setPositiveButton("Save Session", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "User attempting to save current session");
                        String inputName = input.getText().toString();
                        if (inputName.equals(NO_SESSION) || inputName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Invalid Session Name", Toast.LENGTH_SHORT).show();
                            showSaveCurrentPrompt(message);
                            return;
                        }
                        if (sessionManager.sessionExists(inputName)) {
                            Toast.makeText(getApplicationContext(), "Session Already Exists", Toast.LENGTH_SHORT).show();
                            showSaveCurrentPrompt(message);
                            return;
                        }
                        if (inputName.length() > 30) {
                            Toast.makeText(getApplicationContext(), "Session Name too Long", Toast.LENGTH_SHORT).show();
                            showSaveCurrentPrompt(message);
                            return;
                        }
                        sessionManager.renameSession(inputName);
                        sessionManager.saveCurrentSession();
                        Log.d(TAG, "User successfully saved current session");
                        clickStopButton();
                    }
                }).setCancelable(false);
        builder.setView(input);
        builder.create().show();
    }

    /**
     * Run all functions related to starting BoF
     */
    public void clickStartButton() {
        updateSessionNameField();
        this.bofStarted = true;
        startBirdsOfFeather();
        setToStopButton();

        // Start the Nearby Students Service to check for nearby students
        nearbyStudentService = new Intent(BirdsOfAFeatherActivity.this, NearbyStudentsService.class);
        startService(nearbyStudentService);
    }

    /**
     * Run all functions related to stopping BoF
     */
    public void clickStopButton() {
        Log.d(TAG, "Stopped Birds of a Feather.");
        updateSessionNameField();
        this.bofStarted = false;
        setToStartButton();

        // Stop the Nearby Students Service
        stopService(nearbyStudentService);
    }

    /**
     * Start a new session with an obviously unique date
     */
    public void startNewSession() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String strDate = dateFormat.format(currentTime);
        if (sessionManager.sessionExists(strDate)) {
            Log.e(TAG, "User attempted to start session with date that already exists: " + strDate);
            Toast.makeText(this, "Couldn't start new session. Current timestamp is already saved!", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Starting a New Session", Toast.LENGTH_LONG).show();
        sessionManager.changeSession(strDate);
        updateStudentsView();
        clickStartButton();
    }

    /**
     * Update the nearby students view
     */
    public void updateStudentsView() {
        studentViewAdapter.clear();
        studentViewAdapter = new StudentViewAdapter(sessionManager.getSortedPeople(currentPrioritizer), handler);
        studentRecyclerView.setAdapter(studentViewAdapter);
        Log.d(TAG, "Updated nearby students view");
    }

    /**
     * Update the current session name
     */
    public void updateSessionNameField() {
        TextView sessionNameField = findViewById(R.id.sessionNameField);
        sessionNameField.setText(sessionManager.getCurrentSession());
    }

    public void onMockButton(View view) {
        mockUserPrompt("Mock a user.");
    }

    public void mockUserPrompt(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setMessage(message)
                .setPositiveButton("Mock", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "User attempting to mock fake user with Nearby");
                        String inputText = input.getText().toString();
                        FakePersonParser.addFakePersonToDatabaseFromString(inputText);
                        Toast.makeText(getApplicationContext(), "Mocking user...", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setView(input);
        builder.create().show();
    }

    /**
     * Start running birds of a feather
     */
    public void startBirdsOfFeather() {
        // Setup runnable to check nearby students
        Handler runHandler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                if (bofStarted) {
                    handler.refreshNearbyStudents();
                    updateStudentsView();
                    Log.d(TAG, "Refreshed and updated nearby students view in main activity.");
                    runHandler.postDelayed(this, UPDATE_TIME);
                }
            }
        };
        runHandler.postDelayed(r, UPDATE_TIME);
    }

    /**
     * Change to start button
     */
    public void setToStartButton() {
        Log.d(TAG, "Changing button to Start Button");
        Button bofButton = findViewById(R.id.bofButton);
        bofButton.setText(BOF_START_BTN_TEXT);
        setButtonColor(BOF_START_BTN_COLOR, bofButton);
    }

    /**
     * Change to stop button
     */
    private void setToStopButton() {
        Log.d(TAG, "Changing button to Stop Button");
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

    /**
     * Handle the view sessions button
     * @param view the view
     */
    public void onViewSessionsButtonClicked(View view) {
        Log.d(TAG, "User clicked View Sessions button");
        Intent intent = new Intent(this, SessionViewActivity.class);
        startActivityForResult(intent, 0);
    }

    /**
     * Handle returning from the view session to update the view
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Log.d(TAG, "Updating views as a Session was just loaded");
            updateSessionNameField();
            updateStudentsView();
        }

        if (requestCode == 5) {
            Log.d(TAG, "Updating views as profile was edited");
            updateStudentsView();
        }
    }

    /**
     * Handle edit profile button
     * @param view the view
     */
    public void onEditProfileButtonClicked(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivityForResult(intent, 5);
    }
}