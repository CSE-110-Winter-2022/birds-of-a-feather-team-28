package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;

import com.example.bof_group_28.utility.classes.Calendar.DateFinder;
import com.example.bof_group_28.utility.classes.Prioritizers.DefaultPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.Prioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.RecentPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.SmallClassPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.ThisQuarterPrioritizer;
import com.example.bof_group_28.utility.enums.SizeName;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import model.db.CourseEntry;

public class PrioritizerTests {
    private List<CourseEntry> sharedCourses;
    private Prioritizer prio;
    @Before
    public void setUp(){
        sharedCourses = new ArrayList<CourseEntry>();
    }

    @Test
    public void testDefaultPrio(){
        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", "Tiny"));
        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", "Tiny"));
        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", "Tiny"));
        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", "Tiny"));

        prio = new DefaultPrioritizer();
        double exp = 4;
        assertEquals(exp, prio.determineWeight(sharedCourses), 0);
    }

    @Test
    public void testRecentPrio(){


    }

    @Test
    public void testThisQuartPrio(){
        DateFinder df = new DateFinder();

        prio = new ThisQuarterPrioritizer(df.getCurrYear(), df.getCurrQuarter());

        sharedCourses.add(new CourseEntry(2,1,"2022", "Winter", "CSE", "110", SizeName.types()[1]));
        sharedCourses.add(new CourseEntry(2,1,"2022", "Winter", "CSE", "120", SizeName.types()[1]));
        sharedCourses.add(new CourseEntry(2,1,"2020", "Winter", "CSE", "110", SizeName.types()[1]));
        assertEquals(2, prio.determineWeight(sharedCourses), .01);

    }

    @Test
    public void testSmallClassPrio(){

        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", SizeName.types()[1]));
        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", SizeName.types()[4]));
        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", SizeName.types()[5]));
        prio = new SmallClassPrioritizer();
        assertEquals(1.16, prio.determineWeight(sharedCourses), .01);
    }


}
