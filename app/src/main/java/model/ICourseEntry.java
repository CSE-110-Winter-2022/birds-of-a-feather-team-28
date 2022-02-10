package model;

public interface ICourseEntry {
    String getYear();
    String getQuarter();
    String getSubject();
    String getCourseNum();

    void setYear(String year);
    void setQuarter(String quarter);
    void setSubject(String subject);
    void setCourseNum(String courseNum);
}