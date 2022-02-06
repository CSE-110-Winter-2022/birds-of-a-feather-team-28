package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.bof_group_28.utility.BirdsOfAFeatherHandleNearbyStudents;
import com.example.bof_group_28.utility.CourseEntry;
import com.example.bof_group_28.utility.DummyCourse;
import com.example.bof_group_28.utility.DummyStudent;
import com.example.bof_group_28.utility.Person;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BirdsOfAFeatherHandlerTests {

    // ideas: test no matches (should not show up) [no matches at all vs some matches].
    // test like all matching but one field for all fields. test same names.

    public boolean listsEqualNoOrder(List<?> l1, List<?> l2) {
        return (l1.containsAll(l2) && l2.containsAll(l1));
    }

    @Test
    public void testNaiveStudentInput() {
        Person user = new DummyStudent("David");

        user.getCourses().add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        user.getCourses().add(new DummyCourse("2020", "FALL", "HUM", "100"));
        user.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));

        Person studentOne = new DummyStudent("Bob");
        studentOne.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));
        studentOne.getCourses().add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        studentOne.getCourses().add(new DummyCourse("2025", "SUMMER", "BIO", "25"));

        Person studentTwo = new DummyStudent("Sally");
        studentTwo.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));
        studentTwo.getCourses().add(new DummyCourse("2020", "WINTER", "HUM", "101"));
        studentTwo.getCourses().add(new DummyCourse("2025", "SUMMER", "BIO", "25"));

        Person studentThree = new DummyStudent("Terrence");
        studentThree.getCourses().add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        studentThree.getCourses().add(new DummyCourse("2020", "FALL", "HUM", "100"));

        List<Person> students = new ArrayList<>();
        students.add(studentOne);
        students.add(studentTwo);
        students.add(studentThree);

        BirdsOfAFeatherHandleNearbyStudents handler = new BirdsOfAFeatherHandleNearbyStudents(user, students);

        List<CourseEntry> studentOneShared = new ArrayList<>();
        studentOneShared.add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        studentOneShared.add(new DummyCourse("2021", "WINTER", "PHIL", "27"));
        assertTrue(listsEqualNoOrder(studentOneShared, handler.getStudentClassMap().get(studentOne)));

        List<CourseEntry> studentTwoShared = new ArrayList<>();
        studentTwoShared.add(new DummyCourse("2021", "WINTER", "PHIL", "27"));
        assertTrue(listsEqualNoOrder(studentTwoShared, handler.getStudentClassMap().get(studentTwo)));

        List<CourseEntry> studentThreeShared = new ArrayList<>();
        studentThreeShared.add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        studentThreeShared.add(new DummyCourse("2020", "FALL", "HUM", "100"));
        assertTrue(listsEqualNoOrder(studentThreeShared, handler.getStudentClassMap().get(studentThree)));

    }
}
