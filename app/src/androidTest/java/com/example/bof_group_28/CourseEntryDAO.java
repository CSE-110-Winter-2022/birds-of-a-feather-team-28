package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;

public class CourseEntryDAO {

    private AppDatabase db;
    ArrayList<CourseEntry> courseList;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        courseList = new ArrayList<CourseEntry>();


        Person person = new Person(1, "John Doe", null);
        db.personWithCoursesDAO().insert(person);

        CourseEntry courseEntry = new CourseEntry(1, 1, "2022", "WI22", "CSE", "101");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(2, 1, "2022", "WI22", "CSE", "102");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(3, 1, "2022", "WI22", "MGT", "103");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(4, 1, "2022", "WI22", "CSE", "104");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);
    }

    @Test
    public void countCourses() {
        assertEquals(4, db.courseEntryDAO().count());
    }
}
