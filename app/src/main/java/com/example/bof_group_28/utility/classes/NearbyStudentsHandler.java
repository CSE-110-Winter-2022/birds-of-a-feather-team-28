package com.example.bof_group_28.utility.classes;


import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.finder;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.sessionManager;

import android.util.Log;

import com.example.bof_group_28.activities.BirdsOfAFeatherActivity;
import com.example.bof_group_28.utility.classes.Prioritizers.DefaultPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.StudentSorter;
import com.example.bof_group_28.utility.interfaces.StudentFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;

/**
 * This class handles nearby student information
 */
public class NearbyStudentsHandler {

    // The user
    private PersonWithCourses user;

    /**
     * Get the user
     * @return the user
     */
    public PersonWithCourses getUser() {
        return user;
    }
    private StudentSorter sorter;

    /**
     * Constructor
     * @param user user
     */
    public NearbyStudentsHandler(PersonWithCourses user, StudentSorter sorter) {
        this.user = user;
        this.sorter = sorter;

    }

    /**
     * Get nearby students and refresh the student map
     */
    public void refreshNearbyStudents() {
        Log.d(TAG, "Refreshed nearby students finder");
        refreshUser();
        finder.updateNearbyStudents();
        sessionManager.updatePeopleWithNearby(getAllNearbyStudents());
    }

    /**
     * Refresh the user
     */
    public void refreshUser() {
        databaseHandler.updateUser();
        this.user = BirdsOfAFeatherActivity.user;
    }

    /**
     * Get the students in the class map as a list
     * @return the students list
     */
    public List<PersonWithCourses> getAllNearbyStudents() {
        return finder.returnNearbyStudents();
    }

    public void clear() {
        finder.returnNearbyStudents().clear();
    }


}
