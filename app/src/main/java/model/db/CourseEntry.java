package model.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

@Entity(tableName = "courseentries")
public class CourseEntry {
    @PrimaryKey
    @ColumnInfo(name = "course_id")
    public int courseId;

    @ColumnInfo(name = "person_id")
    public int personId;

    @ColumnInfo(name = "year")
    public String year;

    @ColumnInfo(name = "quarter")
    public String quarter;

    @ColumnInfo(name = "subject")
    public String subject;

    @ColumnInfo(name = "course_num")
    public String courseNum;

    public CourseEntry(int courseId, int personId, String year,
                       String quarter, String subject, String courseNum) {
        this.courseId = courseId;
        this.personId = personId;
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNum = courseNum;
    }

    @NonNull
    @Override
    public String toString() {
        return subject + " "  + courseNum + " for " + quarter + " of " + year;
    }

    @Override
    public boolean equals(Object otherCourseObject) {
        CourseEntry otherCourse;
        if (otherCourseObject instanceof CourseEntry) {
            otherCourse = (CourseEntry) otherCourseObject;
        } else {
            return false;
        }
        return (year.equals(otherCourse.year)
                && quarter.equals(otherCourse.quarter)
                && subject.equals(otherCourse.subject)
                && courseNum.equals(otherCourse.courseNum));
    }
}