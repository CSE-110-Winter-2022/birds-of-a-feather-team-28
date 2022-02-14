package com.example.bof_group_28.utility.classes;

import model.db.PersonWithCourses;

/**
 * Utility class for enabling sorting of shared course count
 */
public class PersonWithSharedCouseCount implements Comparable{

    private PersonWithCourses personWithCourses;
    private int count;

    public PersonWithSharedCouseCount(PersonWithCourses personWithCourses, int count) {
        this.personWithCourses = personWithCourses;
        this.count = count;
    }

    public PersonWithCourses getPersonWithCourses() {
        return personWithCourses;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof PersonWithSharedCouseCount) {
            PersonWithSharedCouseCount pwc = (PersonWithSharedCouseCount) o;
            if (getCount() == pwc.getCount()) {
                return 0;
            }
            if (getCount() > pwc.getCount()) {
                return 1;
            }
            if (getCount() < pwc.getCount()) {
                return -1;
            }
        }
        return -2;
    }

}
