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
import java.util.ArrayList;
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

    public void saveCurrentSessionToStorage() {
        StringBuilder personsText = new StringBuilder();
        StringBuilder coursesText = new StringBuilder();

        for (int i = 0; i < appDatabase.personWithCoursesDAO().count(); i++) {
            PersonWithCourses p = appDatabase.personWithCoursesDAO().get(i + 1);
            personsText.append(p.person.personId + "," + p.person.name + "," + new String(p.person.profilePic) + "\n");
        }

        for (int i = 0; i < appDatabase.courseEntryDAO().count(); i++) {
            CourseEntry course = appDatabase.courseEntryDAO().get(i + 1);
            coursesText.append(course.courseId + "," + course.personId + ","
                    + course.subject + "," + course.courseNum + ","
                    + course.quarter + "," + course.year + "," + course.size + "\n");
        }

        Log.v(TAG, "Appending to File " + currentSession + "/persons.csv: \n" + personsText);
        Log.v(TAG, "Appending to File " + currentSession + "/courses.csv: \n" + coursesText);

        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "dataFiles/"); //+ currentSession);
        File personsFile = new File(directory, "persons.csv");
        File coursesFile = new File(directory, "courses.csv");
        if (!directory.exists()) {
            Log.v(TAG, "Parent Directory doesn't exist. Generating directory");
            boolean madeDir = directory.mkdirs();
            if (!directory.canWrite()) {
                System.out.println("WHYYY");
            }
            Log.v(TAG, "Directory creation status: " + madeDir);
        }
        FileOutputStream outputStream;
        try {
            boolean fileStatus = personsFile.createNewFile();
            coursesFile.createNewFile();
            Log.v(TAG, "Creation Status for PersonFile: " + fileStatus);

            outputStream = new FileOutputStream(personsFile, false);
            outputStream.write(personsText.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            outputStream = new FileOutputStream(coursesFile, true);
            outputStream.write(coursesText.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //https://www.baeldung.com/java-csv-file-array
    public boolean openSessionFromStorage(String sessionToOpen) {
        File file = new File(Environment.getExternalStorageDirectory(), sessionToOpen + ".csv");
        if (!file.exists()) {
            return false;
        }
        try (BufferedReader personsReader = new BufferedReader(new FileReader(
                Environment.getExternalStorageDirectory().getPath() + "dataFiles/" + currentSession + "/persons.csv"))) {
            String line;
            while ((line = personsReader.readLine()) != null) {
                String[] values = line.split(",");
                Person p =new Person(Integer.parseInt(values[0]), values[1], values[2].getBytes());
                appDatabase.personWithCoursesDAO().insert(p);
                Log.v(TAG, "Loaded person " + p.name + " " + p.personId + " " + p.profilePic);
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
        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "dataFiles/");
        if (directory.exists()) {
            Log.v(TAG, "Retrieving Sessions List from " + directory.getPath());
            for (File f : directory.listFiles(File::isDirectory)) {
                fileList.add(f.getName().substring(0, f.getName().length() - 4));
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
        // IMPORTANT: Always clear default database as it represents the unsaved database. Otherwise get from file
        currentSession = sessionName;
        if (sessionName.equals(DEFAULT_SESSION_NAME)) {
            databaseHandler.clearNonUserEntries();
        } else {
            openSessionFromStorage(sessionName);
        }
        appDatabase.personWithCoursesDAO().deletePerson(1);
        appDatabase.personWithCoursesDAO().insert(user.person);

        // Update db with original user's courses
        List<CourseEntry> courses = user.getCourses();
        databaseHandler.clearCourses(user.getId());
        databaseHandler.insertCourseList(courses);

        saveCurrentSessionAsLastUsed();
    }

}
