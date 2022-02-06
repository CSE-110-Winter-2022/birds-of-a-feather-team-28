package com.example.bof_group_28;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DummyStudent implements Person {

    String name;
    public DummyStudent(String name) {
        this.name = name;
    }

    @Override
    public ArrayList<CourseEntry> getCourses() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
