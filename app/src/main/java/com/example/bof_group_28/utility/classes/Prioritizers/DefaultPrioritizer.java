package com.example.bof_group_28.utility.classes.Prioritizers;

import java.util.List;

import model.db.CourseEntry;

public class DefaultPrioritizer implements Prioritizer{

    @Override
    public double determineWeight(List<CourseEntry> sharedCourses) {
        return sharedCourses.size();
    }
}
