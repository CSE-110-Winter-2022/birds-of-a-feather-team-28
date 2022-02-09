<<<<<<< Updated upstream:app/src/main/java/com/example/bof_group_28/utility/DummyStudent.java
package com.example.bof_group_28.utility;
=======
package com.example.bof_group_28.utility.classes;

import com.example.bof_group_28.utility.interfaces.CourseEntry;
import com.example.bof_group_28.utility.interfaces.Person;
import com.google.android.gms.nearby.messages.Message;
>>>>>>> Stashed changes:app/src/main/java/com/example/bof_group_28/utility/classes/DummyStudent.java

import java.nio.charset.StandardCharsets;
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
<<<<<<< Updated upstream:app/src/main/java/com/example/bof_group_28/utility/DummyStudent.java
=======

    public void addCourse(CourseEntry courseEntry){
        this.courses.add(courseEntry);
    }

    public int getNumStudentCourses() {
        return courses.size();
    }

    public Message toMessage() {
        String message = "";

        String numCourses = String.valueOf(getNumStudentCourses());
        String name = getName();
        String coursesStr = "";

        for (int i = 0; i < getNumStudentCourses(); i++) {
            coursesStr += courses.get(i).toMessageString();
        }

        message += numCourses + name + coursesStr;

        return new Message(message.getBytes(StandardCharsets.UTF_8));
    }
>>>>>>> Stashed changes:app/src/main/java/com/example/bof_group_28/utility/classes/DummyStudent.java
}
