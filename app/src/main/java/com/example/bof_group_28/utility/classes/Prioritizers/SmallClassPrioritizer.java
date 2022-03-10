package com.example.bof_group_28.utility.classes.Prioritizers;

import com.example.bof_group_28.utility.enums.SizeName;

import java.util.List;

import model.db.CourseEntry;

public class SmallClassPrioritizer implements Prioritizer{
    @Override
    public double determineWeight(List<CourseEntry> sharedCourses) {
        double weight = 0;

        for(CourseEntry course: sharedCourses){
            if(course.size.equals(SizeName.TINY.getText())){
                weight = weight + 1;
            } else if(course.size.equals(SizeName.SMALL.getText())){
                weight = weight + .33;
            } else if(course.size.equals(SizeName.MEDIUM.getText())){
                weight = weight + .18;
            } else if(course.size.equals(SizeName.LARGE.getText())){
                weight = weight + .1;
            } else if(course.size.equals(SizeName.HUGE.getText())){
                weight = weight + .06;
            } else if(course.size.equals(SizeName.GIGANTIC.getText())){
                weight = weight + .03;
            }
        }

        return weight;
    }
}
