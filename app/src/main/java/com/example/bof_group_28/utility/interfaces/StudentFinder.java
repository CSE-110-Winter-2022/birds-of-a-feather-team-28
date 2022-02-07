package com.example.bof_group_28.utility.interfaces;

import java.util.List;

public interface StudentFinder {

    List<Person> returnNearbyStudents();
    void updateNearbyStudents();
    int numNearbyStudents();

}
