package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.ArraySet;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;

public class SessionManager {

    public static String SESSION_LIST = "sessionList";
    public static String LAST_USED_SESSION = "lastUsedSession";
    public static String DEFAULT_SESSION_NAME = "default";

    private AppDatabase appDatabase;
    private Context context;
    private String currentSession;

    public SessionManager(Context context) {
        this.context = context;
        currentSession = getLastUsedSession();
        this.appDatabase = AppDatabase.singleton(context);

        if (!isDefaultSession()) {
            openSessionFromStorage(currentSession);
        }
        saveCurrentSessionAsLastUsed();
    }

    /**
     * Save the current session to storage
     */
    public void saveCurrentSessionToStorage() {
        this.appDatabase = AppDatabase.singleton(context);
        StringBuilder personsText = new StringBuilder();
        StringBuilder coursesText = new StringBuilder();

        // Build CSV string for people
        for (int i = 0; i < appDatabase.personWithCoursesDAO().count(); i++) {
            PersonWithCourses p = appDatabase.personWithCoursesDAO().get(i + 1);
            personsText.append(p.person.personId + "," + p.person.name + "," + bytesToHex(p.person.profilePic) + "\n");
        }

        // Build CSV string for courses
        for (int i = appDatabase.courseEntryDAO().maxId() - appDatabase.courseEntryDAO().count() + 1; i < appDatabase.courseEntryDAO().maxId(); i++) {
            CourseEntry course = appDatabase.courseEntryDAO().get(i + 1);
            coursesText.append(course.courseId + "," + course.personId + ","
                    + course.year + "," + course.quarter + ","
                    + course.subject + "," + course.courseNum + "," + course.size + "\n");
        }

        Log.v(TAG, "Appending to File " + currentSession + "/persons.csv: \n" + personsText);
        Log.v(TAG, "Appending to File " + currentSession + "/courses.csv: \n" + coursesText);

        // Create necessary files
        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/dataFiles/" + currentSession);
        File personsFile = new File(directory, "/persons.csv");
        File coursesFile = new File(directory, "/courses.csv");
        if (!directory.exists()) {
            Log.v(TAG, "Parent Directory doesn't exist. Generating directory");
            boolean madeDir = directory.mkdirs();
            Log.v(TAG, "Directory creation status: " + madeDir);
        }
        FileOutputStream outputStream;
        try {
            boolean fileStatus = personsFile.createNewFile();
            coursesFile.createNewFile();
            Log.v(TAG, "Creation Status for PersonFile: " + fileStatus);

            // Append CSV strings to files
            outputStream = new FileOutputStream(personsFile, false);
            outputStream.write(personsText.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            outputStream = new FileOutputStream(coursesFile, false);
            outputStream.write(coursesText.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
    // This makes it easy to store in CSV
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    //TODO: IMPORTANT!!!! COURSES ARE DISAPPEARING? LIKE ONE AT A TIME?
    //https://www.baeldung.com/java-csv-file-array
    public boolean openSessionFromStorage(String sessionToOpen) {
        this.appDatabase = AppDatabase.singleton(context);
        Log.v(TAG, "Opening session from storage " + sessionToOpen);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/dataFiles/" + sessionToOpen);
        if (!file.exists()) {
            Log.e(TAG, "Session to open from storage did not exist");
            return false;
        }


        appDatabase.clearAllTables();
        Log.v(TAG, "Cleared tables to counts: " + appDatabase.personWithCoursesDAO().count() + " and " + appDatabase.courseEntryDAO().count());

        try (BufferedReader personsReader = new BufferedReader(new FileReader(
                Environment.getExternalStorageDirectory().getPath() + "/dataFiles/" + currentSession + "/persons.csv"))) {
            String line;
            while ((line = personsReader.readLine()) != null) {
                Log.v(TAG, "Parsing line: " + line);
                String[] values = new String[3];
                values[0] = line.substring(0, line.indexOf(","));
                String second = line.substring(line.indexOf(",") + 1);
                values[1] = second.substring(0, second.indexOf(","));
                values[2] = second.substring(second.indexOf(",") + 1);
                Log.v(TAG, "Attempting to load " + Arrays.asList(values).toString());
                Person p = new Person(Integer.parseInt(values[0]), values[1], values[2].getBytes());
                appDatabase.personWithCoursesDAO().insert(p);
                Log.v(TAG, "Loaded person " + p.name + " " + p.personId + " " + Arrays.toString(p.profilePic));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader coursesReader = new BufferedReader(new FileReader(
                Environment.getExternalStorageDirectory().getPath() + "/dataFiles/" + currentSession + "/courses.csv"))) {
            String line;
            while ((line = coursesReader.readLine()) != null) {
                String[] values = line.split(",");
                CourseEntry c = new CourseEntry(Integer.parseInt(values[0]), Integer.parseInt(values[1]), values[2], values[3], values[4], values[5], values[6]);
                appDatabase.courseEntryDAO().insert(c);
                Log.v(TAG, "Loaded course " + c.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isDefaultSession() {
        return (currentSession.equals(DEFAULT_SESSION_NAME));
    }

    public AppDatabase getAppDatabase() {
        this.appDatabase = AppDatabase.singleton(context);
        return appDatabase;
    }

    public String getCurrentSession() {
        return currentSession;
    }

    /**
     * Get list of sessions as an array list
     * @return the list of sessions
     */
    public List<String> getSessionsList() {
        List<String> fileList = new ArrayList<>();
        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/dataFiles");
        if (directory.exists()) {
            Log.v(TAG, "Retrieving Sessions List from " + directory.getPath());
            for (String s : directory.list()) {
                fileList.add(s);
            }
        }
        return fileList;
    }

    /**
     * Get the last used session
     * @return the last used session
     */
    public String getLastUsedSession() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LAST_USED_SESSION, DEFAULT_SESSION_NAME);
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
     * "save" the current default session
     * @param sessionName session
     */
    public void saveSession(String sessionName) {
        Log.v(TAG, "Saving Session: " + sessionName);
        currentSession = sessionName;
        saveCurrentSessionToStorage();
    }

    /**
     * Change the session to some session name.
     * Updates user for that session with the current user in memory, as that is most updated
     * @param sessionName new name of session
     */
    public void changeSession(String sessionName) {
        this.appDatabase = AppDatabase.singleton(context);
        Log.v(TAG, "Changing session to " + sessionName);
        // IMPORTANT: Always clear default database as it represents the unsaved database. Otherwise get from file
        currentSession = sessionName;
        if (sessionName.equals(DEFAULT_SESSION_NAME)) {
            databaseHandler.clearNonUserEntries();
        } else {
            openSessionFromStorage(sessionName);
        }

        // TODO: Doublecheck logic that ensures user object is actually up to date

        appDatabase.personWithCoursesDAO().deletePerson(1);
        appDatabase.personWithCoursesDAO().insert(user.person);

        // Update db with original user's courses
        List<CourseEntry> courses = user.getCourses();
        databaseHandler.clearCourses(user.getId());
        databaseHandler.insertCourseList(courses);

        saveCurrentSessionAsLastUsed();
    }

}
