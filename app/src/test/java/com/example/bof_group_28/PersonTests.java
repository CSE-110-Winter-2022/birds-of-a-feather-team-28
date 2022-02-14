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

public class PersonTests {
    //Empty Person edge cases taken care of during name insertion/saving
    @Test
    public void createPersonTest() {
        Person person = new Person(1, "John Doe", null);
        assertEquals(1,person.personId);
        assertEquals("John Doe",person.name);
    }

}