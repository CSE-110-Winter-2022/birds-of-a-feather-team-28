package com.example.bof_group_28.utility.classes;

import com.example.bof_group_28.utility.interfaces.CourseEntry;
import com.example.bof_group_28.utility.interfaces.Person;
import com.google.android.gms.nearby.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DummyStudent implements Person {

    public static final String MSG_DELIMITER = ",";

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

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getNumCourses() { return courses.size(); }

    public void addCourse(CourseEntry courseEntry){
        this.courses.add(courseEntry);
    }

    public Message toMessage() {
        String numCourses = String.valueOf(this.getNumCourses());
        String name = getName();
        String coursesStr = "";

        for (int i = 0; i < getNumCourses(); i++) {
            coursesStr += ((DummyCourse) courses.get(i)).toMsgString();
        }

        String msg = numCourses + MSG_DELIMITER + name + MSG_DELIMITER + coursesStr;
        return new Message(msg.getBytes(StandardCharsets.UTF_8));
    }
}
