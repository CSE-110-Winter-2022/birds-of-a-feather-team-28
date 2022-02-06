package com.example.bof_group_28;


import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BirdsOfAFeatherHandleNearbyStudents {

    private Person user;
    private static final String TAG = "BoF";

    public BirdsOfAFeatherHandleNearbyStudents(Person user) {
        this.user = user;
        saveNearbyStudentsToDatabase(generateStudentClassMap(getNearbyStudents()));
    }

    /**
     * Get the student class map from the local database
     * @return the student class map
     */
    public HashMap<Person, List<CourseEntry>> getStudentClassMap() {
        //return from database
        HashMap<Person, List<CourseEntry>> map = new HashMap<>();
        List<CourseEntry> courses = new ArrayList<>();
        courses.add(new DummyCourse());
        map.put(new DummyStudent("Jimmy"), courses);
        return map;
        //return null;
    }

    public HashMap<Person, List<CourseEntry>> getStudentClassMapFaked(Person student) {
        //return from database
        HashMap<Person, List<CourseEntry>> map = new HashMap<>();
        List<CourseEntry> courses = new ArrayList<>();
        courses.add(new DummyCourse());
        map.put(student, courses);
        return map;
        //return null;
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
    }


}
