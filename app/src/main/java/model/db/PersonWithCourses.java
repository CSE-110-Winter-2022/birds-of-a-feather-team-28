package model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.google.android.gms.nearby.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.IPerson;

public class PersonWithCourses implements IPerson {

    public static final String MSG_DELIMITER = ",";

    @Embedded
    public Person person;

    @Relation(parentColumn = "person_id", entityColumn = "person_id", entity = CourseEntry.class)
    public List<CourseEntry> courseEntries;
    //projection = {"text"}

    @Override
    public UUID getId() {
        return this.person.personId;
    }

    @Override
    public String getName(){
        return this.person.name;
    }

    @Override
    public String getProfilePic(){
        return this.person.profilePic;
    }

    @Override
    public List<CourseEntry> getCourses() {
        return this.courseEntries;
    }

    public int getNumCourses() { return this.courseEntries.size(); }

    public List<String> getCoursesString() {
        List<String> courseStrings = new ArrayList<>();
        for (CourseEntry courseEntry : getCourses()) {
            courseStrings.add(courseEntry.toString());
        }
        return courseStrings;
    }

    public Message toMessage() {
        String numCourses = String.valueOf(this.getNumCourses());
        String name = getName();
        String uuid = getId().toString();
        String profilePic = getProfilePic();
        String coursesStr = "";

        for (int i = 0; i < getNumCourses(); i++) {
            coursesStr += ((CourseEntry) courseEntries.get(i)).toMsgString();
        }

        String msg = numCourses + MSG_DELIMITER + uuid + MSG_DELIMITER + name + MSG_DELIMITER + profilePic + MSG_DELIMITER + coursesStr;
        return new Message(msg.getBytes(StandardCharsets.UTF_8));
    }

    public boolean sameUUID(PersonWithCourses pwc) {
        return getId().toString().equals(pwc.getId().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PersonWithCourses) {
            PersonWithCourses pwc = (PersonWithCourses) o;
            return sameUUID(pwc);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
