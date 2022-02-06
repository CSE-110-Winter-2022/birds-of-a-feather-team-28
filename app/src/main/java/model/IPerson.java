package model;

import java.util.List;
import model.db.CourseEntry;

public interface IPerson {
    int getId();
    String getName();
    List<CourseEntry> getCourses();
    //Image getProfilePic();

    //void addCourse(CourseEntry courseToAdd);
    //void setName(String name);
    //void setProfilePic(Image profilePic);

    //git test test test
}
