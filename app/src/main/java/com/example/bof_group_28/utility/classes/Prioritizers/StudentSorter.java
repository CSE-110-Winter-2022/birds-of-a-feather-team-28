package com.example.bof_group_28.utility.classes.Prioritizers;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;

import android.util.Log;

import com.example.bof_group_28.utility.classes.PersonWithSharedCouseCount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    public List<PersonWithCourses> getSortedStudents(List<UUID> uuidList, Prioritizer prioritizer) {

        Log.d(TAG, "Sorting Student List");
        List<PersonWithSharedCouseCount> sharedCourseCount = new ArrayList<>();

        for (UUID id : uuidList) {
            if (databaseHandler.sharesCourses(id)) {
                double weight = prioritizer.determineWeight(databaseHandler.getSharedCourses(id));
                sharedCourseCount.add(new PersonWithSharedCouseCount(databaseHandler.getPersonFromUUID(id), weight));
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


}
