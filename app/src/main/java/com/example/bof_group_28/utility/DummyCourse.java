package com.example.bof_group_28.utility;

import androidx.annotation.NonNull;

<<<<<<< Updated upstream:app/src/main/java/com/example/bof_group_28/utility/DummyCourse.java
=======
import com.example.bof_group_28.utility.interfaces.CourseEntry;
import com.google.android.gms.nearby.messages.Message;

import java.nio.charset.StandardCharsets;

>>>>>>> Stashed changes:app/src/main/java/com/example/bof_group_28/utility/classes/DummyCourse.java
public class DummyCourse implements CourseEntry {

    public DummyCourse(String year, String quarter, String subject, String courseNum) {
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNum = courseNum;
    }

    String year;
    String quarter;
    String subject;
    String courseNum;

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public String getQuarter() {
        return quarter;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getCourseNum() {
        return courseNum;
    }

    @Override
    public void setYear(String year) {

    }

    @Override
    public void setQuarter(String quarter) {

    }

    @Override
    public void setSubject(String subject) {

    }

    @Override
    public void setCourseNum(String courseNum) {

    }

    @NonNull
    @Override
    public String toString() {
        return subject + " "  + courseNum + " for " + quarter + " of " + year;
    }

    @NonNull
    @Override
    public String toMessageString() {
        return year + "," + quarter + "," + subject + "," + courseNum + ",";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DummyCourse) {
            DummyCourse otherCourse = (DummyCourse) o;
            if (otherCourse.getCourseNum().equals(getCourseNum())
                    && otherCourse.getQuarter().equals(getQuarter())
                    && otherCourse.getSubject().equals(getSubject())
                    && otherCourse.getYear().equals(getYear())) {
                return true;
            }
        }
        return false;
    }
}
