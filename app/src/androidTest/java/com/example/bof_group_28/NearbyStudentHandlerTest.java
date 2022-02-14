package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bof_group_28.utility.classes.DummyStudentFinder;
import com.example.bof_group_28.utility.classes.NearbyStudentsHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import model.IPerson;
import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;

@RunWith(AndroidJUnit4.class)
public class NearbyStudentHandlerTest {
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        //userDao = db.getUserDao();
    }
    /*@Test
    public void NaiveSharedCoursesTest() {

        Person person = new Person(1, "John Doe", null);
        db.personWithCoursesDAO().insert(person);
        CourseEntry courseEntry = new CourseEntry(1,1,"2022", "WI", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);
        courseEntry = new CourseEntry(2,1,"2022", "WI", "CSE", "101");
        db.courseEntryDAO().insert(courseEntry);

        Person personTwo = new Person(2, "Jeff Bezos", null);
        db.personWithCoursesDAO().insert(personTwo);
        courseEntry = new CourseEntry(3,2,"2022", "WI", "CSE", "110");
        db.courseEntryDAO().insert(courseEntry);

        List<PersonWithCourses> fakePeople = new ArrayList<>();
        fakePeople.add(db.personWithCoursesDAO().get(2));
        NearbyStudentsHandler handler = new NearbyStudentsHandler(db.personWithCoursesDAO().get(1), new DummyStudentFinder(fakePeople, null));

        assertTrue(handler.getStudentClassMap().containsKey(db.personWithCoursesDAO().get(2)));
        assertEquals(handler.getStudentClassMap().get(db.personWithCoursesDAO().get(2)).get(0), courseEntry);
    }*/
}
