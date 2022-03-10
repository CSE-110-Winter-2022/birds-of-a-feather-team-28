package com.example.bof_group_28.utility.classes.Prioritizers;

import com.example.bof_group_28.utility.enums.QuarterName;

import java.util.List;

import model.db.CourseEntry;

public class RecentPrioritizer implements Prioritizer{
    private int year;
    private int quarter;

    public RecentPrioritizer(String currYear, String currQuar){
        this.year = Integer.parseInt(currYear);

        if(currQuar.equals("Fall")){
            this.quarter = 1;
        } else if(currQuar.equals("Winter")){
            this.quarter = 2;
        } else if(currQuar.equals("Spring")){
            this.quarter = 3;
        } else{
            this.quarter = 4;
        }
    }

    @Override
    public double determineWeight(List<CourseEntry> sharedCourses) {
        double weight = 0;
        int tempAge;


        for(CourseEntry course: sharedCourses){
            tempAge = 4 *(this.year - Integer.parseInt(course.year));
            int tempSub = this.quarter - determineCourseQuarterInt(course.quarter);
            tempAge = tempAge + tempSub - 1;

            if(tempAge >= 4){
                weight = weight + 1;
            } else if(tempAge == 3){
                weight = weight + 2;
            } else if(tempAge == 2){
                weight = weight + 3;
            } else if(tempAge == 1){
                weight = weight + 4;
            } else if(tempAge == 0){
                weight = weight + 5;
            }
        }
        return weight;
    }


    public int determineCourseQuarterInt(String currQuar){
        if(currQuar.equals(QuarterName.FALL.getText())){
            return 1;//fall
        } else if(currQuar.equals(QuarterName.WINTER.getText())){
            return 2;//winter
        } else if(currQuar.equals(QuarterName.SPRING.getText())){
            return 3;//spring
        } else{
            return 4; //summer
        }
    }
}
