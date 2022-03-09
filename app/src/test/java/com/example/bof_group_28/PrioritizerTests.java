package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;

import com.example.bof_group_28.utility.classes.Prioritizers.DefaultPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.Prioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.RecentPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.SmallClassPrioritizer;
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

    }

    @Test
    public void testSmallClassPrio(){
        /**
        SizeName size1 = SizeName.HUGE;
        SizeName size2 = SizeName.HUGE;
        SizeName size3 = SizeName.HUGE;

        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", size1.));
        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", SizeName.LARGE.toString()));
        sharedCourses.add(new CourseEntry(2,1,"2022", "WI22", "CSE", "110", SizeName.getText));
        prio = new SmallClassPrioritizer();
        assertEquals(1.16, prio.determineWeight(sharedCourses), 0);*/
    }


}
