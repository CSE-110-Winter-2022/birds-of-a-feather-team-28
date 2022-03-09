package com.example.bof_group_28.utility.classes.Prioritizers;

import java.util.List;

import model.db.CourseEntry;

public class RecentPrioritizer implements Prioritizer{
    private String year;
    private String quarter;

    public RecentPrioritizer(String currYear, String currQuar){
        this.year = currYear;
        this.quarter = currQuar;
    }

    @Override
    public double determineWeight(List<CourseEntry> sharedCourses) {
        double weight = 0;

        for(CourseEntry course: sharedCourses){

        }
        return 0;
    }
}
