package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.*;

import android.content.Context;

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
     * @param context app context
     */
    public DatabaseHandler(AppDatabase db, Context context) {
        this.db = db;
        runDatabaseSetup(context);
    }

    /**
     * Setup the database for app startup
     */
    private void runDatabaseSetup(Context context) {
        db = AppDatabase.singleton(context);
        clearNonUserEntries();
        firstTimeUserInitialize();
    }

    /**
     * Check if it's the user's first time logging in and set up the user if it is
     */
    private void firstTimeUserInitialize() {
        // runs only when db is empty
        if (db.personWithCoursesDAO().maxId() < 1) {
            //FIXME Replace with name from googleAccount
            String name = "Jimmy";

            //FIXME Implement Profile Pic
            byte[] profilePictureBytes = null;

            // Add the person to the Database
            Person userPerson = new Person(1, name, profilePictureBytes);
            db.personWithCoursesDAO().insert(userPerson);

        }
        updateUser();
    }

    /**
     * Update user from database
     */
    public void updateUser() {
        user = db.personWithCoursesDAO().get(1);
    }

    /**
     * Clear entries from the database
     */
    public void clearNonUserEntries() {
        db.personWithCoursesDAO().deleteNonUserCourses();
        db.personWithCoursesDAO().deleteNonUserPersons();
    }

    /**
     * Insert a person given name and pfp
     * @param name name
     * @param profilePic pfp
     */
    public void insertPerson(String name, byte[] profilePic) {
        db.personWithCoursesDAO().insert(new Person(db.personWithCoursesDAO().maxId() + 1, name, profilePic));
    }

    /**
     * Insert a course given course information
     * @param p the person to tie to the course
     * @param years list of years
     * @param quarters list of quarters
     * @param subjects list of subjects
     * @param courseNums list of courseNums
     */
    public void insertCourses(Person p, String[] years, String[] quarters, String[] subjects, String[] courseNums) {
        for (int i = 0; i < years.length; i++){
            db.courseEntryDAO().insert(new CourseEntry(db.courseEntryDAO().maxId() + 1,
                    p.personId,
                    years[i],
                    quarters[i],
                    subjects[i],
                    courseNums[i]
                    ));
        }
    }

    /**
     * Insert a person with courses from a person and their courses
     * @param p person
     */
    public void insertPersonWithCourses(Person p) {
        db.personWithCoursesDAO().insert(p);
    }

    /**
     * Insert a course entry
     * @param courseEntry the course entry
     */
    public void insertCourse(CourseEntry courseEntry) {
        db.courseEntryDAO().insert(courseEntry);
    }

    /**
     * Return a person with courses from a person
     * @param p person
     * @return person with courses
     */
    public PersonWithCourses getPersonWithCourses(Person p) {
        return db.personWithCoursesDAO().get(p.personId);
    }
}