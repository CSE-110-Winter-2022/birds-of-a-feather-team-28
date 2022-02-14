package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import model.IPerson;
import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PersonWithCoursesDAOTest {

    private AppDatabase db;
    ArrayList<CourseEntry> courseList;
    ArrayList<Person> personList;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        courseList = new ArrayList<CourseEntry>();
        personList = new ArrayList<Person>();

        Person person = new Person(1, "John Doe1", null);
        db.personWithCoursesDAO().insert(person);
        personList.add(person);

        person = new Person(2, "John Doe2", null);
        db.personWithCoursesDAO().insert(person);
        personList.add(person);

        person = new Person(3, "John Doe3", null);
        db.personWithCoursesDAO().insert(person);
        personList.add(person);

        person = new Person(4, "John Doe4", null);
        db.personWithCoursesDAO().insert(person);
        personList.add(person);

        CourseEntry courseEntry = new CourseEntry(1, 1, "2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(2, 2, "2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(3, 2, "2022", "WI22", "MGT", "181");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(4, 3, "2022", "WI22", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);
    }

    @Test
    public void countPersons() {
        assertEquals(4, db.personWithCoursesDAO().count());
    }

    @Test
    public void getAll() {
        assertEquals(personList.size(), db.personWithCoursesDAO().getAll().size());
        for (int i = 0; i < 4; i++) {
            assertEquals(personList.get(i).name, db.personWithCoursesDAO().getAll().get(i).getName());
        }
    }

    @Test
    public void noCoursesGetCourses() {
        assertEquals(0, db.personWithCoursesDAO().get(4).getCourses().size());
    }

    @Test
    public void someCoursesGetCourses() {
        assertEquals(2, db.personWithCoursesDAO().get(2).getCourses().size());
        assertEquals(courseList.get(1), db.personWithCoursesDAO().get(2).getCourses().get(0));
    }

    @Test
    public void getFromID() {
        for (int i = 0; i < 4; i++) {
            //Array list indexed to 0, DB indexed to 1 (add 1)
            assertEquals(personList.get(i).name, db.personWithCoursesDAO().get(i+1).getName());
        }

    }

    @Test
    public void maxIDPersons() {
        assertEquals(4, db.personWithCoursesDAO().maxId());
    }

    @Test
    public void updatePersons() {
        db.personWithCoursesDAO().update(1, "Jane Doe", null);
        assertEquals("Jane Doe", db.personWithCoursesDAO().get(1).getName());

        db.personWithCoursesDAO().update(1, "John Doe1", null);
        assertEquals("John Doe1", db.personWithCoursesDAO().get(1).getName());
    }

    @Test
    public void insertPersons() {
        Person person = new Person(10, "John Doe10", null);

        db.personWithCoursesDAO().insert(person);
        assertEquals(5, db.personWithCoursesDAO().count());
        assertEquals(10, db.personWithCoursesDAO().maxId());
        assertEquals("John Doe10", db.personWithCoursesDAO().get(10).getName());

        person = new Person(11, "John Doe11", null);

        db.personWithCoursesDAO().insert(person);
        assertEquals(6, db.personWithCoursesDAO().count());
        assertEquals(11, db.personWithCoursesDAO().maxId());
        assertEquals("John Doe11", db.personWithCoursesDAO().get(11).getName());
    }

    @Test
    public void deleteCoursesFromPersons() {
        db.personWithCoursesDAO().deletePersonCourses(3);
        assertEquals(1, db.personWithCoursesDAO().get(1).getCourses().size());
        assertEquals(2, db.personWithCoursesDAO().get(2).getCourses().size());
        assertEquals(0, db.personWithCoursesDAO().get(3).getCourses().size());
    }

    @Test
    public void deletePersons() {
        db.personWithCoursesDAO().deletePerson(4);
        personList.remove(3);
        assertEquals(personList.size(), db.personWithCoursesDAO().count());
        assertEquals(personList.get(2).name, db.personWithCoursesDAO().get(3).getName());
    }

    @Test
    public void deleteNonUserCoursesTest() {
        db.personWithCoursesDAO().deleteNonUserCourses();
        assertEquals(1, db.personWithCoursesDAO().get(1).getCourses().size());
        assertEquals(0, db.personWithCoursesDAO().get(2).getCourses().size());
        assertEquals(0, db.personWithCoursesDAO().get(3).getCourses().size());
    }

    @Test
    public void deleteNonUserPersonsTest() {
        db.personWithCoursesDAO().deleteNonUserPersons();
        assertEquals(personList.get(0).name, db.personWithCoursesDAO().get(1).getName());
        assertEquals(1, db.personWithCoursesDAO().count());
    }

}