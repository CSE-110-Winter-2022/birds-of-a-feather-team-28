package com.example.bof_group_28.utility.classes;

import com.example.bof_group_28.utility.interfaces.Person;
import com.example.bof_group_28.utility.interfaces.StudentFinder;

import java.util.List;

public class DummyStudentFinder implements StudentFinder {

    List<Person> nearbyStudents;

    public DummyStudentFinder(List<Person> nearbyStudents){
        this.nearbyStudents = nearbyStudents;
    }

    @Override
    public List<Person> returnNearbyStudents() {
        return this.nearbyStudents;
    }

    @Override
    public void updateNearbyStudents() {
    }

    @Override
    public int numNearbyStudents() {
        return this.nearbyStudents.size();
    }

}
