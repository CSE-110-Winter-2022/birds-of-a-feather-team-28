package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.internal.inject.InstrumentationContext;
import androidx.test.internal.runner.InstrumentationConnection;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bof_group_28.utility.classes.DummyStudentFinder;
import com.example.bof_group_28.utility.classes.NearbyStudentsFinder;
import com.example.bof_group_28.utility.classes.NearbyStudentsHandler;
import com.google.android.gms.nearby.messages.Message;

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
import model.db.PersonWithCourses;

@RunWith(AndroidJUnit4.class)
public class NearbyStudentHandlerTest {
    private AppDatabase db;
    private UUID id = UUID.randomUUID();
    private UUID id2 = UUID.randomUUID();
    private UUID id3 = UUID.randomUUID();
    private UUID c1 = UUID.randomUUID();
    private UUID c2 = UUID.randomUUID();
    private UUID c3 = UUID.randomUUID();
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        //userDao = db.getUserDao();
    }

    @Test
    public void StudentMessageTest() {
        PersonWithCourses bob = new PersonWithCourses();
        bob.person = new Person(id, "Bob", null);

        List<CourseEntry> courseEntries = new ArrayList<CourseEntry>();
        courseEntries.add(new CourseEntry(c1, id2, "2022", "WINTER", "CSE", "110", "500"));

        bob.courseEntries = courseEntries;

        String correctStr = "823741,5,2022,WINTER,CSE,110,500";
        Message correctMsg = new Message(correctStr.getBytes(StandardCharsets.UTF_8));

        assertEquals(correctMsg, bob.toMessage());
    }

    @Test
    public void OneNearbyStudentTest() {
        PersonWithCourses bob = new PersonWithCourses();
        bob.person = new Person(id, "Bob", null);

        List<CourseEntry> courseEntries = new ArrayList<CourseEntry>();
        courseEntries.add(new CourseEntry(c1, id2, "2022", "WINTER", "CSE", "110", "500"));

        bob.courseEntries = courseEntries;

        List<PersonWithCourses> nearbyStudents = new ArrayList<PersonWithCourses>();
        nearbyStudents.add(bob);

        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        NearbyStudentsFinder nearbyStudentsFinder = new NearbyStudentsFinder(context);
        nearbyStudentsFinder.getMessageListener().onFound(( bob).toMessage());

        assertEquals(nearbyStudents.size(), nearbyStudentsFinder.returnNearbyStudents().size());
    }

    @Test
    public void MultipleNearbyStudentsTest() {
        List<CourseEntry> courseEntries = new ArrayList<CourseEntry>();
        courseEntries.add(new CourseEntry(c1, id, "2022", "WINTER", "CSE", "110", "500"));

        PersonWithCourses bob = new PersonWithCourses();
        bob.person = new Person(id2, "Bob", null);

        PersonWithCourses lily = new PersonWithCourses();
        lily.person = new Person(id3, "Lily", null);

        bob.courseEntries = courseEntries;
        lily.courseEntries = courseEntries;

        List<PersonWithCourses> nearbyStudents = new ArrayList<PersonWithCourses>();
        nearbyStudents.add(bob);
        nearbyStudents.add(lily);

        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        NearbyStudentsFinder nearbyStudentsFinder = new NearbyStudentsFinder(context);
        nearbyStudentsFinder.getMessageListener().onFound((bob).toMessage());
        nearbyStudentsFinder.getMessageListener().onFound((lily).toMessage());

        assertEquals(nearbyStudents.size(), nearbyStudentsFinder.returnNearbyStudents().size());
    }

    @Test
    public void NaiveSharedCoursesTest() {

        Person person = new Person(id, "John Doe", null);
        db.personWithCoursesDAO().insert(person);
        CourseEntry courseEntry = new CourseEntry(c1,id,"2022", "WI", "CSE", "110", "500");
        db.courseEntryDAO().insert(courseEntry);
        courseEntry = new CourseEntry(c2,id,"2022", "WI", "CSE", "101", "500");
        db.courseEntryDAO().insert(courseEntry);

        Person personTwo = new Person(id2, "Jeff Bezos", null);
        db.personWithCoursesDAO().insert(personTwo);
        courseEntry = new CourseEntry(c3,id2,"2022", "WI", "CSE", "110","500");
        db.courseEntryDAO().insert(courseEntry);

        List<PersonWithCourses> fakePeople = new ArrayList<>();
        fakePeople.add(db.personWithCoursesDAO().get(id2));
        //NearbyStudentsHandler handler = new NearbyStudentsHandler(db.personWithCoursesDAO().get(id), new DummyStudentFinder(fakePeople, null));

        //assertTrue(handler.getStudentClassMap().containsKey(db.personWithCoursesDAO().get(2)));
        //assertEquals(handler.getStudentClassMap().get(db.personWithCoursesDAO().get(2)).get(0), courseEntry);
    }
}
