package model.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CourseEntryDAO {
    @Transaction
    @Query("SELECT * FROM courseentries where person_id=:personId")
    List<CourseEntry> getForPerson(int personId);

    @Query("SELECT * FROM courseentries WHERE course_id=:courseId")
    CourseEntry get(int courseId);

    @Query("SELECT COUNT(*) from courseentries")
    int count();

    @Query("SELECT MAX(course_id) from courseentries")
    int maxId();

    @Insert
    void insert(CourseEntry Course);

    @Delete
    void delete(CourseEntry Course);
}
