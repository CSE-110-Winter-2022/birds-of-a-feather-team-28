package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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

    public boolean listsEqualNoOrder(List<?> l1, List<?> l2) {
        return (l1.containsAll(l2) && l2.containsAll(l1));
        // Note: there cannot be duplicate classes, so this method works
    }

    @Test
    public void testListEqualNoOrder() {
        List<Integer> intList1 = new ArrayList<>();
        intList1.add(1);
        intList1.add(2);
        intList1.add(3);

        List<Integer> intList2 = new ArrayList<>();
        intList2.add(3);
        intList2.add(2);
        intList2.add(1);

        assertTrue(listsEqualNoOrder(intList1, intList2));
        intList2.add(4);
        assertFalse(listsEqualNoOrder(intList1, intList2));
        System.out.println("Tested listsEqualNoOrder method");
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
        System.out.println("Tested Naive student input");

    }

    @Test
    public void testNoMatchingStudents() {
        Person user = new DummyStudent("David");

        user.getCourses().add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        user.getCourses().add(new DummyCourse("2020", "FALL", "HUM", "100"));
        user.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));

        Person studentOne = new DummyStudent("Bob");
        studentOne.getCourses().add(new DummyCourse("2021", "WINTER", "RANDOM", "27"));
        studentOne.getCourses().add(new DummyCourse("2022", "WINTER", "RANDOM", "101"));
        studentOne.getCourses().add(new DummyCourse("2025", "SUMMER", "RANDOM", "25"));

        Person studentTwo = new DummyStudent("Sally");
        studentTwo.getCourses().add(new DummyCourse("2021", "WINTER", "RANDOM", "27"));
        studentTwo.getCourses().add(new DummyCourse("2020", "WINTER", "RANDOM", "101"));
        studentTwo.getCourses().add(new DummyCourse("2025", "SUMMER", "RANDOM", "25"));

        Person studentThree = new DummyStudent("Terrence");
        studentThree.getCourses().add(new DummyCourse("2022", "WINTER", "RANDOM", "101"));
        studentThree.getCourses().add(new DummyCourse("2020", "FALL", "RANDOM", "100"));

        List<Person> students = new ArrayList<>();
        students.add(studentOne);
        students.add(studentTwo);
        students.add(studentThree);

        BirdsOfAFeatherHandleNearbyStudents handler = new BirdsOfAFeatherHandleNearbyStudents(user, students);
        assertFalse(handler.getStudentClassMap().containsKey(studentOne));
        assertFalse(handler.getStudentClassMap().containsKey(studentTwo));
        assertFalse(handler.getStudentClassMap().containsKey(studentThree));
        assertTrue(handler.getStudentClassMap().isEmpty());

        System.out.println("Tested matching students");

    }

    @Test
    public void testSomeMatchingStudents() {
        Person user = new DummyStudent("David");

        user.getCourses().add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        user.getCourses().add(new DummyCourse("2020", "FALL", "HUM", "100"));
        user.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));

        Person studentOne = new DummyStudent("Bob");
        studentOne.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));
        studentOne.getCourses().add(new DummyCourse("2022", "WINTER", "RANDOM", "101"));

        Person studentTwo = new DummyStudent("Sally");
        studentTwo.getCourses().add(new DummyCourse("2021", "WINTER", "RANDOM", "27"));
        studentTwo.getCourses().add(new DummyCourse("2020", "WINTER", "RANDOM", "101"));

        Person studentThree = new DummyStudent("Terrence");
        studentThree.getCourses().add(new DummyCourse("2022", "WINTER", "RANDOM", "101"));

        List<Person> students = new ArrayList<>();
        students.add(studentOne);
        students.add(studentTwo);
        students.add(studentThree);

        BirdsOfAFeatherHandleNearbyStudents handler = new BirdsOfAFeatherHandleNearbyStudents(user, students);

        assertFalse(handler.getStudentClassMap().isEmpty());

        assertTrue(handler.getStudentClassMap().containsKey(studentOne));

        List<CourseEntry> studentOneShared = new ArrayList<>();
        studentOneShared.add(new DummyCourse("2021", "WINTER", "PHIL", "27"));
        assertTrue(listsEqualNoOrder(studentOneShared, handler.getStudentClassMap().get(studentOne)));

        assertFalse(handler.getStudentClassMap().containsKey(studentTwo));
        assertFalse(handler.getStudentClassMap().containsKey(studentThree));
        System.out.println("Tested some matching student input");


    }

    @Test
    public void testSameNamedStudents() {
        Person user = new DummyStudent("David");

        user.getCourses().add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        user.getCourses().add(new DummyCourse("2020", "FALL", "HUM", "100"));
        user.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));

        Person studentOne = new DummyStudent("Bob");
        studentOne.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));
        studentOne.getCourses().add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        studentOne.getCourses().add(new DummyCourse("2025", "SUMMER", "BIO", "25"));

        Person studentTwo = new DummyStudent("Bob");
        studentTwo.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));
        studentTwo.getCourses().add(new DummyCourse("2020", "WINTER", "HUM", "101"));
        studentTwo.getCourses().add(new DummyCourse("2025", "SUMMER", "BIO", "25"));

        Person studentThree = new DummyStudent("Bob");
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

        System.out.println("Tested same name input");

    }

    @Test
    public void testVerySimilarCourses() {
        Person user = new DummyStudent("David");

        user.getCourses().add(new DummyCourse("2022", "WINTER", "CSE", "101"));
        user.getCourses().add(new DummyCourse("2020", "FALL", "HUM", "100"));
        user.getCourses().add(new DummyCourse("2021", "WINTER", "PHIL", "27"));
        user.getCourses().add(new DummyCourse("2023", "WINTER", "PHIL", "28"));

        Person studentOne = new DummyStudent("Bob");
        studentOne.getCourses().add(new DummyCourse("2022", "WINTER", "CSE", "100"));
        studentOne.getCourses().add(new DummyCourse("2020", "FALL", "HUN", "100"));
        studentOne.getCourses().add(new DummyCourse("2021", "WINTE", "PHIL", "27"));
        studentOne.getCourses().add(new DummyCourse("2024", "WINTER", "PHIL", "28"));

        List<Person> students = new ArrayList<>();
        students.add(studentOne);

        BirdsOfAFeatherHandleNearbyStudents handler = new BirdsOfAFeatherHandleNearbyStudents(user, students);

        assertFalse(handler.getStudentClassMap().containsKey(studentOne));
        System.out.println("Tested similar courses input");

    }
}
