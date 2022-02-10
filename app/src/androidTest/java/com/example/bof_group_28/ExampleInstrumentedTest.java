package com.example.bof_group_28;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import model.IPerson;
import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.CourseEntryDAO;
import model.db.Person;
import model.db.PersonWithCourses;

import static org.junit.Assert.*;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    /*@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.bof_group_28", appContext.getPackageName());
    }*/

    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        //userDao = db.getUserDao();
    }
    @Test
    public void SinglePersonInsertionTest() {

        Person person = new Person(1, "John Doe");
        db.personWithCoursesDAO().insert(person);
        CourseEntry courseEntry = new CourseEntry(1,1,"2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseEntry = new CourseEntry(2,1,"2022", "WI22", "CSE", "101");
        db.courseEntryDAO().insert(courseEntry);

        assertEquals(2, db.courseEntryDAO().maxId());

        List<? extends IPerson> persons = db.personWithCoursesDAO().getAll();
        assertEquals("John Doe", persons.get(0).getName());
        assertEquals("CSE", persons.get(0).getCourses().get(0).subject);
        assertEquals("110", persons.get(0).getCourses().get(0).courseNum);
        assertEquals("CSE", persons.get(0).getCourses().get(1).subject);
        assertEquals("101", persons.get(0).getCourses().get(1).courseNum);
    }

    @Test
    public void DoublePersonInsertionTest() {

        Person person = new Person(1, "John Doe");
        db.personWithCoursesDAO().insert(person);
        person = new Person(2, "Jane Doe");
        db.personWithCoursesDAO().insert(person);

        CourseEntry courseEntry = new CourseEntry(3, 1, "2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseEntry = new CourseEntry(4, 2, "2022", "WI22", "MGT", "181");
        db.courseEntryDAO().insert(courseEntry);

        assertEquals(2, db.personWithCoursesDAO().count());

        List<? extends IPerson> persons = db.personWithCoursesDAO().getAll();
        assertEquals("John Doe", persons.get(0).getName());
        assertEquals("CSE", persons.get(0).getCourses().get(0).subject);
        assertEquals("Jane Doe", persons.get(1).getName());
        assertEquals("MGT", persons.get(1).getCourses().get(0).subject);
    }

}