package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.Message;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.bof_group_28.utility.classes.NearbyStudentsFinder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.StandardCharsets;
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
public class DatabaseInsertionTest {
    /*@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.bof_group_28", appContext.getPackageName());
    }*/

     private AppDatabase db;
     private UUID c1 = UUID.randomUUID();
     private UUID c2 = UUID.randomUUID();
     private UUID c3 = UUID.randomUUID();
     private UUID c4 = UUID.randomUUID();
     private UUID id = UUID.randomUUID();
     private UUID id2 = UUID.randomUUID();

     @Before
     public void createDb() {
          Context context = ApplicationProvider.getApplicationContext();
          db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
          //userDao = db.getUserDao();
     }
     @Test
     public void SinglePersonInsertionTest() {

          Person person = new Person(id, "John Doe", null);
          db.personWithCoursesDAO().insert(person);

          CourseEntry courseEntry = new CourseEntry(c1, id,"2022", "WI22", "CSE", "110", "500");
          db.courseEntryDAO().insert(courseEntry);
          //FIXME: bad quarter naming
          courseEntry = new CourseEntry(c2, id,"2022", "WI22", "CSE", "101", "500");
          db.courseEntryDAO().insert(courseEntry);
          courseEntry = new CourseEntry(c3, id,"2022", "WI22", "CSE", "110", "Small (40-75)");
          db.courseEntryDAO().insert(courseEntry);
          //FIXME: bad quarter naming
          courseEntry = new CourseEntry(c4, id,"2022", "WI22", "CSE", "101", "Small (40-75)");

          db.courseEntryDAO().insert(courseEntry);


          List<? extends IPerson> persons = db.personWithCoursesDAO().getAll();
          assertEquals("John Doe", persons.get(0).getName());
          assertEquals("CSE", persons.get(0).getCourses().get(0).subject);
          assertEquals("110", persons.get(0).getCourses().get(0).courseNum);
          assertEquals("CSE", persons.get(0).getCourses().get(1).subject);
          assertEquals("101", persons.get(0).getCourses().get(1).courseNum);
     }

     @Test
     public void DoublePersonInsertionTest() {

          Person person = new Person(id, "John Doe", null);
          db.personWithCoursesDAO().insert(person);
          person = new Person(id2, "Jane Doe", null);
          db.personWithCoursesDAO().insert(person);


          CourseEntry courseEntry = new CourseEntry(c1, id, "2022", "WI22", "CSE", "110", "500");
          db.courseEntryDAO().insert(courseEntry);
          courseEntry = new CourseEntry(c2, id2, "2022", "WI22", "MGT", "181", "500");
          db.courseEntryDAO().insert(courseEntry);
          courseEntry = new CourseEntry(c3, id, "2022", "WI22", "CSE", "110", "Small (40-75)");
          db.courseEntryDAO().insert(courseEntry);
          courseEntry = new CourseEntry(c4, id2, "2022", "WI22", "MGT", "181", "Small (40-75)");

          db.courseEntryDAO().insert(courseEntry);

          assertEquals(2, db.personWithCoursesDAO().count());

          List<? extends IPerson> persons = db.personWithCoursesDAO().getAll();
          assertEquals("John Doe", persons.get(0).getName());
          assertEquals("CSE", persons.get(0).getCourses().get(0).subject);
          assertEquals("Jane Doe", persons.get(1).getName());
          assertEquals("MGT", persons.get(1).getCourses().get(1).subject);
     }
}