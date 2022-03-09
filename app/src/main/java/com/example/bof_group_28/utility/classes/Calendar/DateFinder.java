package com.example.bof_group_28.utility.classes.Calendar;

import com.example.bof_group_28.utility.enums.QuarterName;

import java.util.Calendar;

public class DateFinder {
    private Calendar cal;
    public DateFinder(){
        cal = Calendar.getInstance();
    }

    public String getCurrQuarter(){
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        if((month >= 9 && month <= 10) || (month == 8 && day >= 20)||(month == 11 && day <= 11)){
            return QuarterName.FALL.name();
        }
        else if(month == 1 || (month == 0 && day >= 3) || (month == 2 && day <= 19)){
            return QuarterName.WINTER.name();
        }
        else if((month == 3 || month == 4) || (month == 2 && day >= 23) || (month == 5 && day <= 10))
            return QuarterName.SPRING.name();
        else if((month == 5 && day >=27) || (month == 6 && day <=29 )){
            return QuarterName.SUMMER_SESSION_ONE.name();
        }
        else if((month == 7) || (month == 8 && day <= 3)){
            return QuarterName.SUMMER_SESSION_TWO.name();
        }

        return null;
    }

    public String getCurrYear(){
        return "" + cal.get(Calendar.YEAR);
    }
}
