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

public class CourseEntryTests {
    //Empty Course insertion edge cases taken care of during add course insertion
    @Test
    public void createCourseTest() {
        CourseEntry courseEntry = new CourseEntry(2,1,"2022", "WI22", "CSE", "110", "Tiny");
        assertEquals(2,courseEntry.courseId);
        assertEquals(1,courseEntry.personId);
        assertEquals("2022", courseEntry.year);
        assertEquals("WI22", courseEntry.quarter);
        assertEquals("CSE", courseEntry.subject);
        assertEquals("110", courseEntry.courseNum);
        assertEquals("Tiny", courseEntry.size);
    }

}