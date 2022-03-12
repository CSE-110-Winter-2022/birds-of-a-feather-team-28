package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;

import android.util.Log;

import com.example.bof_group_28.utility.enums.QuarterName;
import com.example.bof_group_28.utility.enums.SizeName;
import com.google.android.gms.nearby.messages.Message;

import java.util.UUID;

public class FakePersonParser {

    public static void addFakePersonToDatabaseFromString(String message, NearbyStudentsFinder finder) {
        StringBuilder builder = new StringBuilder();

        String[] fields = message.split(",,,,\n");
        Log.d(TAG, "Adding fake user to Nearby based on " + message);

        UUID fakeId = UUID.fromString(fields[0]);
        String name = fields[1];
        String profilePic = fields[2];

        System.out.println(name + "hi");

        String[] courses = fields[3].split("\n");

        int numCourses = 0;

        StringBuilder coursesString = new StringBuilder();

        for (String c : courses) {
            if (c.contains(",,,")) {
                Log.d(TAG, c + " parsing as a wave");
                String[] waveData = c.split(",");
                UUID wavedBy = UUID.fromString(waveData[0]);
                Log.d(TAG, "The mocked user received a wave from " + wavedBy.toString());

                // WAVE HERE!
            } else {
                numCourses++;
                Log.d(TAG, c + " parsing as a course");
                String[] courseData = c.split(",");
                String year = courseData[0];
                String quarterBad = courseData[1];
                String quarter = "";
                if (quarterBad.equals("FA")) {
                    quarter = QuarterName.FALL.getText();
                } else if (quarterBad.equals("WI")) {
                    quarter = QuarterName.WINTER.getText();
                } else if (quarterBad.equals("SP")) {
                    quarter = QuarterName.SPRING.getText();
                } else if (quarterBad.equals("SS1")) {
                    quarter = QuarterName.SUMMER_SESSION_ONE.getText();
                } else if (quarterBad.equals("SS2")) {
                    quarter = QuarterName.SUMMER_SESSION_TWO.getText();
                } else if (quarterBad.equals("SSS")) {
                    quarter = QuarterName.SPECIAL_SUMMER_SESSION.getText();
                }

                String subject = courseData[2];
                String courseNum = courseData[3];

                String sizeBad = courseData[4];
                String size = "";
                if (sizeBad.equals("Tiny")) {
                    size = SizeName.TINY.getText();
                } else if (sizeBad.equals("Small")) {
                    size = SizeName.SMALL.getText();
                } else if (sizeBad.equals("Medium")) {
                    size = SizeName.MEDIUM.getText();
                } else if (sizeBad.equals("Large")) {
                    size = SizeName.LARGE.getText();
                } else if (sizeBad.equals("Huge")) {
                    size = SizeName.HUGE.getText();
                } else if (sizeBad.equals("Gigantic")) {
                    size = SizeName.GIGANTIC.getText();
                }
                coursesString.append(UUID.randomUUID()).append(",")
                        .append(fakeId).append(",")
                        .append(year).append(",")
                        .append(quarter).append(",")
                        .append(subject).append(",")
                        .append(courseNum).append(",")
                        .append(size).append(",");
            }
        }


        builder.append(numCourses).append(",").append(name).append(",").append(fakeId).append(",").append(profilePic).append(",").append(coursesString);

        Log.d(TAG, "Faking string : " + builder.toString());

        finder.getMessageListener().onFound(new Message(builder.toString().getBytes()));

    }


}
