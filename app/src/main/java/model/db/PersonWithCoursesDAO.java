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

    @Query("SELECT COUNT(*) FROM persons")
    int count();

    @Query("SELECT MAX(person_id) FROM persons")
    int maxId();

    @Query("UPDATE persons SET name = :personName, profile_pic = :profilePic WHERE person_id = :id")
    void update(int id, String personName, byte[] profilePic);

    //Only use after person's courses have been deleted
    @Query("DELETE FROM persons WHERE person_id != 1")
    void deleteNonUserPersons();

    @Query("DELETE FROM courseentries WHERE person_id != 1")
    void deleteNonUserCourses();

    @Query("DELETE FROM persons WHERE person_id = :id")
    void deletePerson(int id);

    @Query("DELETE FROM courseentries WHERE person_id = :id")
    void deletePersonCourses(int id);

    @Insert
    void insert(Person person);

    //@Delete
    //void delete(Person person);
}