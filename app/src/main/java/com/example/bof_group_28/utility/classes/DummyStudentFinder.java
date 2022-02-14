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
        //FIXME: get nearby students from bluetooth nearby student service as parseable data
        List<String> nearbyStudentsStrings = new ArrayList<>();
        List<Person> personList = new ArrayList<>();
        for (String studentString : nearbyStudentsStrings) {
            //FIXME: properly parse bluetooth
            String name = "TestStudent";
            byte[] profilePic = null;
            databaseHandler.insertPerson(name, profilePic);
            /*for (String courseString : nearbyCourseStrings) {
                //FIXME: properly parse bluetooth

                List<String> years = new ArrayList<>();
                List<String> quarters = new ArrayList<>();
                List<String> subjects = new ArrayList<>();
                List<String> courseNums = new ArrayList<>();

                databaseHandler.insertPerson(name, profilePic);
            }*/
        }
    }

    @Override
    public int numNearbyStudents() {
        return this.nearbyStudents.size();
    }
}
