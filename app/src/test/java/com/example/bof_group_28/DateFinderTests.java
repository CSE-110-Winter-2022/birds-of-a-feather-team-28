package com.example.bof_group_28;

import static org.junit.Assert.assertEquals;

import com.example.bof_group_28.utility.classes.Calendar.DateFinder;
import com.example.bof_group_28.utility.enums.QuarterName;

import org.junit.Test;

public class DateFinderTests {

    @Test
    public void testCurrYear(){

        DateFinder finder = new DateFinder();
        String year = finder.getCurrYear();
        assertEquals("2022", year);
    }

    @Test
    public void testCurrQuarter(){
        DateFinder finder = new DateFinder();
        String quarter = finder.getCurrQuarter();
        System.out.println(QuarterName.WINTER);
        assertEquals(QuarterName.WINTER, quarter);
    }
}
