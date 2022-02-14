package com.example.bof_group_28.utility.classes;


import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;

import android.util.Log;

import com.example.bof_group_28.activities.BirdsOfAFeatherActivity;
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

    // Instance Variables
    private HashMap<PersonWithCourses, List<CourseEntry>> studentClassMap;
    private StudentFinder studentFinder;

    /**
     * Constructor
     * @param user user
     * @param studentFinder StudentFinder used to retrieve students to handle
     */
    public NearbyStudentsHandler(PersonWithCourses user, StudentFinder studentFinder) {
        this.user = user;
        this.studentFinder = studentFinder;
        studentClassMap = generateStudentClassMap(studentFinder.returnNearbyStudents());
    }

    /**
     * Get nearby students and refresh the student map
     */
    public void refreshStudentClassMap() {
        Log.v(TAG, "Refreshed student class map in handler.");
        refreshUser();
        studentFinder.updateNearbyStudents();
        studentClassMap = generateStudentClassMap(studentFinder.returnNearbyStudents());
    }

    /**
     * Refresh the user
     */
    public void refreshUser() {
        databaseHandler.updateUser();
        this.user = BirdsOfAFeatherActivity.user;
    }

    /**
     * Get the student class map
     * @return the student class map
     */
    public HashMap<PersonWithCourses, List<CourseEntry>> getStudentClassMap() {
        return studentClassMap;
    }

    /**
     * Get the students in the class map as a list
     * @return the students list
     */
    public List<PersonWithCourses> getStudentsList() {
        List<PersonWithCourses> students = new ArrayList<>();
        if (getStudentClassMap() != null) {
            students.addAll(getStudentClassMap().keySet());
        }
        return students;
    }

    /**
     * Return students in class map that share courses and sorted by shared courses
     * @return the list
     */
    public List<PersonWithCourses> getSortedStudentsList() {
        Log.v(TAG, "Sorted Student List");
        List<PersonWithSharedCouseCount> sharedCourseCount = new ArrayList<>();
        for (PersonWithCourses person : getStudentsList()) {
            if (getStudentClassMap() != null && getStudentClassMap().containsKey(person)) {
                sharedCourseCount.add(new PersonWithSharedCouseCount(person, getStudentClassMap().get(person).size()));
            } else {
                Log.e(TAG, "Attempted to access invalid student class map.");
            }
        }
        sharedCourseCount.sort(Collections.reverseOrder());
        List<PersonWithCourses> students = new ArrayList<>();
        for (PersonWithSharedCouseCount pwc : sharedCourseCount) {
            students.add(pwc.getPersonWithCourses());
        }
        return students;
    }

    /**
     * Returns a list of courses from two course lists that contain only the matching courses
     * @param courses1 course list 1
     * @param courses2 course list 2
     * @return the list of matching courses
     */
    public List<CourseEntry> filterCourses(List<CourseEntry> courses1, List<CourseEntry> courses2) {
        List<CourseEntry> filteredCourses = new ArrayList<>();
        for (CourseEntry course1 : courses1) {
            for (CourseEntry course2 : courses2) {
                if (course1.equals(course2)) {
                    filteredCourses.add(course1);
                }
            }
        }
        return filteredCourses;
    }

    /**
     * Gets a map of students to their shared classes with some user
     * @param nearbyStudents the list of nearby students
     * @return a mapping of students to shared classes
     */
    public HashMap<PersonWithCourses, List<CourseEntry>> generateStudentClassMap(List<PersonWithCourses> nearbyStudents) {
        if (nearbyStudents == null) {
            Log.e(TAG,"Attempted to generate a student class map off null nearby students");
            return null;
        }

        HashMap<PersonWithCourses, List<CourseEntry>> matchingStudents = new HashMap<>();
        for (PersonWithCourses p : nearbyStudents) {
            List<CourseEntry> matchingCourses = filterCourses(p.getCourses(), user.getCourses());
            if (matchingCourses.size() > 0) {
                matchingStudents.put(p, matchingCourses);
            }
        }
        return matchingStudents;
    }

    /**
     * Clear the student class map
     */
    public void clear() {
        if (studentClassMap != null) {
            studentClassMap.clear();
        }
        studentClassMap = null;
    }


}
