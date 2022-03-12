package model.db;
//person with notes dao

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import java.util.UUID;

@Dao
public interface PersonWithCoursesDAO {
    @Transaction
    @Query("SELECT * FROM persons")
    List<PersonWithCourses> getAll();

    @Query("SELECT * FROM persons WHERE person_id=:id")
    PersonWithCourses get(UUID id);

    @Query("UPDATE persons SET fav_status=:favStatus WHERE person_id=:id")
    void setFavStatus(UUID id, boolean favStatus);

    @Query("SELECT COUNT(*) FROM persons")
    int count();

    @Query("UPDATE persons SET name = :personName, profile_pic = :profilePic WHERE person_id = :id")
    void update(UUID id, String personName, String profilePic);

    @Query("DELETE FROM persons WHERE person_id = :id")
    void deletePerson(UUID id);

    @Query("DELETE FROM courseentries WHERE person_id = :id")
    void deletePersonCourses(UUID id);

    @Insert
    void insert(Person person);

    //@Delete
    //void delete(Person person);
}