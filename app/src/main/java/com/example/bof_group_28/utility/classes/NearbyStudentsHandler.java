package com.example.bof_group_28.utility.classes;


import android.util.Log;

import com.example.bof_group_28.utility.interfaces.CourseEntry;
import com.example.bof_group_28.utility.interfaces.Person;
import com.example.bof_group_28.utility.interfaces.StudentFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NearbyStudentsHandler {

    private final Person user;

    public Person getUser() {
        return user;
    }

    private static final String TAG = "BoF";
    private HashMap<Person, List<CourseEntry>> studentClassMap;

    private StudentFinder studentFinder;

    /*
     * Dangerous testing method! Not sure how else to set this up
     * @param nearbyStudents faked nearby students
    public void setNearbyStudents(List<Person> nearbyStudents) {
        this.nearbyStudents = nearbyStudents;
    }*/

    /*
    public NearbyStudentsHandler(Person user) {
        this.user = user;
        nearbyStudents = new ArrayList<>();
        refreshStudentClassMap();
    }*/

    /**
     * Constructor
     * @param user user
     * @param studentFinder StudentFinder used to retrieve students to handle
     */
    public NearbyStudentsHandler(Person user, StudentFinder studentFinder) {
        this.user = user;
        this.studentFinder = studentFinder;
        studentClassMap = generateStudentClassMap(studentFinder.returnNearbyStudents());
        saveNearbyStudentsToDatabase(studentClassMap);
    }

    public void refreshStudentClassMap() {
        studentFinder.updateNearbyStudents();
        studentClassMap = generateStudentClassMap(studentFinder.returnNearbyStudents());
        saveNearbyStudentsToDatabase(studentClassMap);
    }

    /**
     * Get the student class map
     * @return the student class map
     */
    public HashMap<Person, List<CourseEntry>> getStudentClassMap() {
        return studentClassMap;
    }

    public List<Person> getStudentsList() {
        List<Person> students = new ArrayList<>();
        if (getStudentClassMap() != null) {
            students.addAll(getStudentClassMap().keySet());
        }
        return students;
    }

    public void refreshStudentClassMapFromDatabase() {

    }

    /*
     * Returns nearby students based on bluetooth
     * @return a list of nearby students
    public List<Person> getNearbyStudents() {


        Log.v(TAG, "Attempted to retrieve nearby students.");
        // get them from bluetooth
        // update local nearby students
        return nearbyStudents;
    }*/

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
    public HashMap<Person, List<CourseEntry>> generateStudentClassMap(List<Person> nearbyStudents) {
        if (nearbyStudents == null) {
            Log.e(TAG,"Attempted to generate a student class map off null nearby students");
            return null;
        }

        HashMap<Person, List<CourseEntry>> matchingStudents = new HashMap<>();
        for (Person p : nearbyStudents) {
            List<CourseEntry> matchingCourses = filterCourses(p.getCourses(), user.getCourses());
            if (matchingCourses.size() > 0) {
                matchingStudents.put(p, matchingCourses);
            }
        }
        return matchingStudents;
    }

    /**
     * Save nearby students to local
     * @param nearbyStudents a mapping of students to their matching course entries
     */
    public void saveNearbyStudentsToDatabase(HashMap<Person, List<CourseEntry>> nearbyStudents) {
        if (nearbyStudents == null) {
            Log.e(TAG,"Attempted to save a null nearby students list to database");
            return;
        }
        // with database class save the person and their course data
    }

    public void clearDatabase() {
    }

    public void stop() {

    }

    public void clear() {
        clearDatabase();
        if (studentClassMap != null) {
            studentClassMap.clear();
        }
        studentClassMap = null;
    }


}
