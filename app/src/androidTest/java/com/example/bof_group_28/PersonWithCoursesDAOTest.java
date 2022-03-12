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
import java.util.UUID;

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
    private UUID c1 = UUID.randomUUID();
    private UUID c2 = UUID.randomUUID();
    private UUID c3 = UUID.randomUUID();
    private UUID c4 = UUID.randomUUID();
    private UUID c5 = UUID.randomUUID();
    private UUID c6 = UUID.randomUUID();
    private UUID c7 = UUID.randomUUID();
    private UUID c8 = UUID.randomUUID();
    private UUID id = UUID.randomUUID();
    private UUID id2 = UUID.randomUUID();
    private UUID id3 = UUID.randomUUID();
    private UUID id4 = UUID.randomUUID();



    ArrayList<CourseEntry> courseList;
    ArrayList<Person> personList;

     @Before
     public void createDb() {
     Context context = ApplicationProvider.getApplicationContext();
     db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
     courseList = new ArrayList<CourseEntry>();
     personList = new ArrayList<Person>();

     Person person = new Person(id, "John Doe1", null);
     db.personWithCoursesDAO().insert(person);
     personList.add(person);

     person = new Person(id2, "John Doe2", null);
     db.personWithCoursesDAO().insert(person);
     personList.add(person);

     person = new Person(id3, "John Doe3", null);
     db.personWithCoursesDAO().insert(person);
     personList.add(person);

     person = new Person(id4, "John Doe4", null);
     db.personWithCoursesDAO().insert(person);
     personList.add(person);


     CourseEntry courseEntry = new CourseEntry(c1, id, "2022", "WI22", "CSE", "110", "500");
     db.courseEntryDAO().insert(courseEntry);
     courseList.add(courseEntry);

     courseEntry = new CourseEntry(c2, id2, "2022", "WI22", "CSE", "110", "500");
     db.courseEntryDAO().insert(courseEntry);
     courseList.add(courseEntry);

     courseEntry = new CourseEntry(c3, id2, "2022", "WI22", "MGT", "181", "500");
     db.courseEntryDAO().insert(courseEntry);
     courseList.add(courseEntry);

     courseEntry = new CourseEntry(c4, id3, "2022", "WI22", "CSE", "110", "500");
     db.courseEntryDAO().insert(courseEntry);
     courseList.add(courseEntry);

     courseEntry = new CourseEntry(c5, id, "2022", "WI22", "CSE", "110", "Small (40-75)");
     db.courseEntryDAO().insert(courseEntry);
     courseList.add(courseEntry);

     courseEntry = new CourseEntry(c6, id2, "2022", "WI22", "CSE", "110", "Small (40-75)");
     db.courseEntryDAO().insert(courseEntry);
     courseList.add(courseEntry);

     courseEntry = new CourseEntry(c7, id2, "2022", "WI22", "MGT", "181", "Small (40-75)");
     db.courseEntryDAO().insert(courseEntry);
     courseList.add(courseEntry);

     courseEntry = new CourseEntry(c8, id3, "2022", "WI22", "CSE", "110", "Small (40-75)");
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
     assertEquals(0, db.personWithCoursesDAO().get(id4).getCourses().size());
     }

     @Test
     public void someCoursesGetCourses() {
     assertEquals(4, db.personWithCoursesDAO().get(id2).getCourses().size());
     assertEquals(courseList.get(1), db.personWithCoursesDAO().get(id2).getCourses().get(0));
     }


     @Test
     public void getFromID() {
      assertEquals(personList.get(0).name, db.personWithCoursesDAO().get(id).getName());
      assertEquals(personList.get(1).name, db.personWithCoursesDAO().get(id2).getName());
      assertEquals(personList.get(2).name, db.personWithCoursesDAO().get(id3).getName());
      assertEquals(personList.get(3).name, db.personWithCoursesDAO().get(id4).getName());
     }

    /*@Test
    public void maxIDPersons() {
        assertEquals(4, db.personWithCoursesDAO().maxId());
    }*/
    /**
     @Test
     public void updatePersons() {
     db.personWithCoursesDAO().update(id, "Jane Doe", null);
     assertEquals("Jane Doe", db.personWithCoursesDAO().get(id).getName());

     db.personWithCoursesDAO().update(id, "John Doe1", null);
     assertEquals("John Doe1", db.personWithCoursesDAO().get(id).getName());
     }

     @Test
     public void insertPersons() {
     Person person = new Person(id, "John Doe10", null);

     db.personWithCoursesDAO().insert(person);
     assertEquals(5, db.personWithCoursesDAO().count());
     //assertEquals(10, db.personWithCoursesDAO().maxId());
     assertEquals("John Doe10", db.personWithCoursesDAO().get(id).getName());

     person = new Person(id2, "John Doe11", null);

     db.personWithCoursesDAO().insert(person);
     assertEquals(6, db.personWithCoursesDAO().count());
     //assertEquals(11, db.personWithCoursesDAO().maxId());
     assertEquals("John Doe11", db.personWithCoursesDAO().get(id2).getName());
     }

     @Test
     public void deleteCoursesFromPersons() {
     db.personWithCoursesDAO().deletePersonCourses(id3);
     assertEquals(1, db.personWithCoursesDAO().get(id).getCourses().size());
     assertEquals(2, db.personWithCoursesDAO().get(id2).getCourses().size());
     assertEquals(0, db.personWithCoursesDAO().get(id3).getCourses().size());
     }

     @Test
     public void deletePersons() {
     db.personWithCoursesDAO().deletePerson(id4);
     personList.remove(3);
     assertEquals(personList.size(), db.personWithCoursesDAO().count());
     assertEquals(personList.get(2).name, db.personWithCoursesDAO().get(id3).getName());
     }

     /**
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
     }*/

}