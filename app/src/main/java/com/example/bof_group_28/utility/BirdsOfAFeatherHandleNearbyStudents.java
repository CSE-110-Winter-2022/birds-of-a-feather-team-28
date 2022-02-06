package com.example.bof_group_28.utility;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BirdsOfAFeatherHandleNearbyStudents {

    private Person user;
    private static final String TAG = "BoF";

    private HashMap<Person, List<CourseEntry>> studentClassMap;

    public BirdsOfAFeatherHandleNearbyStudents(Person user) {
        this.user = user;
        studentClassMap = generateStudentClassMap(getNearbyStudents());
        saveNearbyStudentsToDatabase(studentClassMap);
    }

    /**
     * Test constructor
     * @param user user
     * @param nearbyStudents faked nearby students list
     */
    public BirdsOfAFeatherHandleNearbyStudents(Person user, List<Person> nearbyStudents) {
        this.user = user;
        studentClassMap = generateStudentClassMap(nearbyStudents);
        saveNearbyStudentsToDatabase(studentClassMap);
    }

    /**
     * Test constructor
     * @param user user
     * @param studentClassMap faked class map
     */
    public BirdsOfAFeatherHandleNearbyStudents(Person user, HashMap<Person, List<CourseEntry>> studentClassMap) {
        this.user = user;
        this.studentClassMap = studentClassMap;
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
        students.addAll(getStudentClassMap().keySet());
        return students;
    }

    public void refreshStudentClassMapFromDatabase() {

    }

    /**
     * Returns nearby students based on bluetooth
     * @return a list of nearby students
     */
    public List<Person> getNearbyStudents() {
        // get them from bluetooth
        // return that list of students
        return null;
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
        return;
    }

    public void clearDatabase() {
        return;
    }

    public void stop() {
        clearDatabase();
        studentClassMap.clear();
        studentClassMap = null;
    }


}
