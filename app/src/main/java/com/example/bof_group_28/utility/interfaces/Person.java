package com.example.bof_group_28.utility.interfaces;

import com.example.bof_group_28.utility.interfaces.CourseEntry;

import java.io.Serializable;
import java.util.List;

public interface Person {

    List<CourseEntry> getCourses();
    String getName();

    void setName(String name);

}
