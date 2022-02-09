package com.example.bof_group_28.utility;

public interface CourseEntry {

    String getYear();
    String getQuarter();
    String getSubject();
    String getCourseNum();

    String toMessageString();

    void setYear(String year);
    void setQuarter(String quarter);
    void setSubject(String subject);
    void setCourseNum(String courseNum);
}
