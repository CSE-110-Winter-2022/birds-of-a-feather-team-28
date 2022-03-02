package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import model.db.AppDatabase;
import model.db.CourseEntry;

public class SessionManager {

    public static String SESSION_LIST = "sessionList";
    public static String LAST_USED_SESSION = "lastUsedSession";
    public static String DEFAULT_SESSION_NAME = "default";

    private AppDatabase appDatabase;
    private Context context;
    private String currentSession;

    public List<String> sessions;

    public SessionManager(Context context) {
        this.context = context;
        currentSession = getLastUsedSession();
        this.appDatabase = AppDatabase.singleton(context, currentSession);
        sessions = getSessionsList();
        saveCurrentSessionAsLastUsed();
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
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return new ArrayList<>(preferences.getStringSet(SESSION_LIST, new ArraySet<>()));
    }

    /**
     * Save the list of sessions to file
     */
    public void saveSessionsList() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> sessionsSet = new ArraySet<>();
        sessionsSet.addAll(sessions);
        editor.putStringSet(SESSION_LIST, sessionsSet);
        editor.apply();
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
     * Change the session to some session name.
     * Updates user for that session with the current user in memory, as that is most updated
     * @param sessionName new name of session
     */
    public void changeSession(String sessionName) {
        if (!sessions.contains(sessionName) && !sessionName.equals(DEFAULT_SESSION_NAME)) {
            sessions.add(sessionName);
            saveSessionsList();
            // Start a new session and clear data which should not be here
            AppDatabase.singleton(context, sessionName);
            databaseHandler.db.clearAllTables();
        } else {
            // Pull from existing session
            AppDatabase.singleton(context, sessionName);
        }

        // Insert the original user stored in memory
        // Make sure to only delete original user if they actually exist in the database
        if (appDatabase.personWithCoursesDAO().count() > 0) {
            appDatabase.personWithCoursesDAO().deletePerson(1);
        }
        appDatabase.personWithCoursesDAO().insert(user.person);

        // Update db with original user's courses
        List<CourseEntry> courses = user.getCourses();
        databaseHandler.clearCourses(user.getId());
        databaseHandler.insertCourseList(courses);

        if (currentSession.equals(DEFAULT_SESSION_NAME)) {
            databaseHandler.clearNonUserEntries();
        }
        saveCurrentSessionAsLastUsed();
    }

}
