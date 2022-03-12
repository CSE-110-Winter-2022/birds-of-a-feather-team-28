package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.bof_group_28.utility.enums.QuarterName;
import com.example.bof_group_28.utility.enums.SizeName;
import com.example.bof_group_28.utility.interfaces.StudentFinder;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;

public class NearbyStudentsFinder implements StudentFinder {

    public static final String TAG = "BOF-TEAM-28-NEARBY";
    public static final String MSG_DELIMITER = ",";
    public static final int NUM_COURSE_FIELDS = 7;

    private MessageListener messageListener;

    List<PersonWithCourses> nearbyStudents;
    Context context;

    public NearbyStudentsFinder(Context context) {
        this.messageListener = createMessageListener();
        this.nearbyStudents = new ArrayList<>();
        this.context = context;
    }

    @Override
    public List<PersonWithCourses> returnNearbyStudents() {
        return nearbyStudents;
    }

    @Override
    public void updateNearbyStudents() {
        Nearby.getMessagesClient(context).subscribe(messageListener);
    }

    // NOTE: Is this the correct way to read from the DB?
    public void publishToNearbyStudents() {

        Message userDetailsMsg = user.toMessage();
        Nearby.getMessagesClient(context).publish(userDetailsMsg);
    }

    @Override
    public int numNearbyStudents() {
        return nearbyStudents.size();
    }

    private MessageListener createMessageListener() {
        return new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                PersonWithCourses foundStudent = decodeMessage(message);
                Log.d(TAG, "Found student " + foundStudent.person);
                nearbyStudents.add(foundStudent);
            }

            @Override
            public void onLost(@NonNull Message message) {

            }
        };
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public PersonWithCourses decodeMessage(Message message) {
        Log.d(TAG, "Decoding message: " + message);
        String messageContent = new String(message.getContent());
        String[] messageVals = messageContent.split(MSG_DELIMITER);

        int numCourses = Integer.parseInt(messageVals[0]);
        String studentName = messageVals[1];
        UUID uuid = UUID.fromString(messageVals[2]);
        String profilePic = messageVals[3];

        if (databaseHandler.databaseHasUUID(uuid)) {
            databaseHandler.deleteCourses(uuid);
        }

        for (int i = 0; i < numCourses; i++) {

            // 4 is the offset, as the first element is the number of course and the second element is the student name
            // After that, we increment by one to get the next value for the course

            UUID courseId        = UUID.fromString(messageVals[(i*NUM_COURSE_FIELDS)+4]);
            UUID personId        = UUID.fromString(messageVals[(i*NUM_COURSE_FIELDS)+5]);
            String year         = messageVals[(i*NUM_COURSE_FIELDS)+6];
            String quarter      = messageVals[(i*NUM_COURSE_FIELDS)+7];
            String subject      = messageVals[(i*NUM_COURSE_FIELDS)+8];
            String courseNum    = messageVals[(i*NUM_COURSE_FIELDS)+9];
            String size         = messageVals[(i*NUM_COURSE_FIELDS)+10];

            databaseHandler.insertCourse(new CourseEntry(courseId, personId, year, quarter, subject, courseNum, size));

            Log.d(TAG, "Loaded: " + courseId + " " + personId + " " + year + " " + quarter + " " + subject + " " + courseNum + " " + size);
        }

        if (databaseHandler.databaseHasUUID(uuid)) {
            Log.d(TAG, "Updated Person: " + uuid + " " + studentName + " " + profilePic);
            databaseHandler.updatePerson(uuid, studentName, profilePic);
        } else {
            Log.d(TAG, "Inserted New Person: " + uuid + " " + studentName + " " + profilePic);
            databaseHandler.insertPersonWithCourses(new Person(uuid, studentName, profilePic));
        }

        return databaseHandler.getPersonFromUUID(uuid);
    }
}
