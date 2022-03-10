package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
        user = db.personWithCoursesDAO().get(1);
    }

    public PersonWithCourses getUser() {
        return db.personWithCoursesDAO().get(1);
    }

    /**
     * Clear entries from the database
     */
    public void clearNonUserEntries() {
        db.personWithCoursesDAO().deleteNonUserCourses();
        db.personWithCoursesDAO().deleteNonUserPersons();
        updateUser();
    }

    public boolean hasPerson(PersonWithCourses pwc) {
        for (PersonWithCourses p : getAllPeople()) {
            if (p.hasSameInformation(pwc)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Insert a person given name and pfp
     * @param name name
     * @param profilePic pfp
     */
    public void insertPerson(String name, byte[] profilePic) {
        db.personWithCoursesDAO().insert(new Person(db.personWithCoursesDAO().maxId() + 1, name, profilePic));
        updateUser();
    }

    /**
     * Insert a course given course information
     * @param p the person to tie to the course
     * @param years list of years
     * @param quarters list of quarters
     * @param subjects list of subjects
     * @param courseNums list of courseNums
     */
    public void insertCourses(Person p, String[] years, String[] quarters, String[] subjects, String[] courseNums, String[] sizes) {
        for (int i = 0; i < years.length; i++){
            db.courseEntryDAO().insert(new CourseEntry(db.courseEntryDAO().maxId() + 1,
                    p.personId,
                    years[i],
                    quarters[i],
                    subjects[i],
                    courseNums[i],
                    sizes[i]
                    ));
        }
        updateUser();
    }

    public List<PersonWithCourses> getPeople() {
        List<PersonWithCourses> people = new ArrayList<>();
        for (PersonWithCourses pwc : db.personWithCoursesDAO().getAll()) {
            if (pwc.getId() != 1) {
                people.add(pwc);
            }
        }
        return people;
    }

    public List<PersonWithCourses> getAllPeople() {
        return db.personWithCoursesDAO().getAll();
    }

    public List<CourseEntry> getAllCourses() {
        return db.courseEntryDAO().getAll();
    }

    public void saveMemoryUser() {
        db.personWithCoursesDAO().deletePerson(1);
        db.personWithCoursesDAO().insert(user.person);

        // Update db with original user's courses
        List<CourseEntry> courses = user.getCourses();
        clearCourses(user.getId());
        insertCourseList(courses);
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
     * Return a person with courses from a person
     * @param p person
     * @return person with courses
     */
    public PersonWithCourses getPersonWithCourses(Person p) {
        return db.personWithCoursesDAO().get(p.personId);
    }

    /**
     * Get a person's courses from their id
     * @param personId person id
     * @return their list of courses
     */
    public List<CourseEntry> getPersonsCourses(int personId) {
        updateUser();
        return db.personWithCoursesDAO().get(personId).getCourses();
    }

    /**
     * Non UI-safe method to delete courses. DOES NOT UPDATE USER!
     * @param personId the id of person to clear courses
     */
    public void clearCourses(int personId) {
        for (CourseEntry courseEntry : getPersonsCourses(personId)) {
            db.courseEntryDAO().deleteCourse(courseEntry.courseId);
        }
    }

    /**
     * Non UI-safe method to insert a list of courses. DOES NOT UPDATE USER
     * @param courses list of courses
     */
    public void insertCourseList(List<CourseEntry> courses) {
        for (CourseEntry courseEntry : courses) {
            db.courseEntryDAO().insert(courseEntry);
        }
    }

    /**
     * Insert a course entry
     * @param courseEntry the course entry
     */
    public void insertCourse(CourseEntry courseEntry) {
        db.courseEntryDAO().insert(courseEntry);
        updateUser();
    }

    public void updateAndSaveUser(String name, byte[] profilePic) {
        db.personWithCoursesDAO().update(1, name, profilePic);
        updateUser();

        // Save every time user is updated
        sessionManager.saveCurrentSession();
    }

    /**
     * Update an entry
     * @param id person id
     * @param name name
     * @param profilePic pfp
     */
    public void updatePerson(int id, String name, byte[] profilePic) {
        db.personWithCoursesDAO().update(id, name, profilePic);
        updateUser();
    }

    /**
     * Delete a course
     * @param courseId the course id
     */
    public void deleteCourse(int courseId) {
        db.courseEntryDAO().deleteCourse(courseId);
        updateUser();
    }
}
