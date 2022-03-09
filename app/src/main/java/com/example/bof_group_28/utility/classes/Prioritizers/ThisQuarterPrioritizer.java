package com.example.bof_group_28.utility.classes.Prioritizers;

import java.util.List;

import model.db.CourseEntry;

public class ThisQuarterPrioritizer implements Prioritizer {
    private String year;
    private String quarter;

    public ThisQuarterPrioritizer(String currYear, String currQuar){
        this.year = currYear;
        this.quarter = currQuar;
    }
    @Override
    public double determineWeight(List<CourseEntry> sharedCourses) {
        double weight = 0;

        for(CourseEntry course: sharedCourses){
            if(course.year.equals(this.year) && course.quarter.equals(this.quarter)){
                weight++;
            }
        }

        return weight;
    }
}
