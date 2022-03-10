package com.example.bof_group_28.utility.classes;

import model.db.PersonWithCourses;

/**
 * Utility class for enabling sorting of shared course count
 */
public class PersonWithSharedCouseCount implements Comparable{

    private PersonWithCourses personWithCourses;
    private double weight;

    public PersonWithSharedCouseCount(PersonWithCourses personWithCourses, double weight) {
        this.personWithCourses = personWithCourses;
        this.weight = weight;
    }

    public PersonWithCourses getPersonWithCourses() {
        return personWithCourses;
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof PersonWithSharedCouseCount) {
            PersonWithSharedCouseCount pwc = (PersonWithSharedCouseCount) o;
            if (getWeight() == pwc.getWeight()) {
                return 0;
            }
            if (getWeight() > pwc.getWeight()) {
                return 1;
            }
            if (getWeight() < pwc.getWeight()) {
                return -1;
            }
        }
        return -2;
    }

}
