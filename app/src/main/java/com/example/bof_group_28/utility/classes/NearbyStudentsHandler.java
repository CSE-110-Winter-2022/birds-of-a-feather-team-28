package com.example.bof_group_28.utility.classes;


import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;

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

    private StudentFinder studentFinder;
    private StudentSorter sorter;

    /**
     * Constructor
     * @param user user
     * @param studentFinder StudentFinder used to retrieve students to handle
     */
    public NearbyStudentsHandler(PersonWithCourses user, StudentFinder studentFinder) {
        this.user = user;
        this.studentFinder = studentFinder;
        this.sorter = new StudentSorter(user);
    }

    /**
     * Get nearby students and refresh the student map
     */
    public void refreshNearbyStudents() {
        Log.v(TAG, "Refreshed nearby students finder");
        refreshUser();
        studentFinder.updateNearbyStudents();
        appendNearbyStudentsToDatabase();
    }

    public void appendNearbyStudentsToDatabase() {
        for (PersonWithCourses p : studentFinder.returnNearbyStudents()) {
            if (!databaseHandler.hasPerson(p)) {
                //TODO: need to append to the datbase, but how do u know if the student is a new addition or not.
                for (CourseEntry course : p.getCourses()) {
                    databaseHandler.insertCourse(course);
                }
                databaseHandler.insertPersonWithCourses(p.person);
            }
        }
    }

    /**
     * Refresh the user
     */
    public void refreshUser() {
        databaseHandler.updateUser();
        this.user = BirdsOfAFeatherActivity.user;
    }

    public HashMap<PersonWithCourses, List<CourseEntry>> getStudentSorterMap() {
        return sorter.generateStudentClassMap(getAllNearbyStudents());
    }

    public List<PersonWithCourses> getNearbyStudentsSharedCourses() {
        //TODO: Change to prioritize by type
        return sorter.getSortedStudents(studentFinder.returnNearbyStudents(), new DefaultPrioritizer());
    }

    /**
     * Get the students in the class map as a list
     * @return the students list
     */
    public List<PersonWithCourses> getAllNearbyStudents() {
        return studentFinder.returnNearbyStudents();
    }

    public void clear() {
        studentFinder.returnNearbyStudents().clear();
    }


}
