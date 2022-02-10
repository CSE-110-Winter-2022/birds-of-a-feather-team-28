package com.example.bof_group_28.utility;

import java.util.ArrayList;
import java.util.List;

public class DummyStudent implements Person {

    List<CourseEntry> courses;

    String name;
    public DummyStudent(String name) {
        this.name = name;
        courses = new ArrayList<>();
    }

    @Override
    public List<CourseEntry> getCourses() {
        return courses;
    }

    @Override
    public String getName() {
        return name;
    }
}
