package com.example.bof_group_28;

import org.junit.After;
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
import model.db.PersonWithCourses;

import static org.junit.Assert.*;

import android.content.Context;

import java.io.IOException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class DatabaseInsertionTest {
    //AppDatabase db;
    //private UserDao userDao;
    //private TestDatabase db;

    /*@Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        //userDao = db.getUserDao();
    }*/

    /*@Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.singleton(context.getApplicationContext());
        db.clearAllTables();
    }*/

    @After
    public void closeDb() throws IOException {
        //db.close();
    }

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

    /*@Test
    public void SingleInsertionTest() {

        Person person = new Person(1, "John Doe");
        db.personWithCoursesDAO().insert(person);
        CourseEntry courseEntry = new CourseEntry(1,1,"2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseEntry = new CourseEntry(2,1,"2022", "WI22", "CSE", "101");
        db.courseEntryDAO().insert(courseEntry);

        assertEquals(2, db.courseEntryDAO().maxId());
    }*/


}