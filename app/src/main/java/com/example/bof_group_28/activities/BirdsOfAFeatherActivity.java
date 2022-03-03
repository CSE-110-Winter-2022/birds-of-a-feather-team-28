package com.example.bof_group_28.activities;

import static com.example.bof_group_28.utility.classes.SessionManager.DEFAULT_SESSION_NAME;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bof_group_28.utility.Utilities;
import com.example.bof_group_28.utility.classes.SessionManager;
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

    /**
     * Create the Birds of a Feather Activity on App Startup
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        sessionManager = new SessionManager(getApplicationContext());
        bofStarted = false;
        databaseHandler = new DatabaseHandler(sessionManager.getAppDatabase(), getApplicationContext());

        if (sessionManager.getCurrentSession().equals(DEFAULT_SESSION_NAME)) {
            databaseHandler.clearNonUserEntries();
        }

        TextView sessionNameField = findViewById(R.id.sessionNameField);
        sessionNameField.setText(sessionManager.getCurrentSession());

        Log.v(TAG, "Sessions Available: " + sessionManager.getSessionsList().toString());

        if (GoogleSignIn.getLastSignedInAccount(this) == null) {
            Log.v(TAG, "User is not logged in through Google.");

            //TODO: uncomment
            //Intent googleIntent = new Intent(this, GoogleSignInActivity.class);
            //startActivity(googleIntent);
        } else {
            Log.v(TAG, "User is already logged in!");
        }


        // Setup the nearby students handler
        Log.v(TAG, "Attempting to instantiate handler");
        handler = new NearbyStudentsHandler(user, new DummyStudentFinder(new ArrayList<>(), databaseHandler));

        // Setup student view
        studentRecyclerView = findViewById(R.id.personRecyclerView);
        studentLayoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(studentLayoutManager);
        studentViewAdapter = new StudentViewAdapter(handler.getSortedStudentsList(), handler);
        studentRecyclerView.setAdapter(studentViewAdapter);

        // TODO: Current logic is to refresh every time there is a change. But to check for a change the handler must be
        // updated which changes ordering and is generally inefficient. Come up with a better way to chekc for change.
        Handler dbRunHandler = new Handler();
        dbRunHandler.post (new Runnable() {
            @Override
            public void run() {
                if (!bofStarted && isSessionDifferent(sessionManager.getCurrentSession())) {
                    handler.refreshStudentClassMap();
                    studentViewAdapter.clear();
                    studentViewAdapter = new StudentViewAdapter(handler.getSortedStudentsList(), handler);
                    studentRecyclerView.setAdapter(studentViewAdapter);
                }
                updateSessionNameField();
                dbRunHandler.postDelayed(this, 100);
            }
        });
    }

    public boolean isSessionDifferent(String session) {
        TextView sessionNameField = findViewById(R.id.sessionNameField);
        return !sessionNameField.getText().toString().equals(session)
                || !(handler.getStudentsList().containsAll(studentViewAdapter.students)
                && studentViewAdapter.students.containsAll(handler.getStudentsList()));
    }

    public void updateSessionNameField() {
        TextView sessionNameField = findViewById(R.id.sessionNameField);
        sessionNameField.setText(sessionManager.getCurrentSession());
    }

    /**
     * Handle pressing of the start / stop button
     * @param view the view
     */
    public void onBofButtonClick(View view) {
        if (bofStarted) {
            if (sessionManager.isDefaultSession()) {
                showSaveCurrentPrompt("Do you want to save this session?");
            } else {
                // Certainly save the session
                sessionManager.saveCurrentSessionToStorage();
                clickStopButton();
            }
        } else {
            showNewCurrentPrompt("Do you want to start a new session or use an existing one?");
        }
    }

    public void clickStartButton() {
        updateSessionNameField();
        startBirdsOfFeather(user);
        this.bofStarted = true;
        setToStopButton();

        // Start the Nearby Students Service to check for nearby students
        nearbyStudentService = new Intent(BirdsOfAFeatherActivity.this, NearbyStudentsService.class);
        startService(nearbyStudentService);
    }

    public void clickStopButton() {
        updateSessionNameField();
        stopBirdsOfFeather();
        this.bofStarted = false;
        setToStartButton();

        // Stop the Nearby Students Service
        stopService(nearbyStudentService);
    }

    /**
     * Show alert for prompt
     * @param message the message
     */
    public void showNewCurrentPrompt(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("New", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sessionManager.changeSession(DEFAULT_SESSION_NAME);
                        clickStartButton();
                    }
                })
                .setNegativeButton("Current", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getApplicationContext(), "Open a Previous Session in the Bottom Right", Toast.LENGTH_LONG).show();
                        clickStartButton();
                    }
                });
        builder.create().show();
    }

    public void showSaveCurrentPrompt(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        builder.setMessage(message)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String inputName = input.getText().toString();
                        if (inputName.equals(DEFAULT_SESSION_NAME) || inputName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Invalid Session Name", Toast.LENGTH_SHORT).show();
                            showSaveCurrentPrompt(message);
                            return;
                        }
                        if (sessionManager.getSessionsList().contains(inputName)) {
                            Toast.makeText(getApplicationContext(), "Session Already Exists", Toast.LENGTH_SHORT).show();
                            showSaveCurrentPrompt(message);
                            return;
                        }
                        if (inputName.length() > 20) {
                            Toast.makeText(getApplicationContext(), "Session Name too Long", Toast.LENGTH_SHORT).show();
                            showSaveCurrentPrompt(message);
                            return;
                        }
                        sessionManager.saveSession(inputName);
                        sessionManager.changeSession(inputName);
                        clickStopButton();
                    }
                })
                .setNegativeButton("Don't Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickStopButton();
                    }
                });
        builder.setView(input);
        builder.create().show();
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

        AppDatabase db = sessionManager.getAppDatabase();
        if (db.personWithCoursesDAO().maxId() < 3) {

            Person fakePersonOne = new Person(db.personWithCoursesDAO().maxId() + 1, "Bob", null);
            Person fakePersonTwo = new Person(db.personWithCoursesDAO().maxId() + 2, "Lily", null);

            CourseEntry fakeCourseOne = new CourseEntry(db.courseEntryDAO().maxId() + 1, fakePersonOne.personId,
                    "2022", "Winter", "CSE", "12", "Tiny (<40)");
            databaseHandler.insertCourse(fakeCourseOne);

            CourseEntry fakeCourseTwo = new CourseEntry(db.courseEntryDAO().maxId() + 1, fakePersonTwo.personId,
                    "2022", "Winter", "CSE", "12", "Tiny (<40)");
            databaseHandler.insertCourse(fakeCourseTwo);

            CourseEntry fakeCourseThree = new CourseEntry(db.courseEntryDAO().maxId() + 1, fakePersonTwo.personId,
                    "2022", "Winter", "CSE", "20", "Tiny (<40)");
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
        // TODO insert fake students

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

    public void onViewSessionsButtonClicked(View view) {
        Intent intent = new Intent(this, SessionViewActivity.class);
        startActivity(intent);
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