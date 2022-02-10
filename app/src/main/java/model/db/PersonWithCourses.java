package model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import model.IPerson;

public class PersonWithCourses implements IPerson {
    @Embedded
    public Person person;

    @Relation(parentColumn = "person_id", entityColumn = "person_id", entity = CourseEntry.class)
    public List<CourseEntry> courseEntries;
    //projection = {"text"}

    @Override
    public int getId() {
        return this.person.personId;
    }

    @Override
    public String getName(){
        return this.person.name;
    }

    @Override
    public byte[] getProfilePic(){
        return this.person.profilePic;
    }

    @Override
    public List<CourseEntry> getCourses() {
        return this.courseEntries;
    }


}
