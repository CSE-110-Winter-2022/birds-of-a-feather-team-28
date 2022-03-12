package model;

import java.util.List;
import java.util.UUID;

import model.db.CourseEntry;

public interface IPerson {
    UUID getId();
    String getName();
    String getProfilePic();
    List<CourseEntry> getCourses();

}
