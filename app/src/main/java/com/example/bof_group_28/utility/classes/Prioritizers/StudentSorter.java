package com.example.bof_group_28.utility.classes.Prioritizers;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;

import android.util.Log;

import com.example.bof_group_28.utility.classes.PersonWithSharedCouseCount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import model.db.CourseEntry;
import model.db.PersonWithCourses;

public class StudentSorter {

    private PersonWithCourses user;

    public StudentSorter(PersonWithCourses user){
        this.user = user;
    }

    /**
     * Return students in class map that share courses and sorted by shared courses
     * @return the list
     */
    public List<PersonWithCourses> getSortedStudents(List<PersonWithCourses> inpList, Prioritizer prioritizer) {

        Log.v(TAG, "Sorted Student List");
        List<PersonWithSharedCouseCount> sharedCourseCount = new ArrayList<>();
        HashMap<PersonWithCourses, List<CourseEntry>> studentClassMap = generateStudentClassMap(inpList);

        for (PersonWithCourses student : inpList) {
            if (studentClassMap != null && studentClassMap.containsKey(student)) {
                double weight = prioritizer.determineWeight(studentClassMap.get(student));
                sharedCourseCount.add(new PersonWithSharedCouseCount(student, weight));
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


}
