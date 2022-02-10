package model.db;
//person with notes dao

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PersonWithCoursesDAO {
    @Transaction
    @Query("SELECT * FROM persons")
    List<PersonWithCourses> getAll();

    @Query("SELECT * FROM persons WHERE person_id=:id")
    PersonWithCourses get(int id);

    @Query("SELECT COUNT(*) from persons")
    int count();

    @Query("SELECT MAX(course_id) from courseentries")
    int maxId();

    @Insert
    void insert(Person person);

    @Delete
    void delete(Person person);
}