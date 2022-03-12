package model.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import java.util.UUID;

@Dao
public interface CourseEntryDAO {
    @Transaction
    @Query("SELECT * FROM courseentries")
    List<CourseEntry> getAll();

    @Transaction
    @Query("SELECT * FROM courseentries where person_id=:personId")
    List<CourseEntry> getForPerson(UUID personId);

    @Query("SELECT * FROM courseentries WHERE course_id=:courseId")
    CourseEntry get(UUID courseId);

    @Query("SELECT COUNT(*) from courseentries")
    int count();

    @Query("UPDATE courseentries SET person_id = :personId, subject = :newSubject, course_num = :newCourseNum, year = :newYear, quarter = :newQuarter, size = :newSize WHERE course_id = :id")
    void update(UUID id, UUID personId, String newSubject, String newCourseNum, String newYear, String newQuarter, String newSize);

    @Query("DELETE FROM courseentries WHERE course_id = :id")
    void deleteCourse(UUID id);

    @Query("DELETE FROM courseentries WHERE person_id = :id")
    void deleteCourses(UUID id);

    @Insert
    void insert(CourseEntry Course);

    //@Delete
    //void delete(CourseEntry Course);
}
