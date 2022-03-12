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
import java.util.UUID;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class CourseEntryTests {
    //Empty Course insertion edge cases taken care of during add course insertion
    @Test
    public void createCourseTest() {
        UUID courseID = UUID.randomUUID();
        UUID personID = UUID.randomUUID();
        CourseEntry courseEntry = new CourseEntry(courseID,personID,"2022", "WI22", "CSE", "110", "Tiny");
        assertEquals(courseID,courseEntry.courseId);
        assertEquals(personID,courseEntry.personId);
        assertEquals("2022", courseEntry.year);
        assertEquals("WI22", courseEntry.quarter);
        assertEquals("CSE", courseEntry.subject);
        assertEquals("110", courseEntry.courseNum);
        assertEquals("Tiny", courseEntry.size);
    }

}