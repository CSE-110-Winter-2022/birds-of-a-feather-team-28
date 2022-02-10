package com.example.bof_group_28.utility.classes;

import android.os.Message;

import androidx.annotation.NonNull;

import com.example.bof_group_28.utility.interfaces.CourseEntry;

import java.nio.charset.StandardCharsets;

public class DummyCourse implements CourseEntry {

    public static final String MSG_DELIMITER = ",";

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
    public void setYear(String year) { this.year = year; }

    @Override
    public void setQuarter(String quarter) { this.quarter = quarter; }

    @Override
    public void setSubject(String subject) { this.subject = subject; }

    @Override
    public void setCourseNum(String courseNum) { this.courseNum = courseNum; }

    @NonNull
    @Override
    public String toString() {
        return subject + " "  + courseNum + " for " + quarter + " of " + year;
    }

    @NonNull
    @Override
    public String toMsgString() {
        return year + MSG_DELIMITER + quarter + MSG_DELIMITER + subject + MSG_DELIMITER + courseNum + MSG_DELIMITER;
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
