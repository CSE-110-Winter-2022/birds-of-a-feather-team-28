package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;

import com.example.bof_group_28.utility.classes.Calendar.DateFinder;
import com.example.bof_group_28.utility.classes.Prioritizers.DefaultPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.Prioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.RecentPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.SmallClassPrioritizer;
import com.example.bof_group_28.utility.classes.Prioritizers.ThisQuarterPrioritizer;
import com.example.bof_group_28.utility.enums.QuarterName;
import com.example.bof_group_28.utility.enums.SizeName;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.db.CourseEntry;

public class PrioritizerTests {
    private List<CourseEntry> sharedCourses;
    private Prioritizer prio;
    private UUID classId1;
    private UUID classId2;
    private UUID classId3;
    private UUID classId4;
    private UUID studId;
    @Before
    public void setUp(){
        sharedCourses = new ArrayList<CourseEntry>();
        classId1 = UUID.randomUUID();
        classId2 = UUID.randomUUID();
        classId3 = UUID.randomUUID();
        classId4 = UUID.randomUUID();
        studId = UUID.randomUUID();
    }

    @Test
    public void testDefaultPrio(){

        sharedCourses.add(new CourseEntry(classId1, studId, "2022", "WI22", "CSE", "110", "Tiny"));
        sharedCourses.add(new CourseEntry(classId2,studId,"2022", "WI22", "CSE", "110", "Tiny"));
        sharedCourses.add(new CourseEntry(classId3,studId,"2022", "WI22", "CSE", "110", "Tiny"));
        sharedCourses.add(new CourseEntry(classId4,studId,"2022", "WI22", "CSE", "110", "Tiny"));

        prio = new DefaultPrioritizer();
        double exp = 4;
        assertEquals(exp, prio.determineWeight(sharedCourses), 0);
    }

    @Test
    public void testRecentPrio(){
        DateFinder df = new DateFinder();
        prio = new RecentPrioritizer(df.getCurrYear(), df.getCurrQuarter());
        sharedCourses.add(new CourseEntry(classId1,studId,"2022", QuarterName.types()[1], "CSE", "110", SizeName.types()[1]));
        sharedCourses.add(new CourseEntry(classId2,studId,"2021", QuarterName.types()[2], "CSE", "110", SizeName.types()[1]));

        assertEquals(7, prio.determineWeight(sharedCourses), .01);
    }

    @Test
    public void testThisQuartPrio(){
        DateFinder df = new DateFinder();

        prio = new ThisQuarterPrioritizer(df.getCurrYear(), df.getCurrQuarter());

        sharedCourses.add(new CourseEntry(classId1,studId,"2022", "Winter", "CSE", "110", SizeName.types()[1]));
        sharedCourses.add(new CourseEntry(classId2,studId,"2022", "Winter", "CSE", "120", SizeName.types()[1]));
        sharedCourses.add(new CourseEntry(classId3,studId,"2020", "Winter", "CSE", "110", SizeName.types()[1]));
        assertEquals(2, prio.determineWeight(sharedCourses), .01);

    }

    @Test
    public void testSmallClassPrio(){

        sharedCourses.add(new CourseEntry(classId1,studId,"2022", "WI22", "CSE", "110", SizeName.types()[1]));
        sharedCourses.add(new CourseEntry(classId2,studId,"2022", "WI22", "CSE", "110", SizeName.types()[4]));
        sharedCourses.add(new CourseEntry(classId3,studId,"2022", "WI22", "CSE", "110", SizeName.types()[5]));
        prio = new SmallClassPrioritizer();
        assertEquals(1.16, prio.determineWeight(sharedCourses), .01);
    }


}
