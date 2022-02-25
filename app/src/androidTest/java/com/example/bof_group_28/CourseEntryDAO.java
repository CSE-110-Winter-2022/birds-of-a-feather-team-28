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

        CourseEntry courseEntry = new CourseEntry(1, 1, "2022", "WI22", "CSE", "101", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(2, 1, "2022", "WI22", "CSE", "102", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(3, 1, "2022", "WI22", "MGT", "103", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(4, 1, "2022", "WI22", "CSE", "104", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);
    }

    @Test
    public void countCourses() {
        assertEquals(4, db.courseEntryDAO().count());
    }

    @Test
    public void maxCourseID() {
        assertEquals(4, db.courseEntryDAO().maxId());
    }

    @Test
    public void getCourseFromID() {
        for (int i = 0; i < courseList.size(); i++) {
            assertEquals(courseList.get(i), db.courseEntryDAO().get(i+1));
        }
    }

    @Test
    public void updateCourses() {
        db.courseEntryDAO().update(1,1,"MGT", "111", "2022", "WI22", "Tiny");
        assertEquals("MGT", db.courseEntryDAO().get(1).subject);
        assertEquals("111", db.courseEntryDAO().get(1).courseNum);

        db.courseEntryDAO().update(1,1,"CSE", "101", "2022", "WI22", "Tiny");
        assertEquals("CSE", db.courseEntryDAO().get(1).subject);
        assertEquals("101", db.courseEntryDAO().get(1).courseNum);

    }

    @Test
    public void deleteCourses() {
        db.courseEntryDAO().deleteCourse(4);
        assertEquals(3, db.courseEntryDAO().maxId());
        db.courseEntryDAO().deleteCourse(3);
        assertEquals(2, db.courseEntryDAO().maxId());
        assertEquals(2, db.courseEntryDAO().count());
        db.courseEntryDAO().deleteCourse(1);
        assertEquals(2, db.courseEntryDAO().maxId());
        assertEquals(1, db.courseEntryDAO().count());
    }

    @Test
    public void insertCourses() {
        CourseEntry courseEntry = new CourseEntry(9, 1, "2022", "WI22", "WCWP", "109", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        assertEquals("WCWP", db.courseEntryDAO().get(9).subject);
        assertEquals("109", db.courseEntryDAO().get(9).courseNum);
        assertEquals(9, db.courseEntryDAO().maxId());
        assertEquals(5, db.courseEntryDAO().count());
    }

}
