package model;

import android.media.Image;

import java.util.List;
import model.db.CourseEntry;

public interface IPerson {
    int getId();
    String getName();
    byte[] getProfilePic();
    List<CourseEntry> getCourses();

}
