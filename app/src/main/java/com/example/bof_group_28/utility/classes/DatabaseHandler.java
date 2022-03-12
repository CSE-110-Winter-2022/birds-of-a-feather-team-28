package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;

/**
 * DatabaseHandler handles the AppDatabase and all database creation in BoF activity
 */
public class DatabaseHandler {

    public AppDatabase db;

    /**
     * Construct a database handler
     * @param db the app database
     */
    public DatabaseHandler(AppDatabase db) {
        this.db = db;
    }

    /**
     * Update user from database
     */
    public void updateUser() {
        user = db.personWithCoursesDAO().get(userId);
    }

    public PersonWithCourses getUser() {
        return db.personWithCoursesDAO().get(userId);
    }

    public boolean databaseHasPerson(PersonWithCourses pwc) {
        return db.personWithCoursesDAO().getAll().contains(pwc);
    }

    public boolean databaseHasUUID(UUID id) {
        for (PersonWithCourses p : db.personWithCoursesDAO().getAll()) {
            if (p.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public PersonWithCourses getPersonFromUUID(UUID id) {
        return db.personWithCoursesDAO().get(id);
    }

    /**
     * Insert a person with courses from a person and their courses
     * @param p person
     */
    public void insertPersonWithCourses(Person p) {
        db.personWithCoursesDAO().insert(p);
        updateUser();
    }

    /**
     * Get a person's courses from their id
     * @param personId person id
     * @return their list of courses
     */
    public List<CourseEntry> getPersonsCourses(UUID personId) {
        updateUser();
        return db.personWithCoursesDAO().get(personId).getCourses();
    }

    /**
     * Insert a course entry
     * @param courseEntry the course entry
     */
    public void insertCourse(CourseEntry courseEntry) {
        db.courseEntryDAO().insert(courseEntry);
        updateUser();
    }

    public void updateAndSaveUser(String name, String profilePic) {
        db.personWithCoursesDAO().update(userId, name, profilePic);
        updateUser();
    }

    /**
     * Update an entry
     * @param id person id
     * @param name name
     * @param profilePic pfp
     */
    public void updatePerson(UUID id, String name, String profilePic) {
        db.personWithCoursesDAO().update(id, name, profilePic);
        updateUser();
    }

    /**
     * Delete a course
     * @param courseId the course id
     */
    public void deleteCourse(UUID courseId) {
        db.courseEntryDAO().deleteCourse(courseId);
        updateUser();
    }

    public void deleteCourses(UUID personId) {
        db.courseEntryDAO().deleteCourses(personId);
    }

    public boolean sharesCourses(UUID other) {
        PersonWithCourses otherUser = getPersonFromUUID(other);
        for (CourseEntry course : otherUser.getCourses()) {
            if (user.getCourses().contains(course)) {
                return true;
            }
        }
        return false;
    }

    public int sharedCoursesCount(UUID other) {
        PersonWithCourses otherUser = getPersonFromUUID(other);
        int count = 0;
        for (CourseEntry course : otherUser.getCourses()) {
            if (user.getCourses().contains(course)) {
                count++;
            }
        }
        return count;
    }

    public List<CourseEntry> getSharedCourses(UUID other) {
        List<CourseEntry> sharedCourses = new ArrayList<>();
        for (CourseEntry course : getPersonFromUUID(other).getCourses()) {
            if (user.getCourses().contains(course)) {
                sharedCourses.add(course);
            }
        }
        return sharedCourses;
    }

    public void setFavStatus(UUID id, boolean favStatus) {
        db.personWithCoursesDAO().setFavStatus(id, favStatus);
    }

    public boolean getFavStatus(UUID id) {

        return db.personWithCoursesDAO().get(id).getFavStatus();
    }

}
