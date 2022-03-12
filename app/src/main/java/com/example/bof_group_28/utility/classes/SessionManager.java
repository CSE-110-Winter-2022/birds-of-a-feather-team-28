package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.sessionManager;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.userId;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.util.ArraySet;
import android.util.Log;

import com.example.bof_group_28.activities.BirdsOfAFeatherActivity;
import com.example.bof_group_28.utility.classes.Prioritizers.DefaultPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.Prioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.StudentSorter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;

public class SessionManager {

    public static String LAST_USED_SESSION = "lastUsedSession";
    public static String NO_SESSION = "No Session Active";
    public static String DIRECTORY_PATH = "";

    private AppDatabase appDatabase;
    private Context context;
    private String currentSession;
    private StudentSorter sorter;

    private List<UUID> uuidList;

    /**
     * Create a new Session Manager
     * @param context app context
     */
    public SessionManager(Context context) {
        DIRECTORY_PATH = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath();
        this.context = context;

        // Set current session to session from last time
        currentSession = getLastUsedSession();

        // Setup database and database handler
        this.appDatabase = AppDatabase.singleton(context);
        databaseHandler = new DatabaseHandler(this.appDatabase);

        // Setup the list of people to be shown in the current session
        uuidList = new ArrayList<>();

        // If it's the first time, run first time setup!
        if (getUserId() != null) {
            userId = getUserId();
        } else {
            firstTimeUserInitialize();
        }
        user = databaseHandler.getUser();

        // Open the session if the session is not the default
        if (!noSessionActive()) {
            openSessionFromStorage(currentSession);
        }

        saveCurrentSessionAsLastUsed();
    }

    public void setSorter(StudentSorter sorter) {
        this.sorter = sorter;
    }

    /**
     * Get the user's ID from file
     * @return
     */
    public UUID getUserId() {
        File uuidFile = new File(DIRECTORY_PATH + "/uuidFile.text");
        if (!uuidFile.exists()) {
            return null;
        }
        try (BufferedReader uuidReader = new BufferedReader(new FileReader(uuidFile))) {
            String line;
            while ((line = uuidReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    Log.d(TAG, "Reading UUID line: " + line);
                    return UUID.fromString(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save the user's UUID to a file
     */
    public void saveUUIDToFile() {
        File uuidFile = new File(DIRECTORY_PATH + "/uuidFile.text");
        try {
            uuidFile.createNewFile();
            FileOutputStream stream;
            stream = new FileOutputStream(uuidFile, false);
            stream.write(userId.toString().getBytes());
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get an unordered list of the people in this session
     * @return list of people
     */
    public List<PersonWithCourses> getPeople() {
        List<PersonWithCourses> peopleList = new ArrayList<>();
        Log.d(TAG, "Loading people from list of UUID size " + uuidList.size());
        for (UUID id : uuidList) {
            peopleList.add(databaseHandler.getPersonFromUUID(id));
        }
        Log.d(TAG, "Returned a list of people with size " + peopleList.size());
        return peopleList;
    }

    /**
     * add a UUID to the list of shown people
     * @param id the UUID
     */
    public void addUUID(UUID id) {
        uuidList.add(id);
    }

    /**
     * Get the list of people in the current session in order of the prioritizer
     * @param prioritizer the prioritizer
     * @return the sorted list of people
     */
    public List<PersonWithCourses> getSortedPeople(Prioritizer prioritizer) {
        return sorter.getSortedStudents(uuidList, prioritizer);
    }

    /**
     * Update current list of people with nearby's newest people
     * @param people the list of people
     */
    public void updatePeopleWithNearby(List<PersonWithCourses> people) {
        for (PersonWithCourses pwc : people) {
            // If we don't have them yet, and they share courses, add them!
            if (!uuidList.contains(pwc.getId()) && databaseHandler.sharesCourses(pwc.getId())) {
                uuidList.add(pwc.getId());
                Log.d(TAG, "Added new UUID to nearby " + pwc.getId());
            }
        }
    }

    /**
     * Get list of sessions as an array list
     * @return the list of sessions
     */
    public List<String> getSessionsList() {
        List<String> fileList = new ArrayList<>();
        File directory = new File(DIRECTORY_PATH);
        Log.d(TAG, "Retrieving Sessions List from " + directory.getPath());
        if (directory.list() != null) {
            for(String f : directory.list()) {
                if (f.equals("uuidFile.text")) {
                    continue;
                }
                fileList.add(f.substring(0, f.length() - 4));
            }
        }
        return fileList;
    }

    public boolean sessionExists(String sessionName) {
        return getSessionsList().contains(sessionName);
    }

    /**
     * "save" the current session
     */
    public void saveCurrentSession() {
        Log.d(TAG, "Saving Session: " + currentSession);
        saveCurrentSessionToStorage();
    }

    /**
     * Change the session to some session name.
     * Updates user for that session with the current user in memory, as that is most updated
     * @param sessionName new name of session
     */
    public void changeSession(String sessionName) {
        Log.d(TAG, "Changing session to " + sessionName);
        currentSession = sessionName;
        clearCurrentPeople();
        if (sessionExists(sessionName)) {
            Log.d(TAG, "Session exists!");
            openSessionFromStorage(sessionName);
        }
        saveCurrentSessionAsLastUsed();
    }

    /**
     * Clear current list of people shown
     */
    public void clearCurrentPeople() {
        uuidList.clear();
    }

    /**
     * Check if it's the user's first time logging in and set up the user if it is
     */
    private void firstTimeUserInitialize() {
        String name = "Name Not Set";

        // Add the person to the Database
        userId = UUID.randomUUID();
        Log.d(TAG, "First time user initialize ran, generated new UUID " + userId);
        Person userPerson = new Person(userId, name, "https://i.imgur.com/OLWcBAL.png");
        appDatabase.personWithCoursesDAO().insert(userPerson);
        saveUUIDToFile();

        databaseHandler.updateUser();
    }

    /**
     * Save the current session to storage
     */
    public void saveCurrentSessionToStorage() {
        Log.d(TAG, "Attempting to save " + currentSession + " to storage");
        StringBuilder personsText = new StringBuilder();

        for (PersonWithCourses pwc : getPeople()) {
            personsText.append(pwc.getId()).append("\n");
        }
        Log.d(TAG, "Saving: " + personsText);

        File sessionFile = new File(DIRECTORY_PATH + "/" + currentSession + ".txt");
        try {
            boolean fileStatus = sessionFile.createNewFile();
            Log.d(TAG, "File creation status for " + currentSession + ".txt : " + fileStatus);

            FileOutputStream stream;
            stream = new FileOutputStream(sessionFile, false);
            stream.write(personsText.toString().getBytes());
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteSession(String session) {
        File sessionFile = new File(DIRECTORY_PATH + "/" + session + ".txt");
        boolean del = sessionFile.delete();
        Log.d(TAG, "Deleted session " + session + " STATUS: " + del);
    }

    public void openSessionFromStorage(String sessionToOpen) {
        Log.d(TAG, "Opening session from storage " + sessionToOpen);
        File sessionFile = new File(DIRECTORY_PATH + "/" + sessionToOpen + ".txt");
        if (!sessionFile.exists()) {
            Log.e(TAG, "Session to open from storage did not exist");
            return;
        }

        try (BufferedReader personsReader = new BufferedReader(new FileReader(sessionFile))) {
            String line;
            while ((line = personsReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    Log.d(TAG, "Reading line: " + line);
                    if (databaseHandler.databaseHasUUID(UUID.fromString(line))) {
                        Log.d(TAG, "Line loaded to user successfully!");
                        addUUID(UUID.fromString(line));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the last used session
     * @return the last used session
     */
    public String getLastUsedSession() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LAST_USED_SESSION, NO_SESSION);
    }

    /**
     * Save the current session to the last used session
     */
    public void saveCurrentSessionAsLastUsed() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LAST_USED_SESSION, currentSession);
        editor.apply();
    }

    /**
     * Rename current session string
     * @param session to rename to
     */
    public void renameSession(String session) {
        currentSession = session;
    }

    /**
     * Rename session to new session
     * @param session the session to renmae
     * @param newSession new session name
     */
    public void renameSessionFile(String session, String newSession) {
        File file = new File(DIRECTORY_PATH + "/" + session + ".txt");
        boolean rename = file.renameTo(new File(DIRECTORY_PATH + "/" + newSession + ".txt"));
        Log.d(TAG, "Renamed session " + session + " to " + newSession + " status: " + rename);
    }

    public boolean noSessionActive() {
        return currentSession.equals(NO_SESSION);
    }

    public String getCurrentSession() {
        return currentSession;
    }

}
