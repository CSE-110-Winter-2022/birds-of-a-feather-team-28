package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;

public class CourseEntryDAOTest {

    private AppDatabase db;
    ArrayList<CourseEntry> courseList;
    private UUID id = UUID.randomUUID();
    private UUID c1 = UUID.randomUUID();
    private UUID c2 = UUID.randomUUID();
    private UUID c3 = UUID.randomUUID();
    private UUID c4 = UUID.randomUUID();


    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        courseList = new ArrayList<CourseEntry>();


        Person person = new Person(id, "John Doe", null);
        db.personWithCoursesDAO().insert(person);

        CourseEntry courseEntry = new CourseEntry(c1, id, "2022", "WI22", "CSE", "101", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(c2, id, "2022", "WI22", "CSE", "102", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(c3, id, "2022", "WI22", "MGT", "103", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);

        courseEntry = new CourseEntry(c4, id, "2022", "WI22", "CSE", "104", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        courseList.add(courseEntry);
    }

    @Test
    public void countCourses() {
        assertEquals(4, db.courseEntryDAO().count());
    }


    @Test
    public void getCourseFromID() {
        /*for (int i = 0; i < courseList.size(); i++) {
            String classId = "c";
            String tempClassId = classId + i;
            assertEquals(courseList.get(i), db.courseEntryDAO().get(UUID.fromString(tempClassId)));
        }*/
        assertEquals(courseList.get(0), db.courseEntryDAO().get(c1));

        assertEquals(courseList.get(1), db.courseEntryDAO().get(c2));

        assertEquals(courseList.get(2), db.courseEntryDAO().get(c3));

        assertEquals(courseList.get(3), db.courseEntryDAO().get(c4));

    }

    @Test
    public void updateCourses() {
        db.courseEntryDAO().update(c2,id,"MGT", "111", "2022", "WI22", "Tiny");
        assertEquals("MGT", db.courseEntryDAO().get(c2).subject);
        assertEquals("111", db.courseEntryDAO().get(c2).courseNum);

        db.courseEntryDAO().update(c2,id,"CSE", "101", "2022", "WI22", "Tiny");
        assertEquals("CSE", db.courseEntryDAO().get(c2).subject);
        assertEquals("101", db.courseEntryDAO().get(c2).courseNum);

    }

    @Test
    public void deleteCourses() {
        db.courseEntryDAO().deleteCourse(c4);
        assertEquals(3, db.courseEntryDAO().count());
        db.courseEntryDAO().deleteCourse(c3);
        assertEquals(2, db.courseEntryDAO().count());
        db.courseEntryDAO().deleteCourse(c1);
        assertEquals(1, db.courseEntryDAO().count());
    }

    @Test
    public void insertCourses() {
        UUID c9 = UUID.randomUUID();
        CourseEntry courseEntry = new CourseEntry(c9, id, "2022", "WI22", "WCWP", "109", "Tiny");
        db.courseEntryDAO().insert(courseEntry);
        assertEquals("WCWP", db.courseEntryDAO().get(c9).subject);
        assertEquals("109", db.courseEntryDAO().get(c9).courseNum);
        assertEquals(5, db.courseEntryDAO().count());
    }

}
