package com.example.bof_group_28.utility.interfaces;

import com.example.bof_group_28.utility.classes.DatabaseHandler;

import java.util.List;

import model.db.PersonWithCourses;

public interface StudentFinder {
    List<PersonWithCourses> returnNearbyStudents();
    void updateNearbyStudents();
    int numNearbyStudents();

}
