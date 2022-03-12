package com.example.bof_group_28.utility.classes;

import com.example.bof_group_28.utility.interfaces.StudentFinder;

import java.util.ArrayList;
import java.util.List;

import model.db.Person;
import model.db.PersonWithCourses;

/**
 * Dummy implementation of the student finder
 */
public class DummyStudentFinder implements StudentFinder {

    List<PersonWithCourses> nearbyStudents;
    private final DatabaseHandler databaseHandler;

    public DummyStudentFinder(List<PersonWithCourses> nearbyStudents, DatabaseHandler databaseHandler){
        this.nearbyStudents = nearbyStudents;
        this.databaseHandler = databaseHandler;
    }

    @Override
    public List<PersonWithCourses> returnNearbyStudents() {
        return this.nearbyStudents;
    }

    @Override
    public void updateNearbyStudents() {
        nearbyStudents.clear();
    }

    @Override
    public int numNearbyStudents() {
        return this.nearbyStudents.size();
    }
}
