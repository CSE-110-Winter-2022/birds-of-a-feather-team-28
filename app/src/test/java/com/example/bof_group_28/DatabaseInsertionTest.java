package com.example.bof_group_28;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.CourseEntryDAO;
import model.db.Person;

import static org.junit.Assert.*;

import android.content.Context;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class DatabaseInsertionTest {
    AppDatabase db;

   /*@Before
   public void createDb() {
       Context context = ApplicationProvider.getApplicationContext();
       db = AppDatabase.singleton(context.getApplicationContext());
       db.clearAllTables();
   }*/

    @Test
    public void createPersonTest() {
        Person person = new Person(1, "John Doe");
        assertEquals(1,person.personId);
        assertEquals("John Doe",person.name);
    }

    @Test
    public void createCourseTest() {
        CourseEntry courseEntry = new CourseEntry(2,1,"2022", "WI22", "CSE", "110");
        assertEquals(2,courseEntry.courseId);
        assertEquals(1,courseEntry.personId);
        assertEquals("2022", courseEntry.year);
        assertEquals("WI22", courseEntry.quarter);
        assertEquals("CSE", courseEntry.subject);
        assertEquals("110", courseEntry.courseNum);
    }

   /* @Test
    public void SingleInsertionTest() {

        Person person = new Person(1, "John Doe");
        db.personWithCoursesDAO().insert(person);
        CourseEntry courseEntry = new CourseEntry(1,1,"2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseEntry = new CourseEntry(2,1,"2022", "WI22", "CSE", "101");
        db.courseEntryDAO().insert(courseEntry);

        assertEquals(2, db.courseEntryDAO().maxId());
    }*/

   /* @Test
    public void DoubleInsertionTest() {

        Person person = new Person(1, "John Doe");
        db.personWithCoursesDAO().insert(person);
        person = new Person(2, "Jane Doe");
        db.personWithCoursesDAO().insert(person);

        CourseEntry courseEntry = new CourseEntry(3, 3, "2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseEntry = new CourseEntry(4, 4, "2022", "WI22", "MGT", "181");
        db.courseEntryDAO().insert(courseEntry);

        assertEquals(2, db.personWithCoursesDAO().count());
    }*/

}