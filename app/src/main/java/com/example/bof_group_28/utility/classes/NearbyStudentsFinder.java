package com.example.bof_group_28.utility.classes;

import com.example.bof_group_28.utility.interfaces.Person;
import com.example.bof_group_28.utility.interfaces.StudentFinder;

import java.util.ArrayList;
import java.util.List;

public class NearbyStudentsFinder implements StudentFinder {

    List<Person> nearbyStudents;

    NearbyStudentsFinder(){
        this.nearbyStudents = new ArrayList<Person>();
    }

    @Override
    public List<Person> returnNearbyStudents() {
        return nearbyStudents;
    }

    @Override
    public void updateNearbyStudents() {
        //implement in future
    }

    @Override
    public int numNearbyStudents() {
        return nearbyStudents.size();
    }

}
