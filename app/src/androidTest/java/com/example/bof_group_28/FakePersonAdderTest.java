package com.example.bof_group_28;

import com.example.bof_group_28.utility.classes.FakePersonParser;

import org.junit.Test;

public class FakePersonAdderTest {
    @Test
    public void fakePersonParser() {
        FakePersonParser.addFakePersonToDatabaseFromString("a4ca50b6-941b-11ec-b909-0242ac120002,,,,\n" +
                "Bill,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "4b295157-ba31-4f9f-8401-5d85d9cf659a,wave,,,");
    }
}
