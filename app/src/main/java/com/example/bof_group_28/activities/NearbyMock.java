package com.example.bof_group_28.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bof_group_28.R;
import com.example.bof_group_28.utility.classes.NearbyStudentsFinder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;

public class NearbyMock extends AppCompatActivity {

    public static final String COURSES_DELIMITER = ",";
    public static final int NUM_COURSE_FIELDS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_mock);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });
    }

    public void onSubmit() {

        UUID userUUID                   = convertUUID(findViewById(R.id.uuidInput));
        String userName                 = getStrFromEditText(findViewById(R.id.nameInput)).replaceAll(",", "");
        String userProfileImageURL      = getStrFromEditText(findViewById(R.id.profileImageURLInput)).replaceAll(",", "");

        List<CourseEntry> userCourses   = convertCourses(findViewById(R.id.coursesInput), userUUID);
        String waveInput                = getStrFromEditText(findViewById(R.id.waveInput)).replaceAll(",", "");

        PersonWithCourses mockPersonWithCourses = new PersonWithCourses();
        Person mockPerson                       = new Person(userUUID, userName, userProfileImageURL);

        mockPersonWithCourses.person        = mockPerson;
        mockPersonWithCourses.courseEntries = userCourses;

        NearbyStudentsFinder nearby = new NearbyStudentsFinder(getApplicationContext());
        nearby.getMessageListener().onFound(mockPersonWithCourses.toMessage());

    }

    UUID convertUUID(EditText uuidInput) {
        String uuid = getStrFromEditText(uuidInput).replaceAll(",", "");
        return UUID.fromString(uuid);
    }

    List<CourseEntry> convertCourses(EditText courseInput, UUID personUUID) {
        String coursesStr = getStrFromEditText(courseInput);
        String[] courseFields = coursesStr.split(COURSES_DELIMITER);

        List<CourseEntry> userCourses = new ArrayList<CourseEntry>();

        for (int i = 0; i < (courseFields.length / NUM_COURSE_FIELDS); i++) {
            UUID courseId = UUID.randomUUID();
            UUID personId = personUUID;
            String courseYear       = courseFields[(i*NUM_COURSE_FIELDS)];
            String courseQuarter    = courseFields[(i*NUM_COURSE_FIELDS) + 1];
            String courseSubject    = courseFields[(i*NUM_COURSE_FIELDS) + 2];
            String courseNum        = courseFields[(i*NUM_COURSE_FIELDS) + 3];
            String courseSize       = courseFields[(i*NUM_COURSE_FIELDS) + 4];

            CourseEntry curCourse = new CourseEntry(courseId, personId, courseYear,
                                                courseQuarter, courseSubject, courseNum, courseSize);

            userCourses.add(curCourse);
        }

        return userCourses;
    }

    String getStrFromEditText(EditText textInput) {
        return textInput.getText().toString();
    }


}