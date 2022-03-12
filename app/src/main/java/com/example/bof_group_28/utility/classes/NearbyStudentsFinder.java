package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.user;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.userId;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.bof_group_28.utility.enums.QuarterName;
import com.example.bof_group_28.utility.enums.SizeName;
import com.example.bof_group_28.utility.interfaces.StudentFinder;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.Person;
import model.db.PersonWithCourses;
import model.db.Wave;

public class NearbyStudentsFinder implements StudentFinder {

    public static final String TAG = "NEARBY-FUNC";
    public static final String MSG_DELIMITER = ",";
    public static final int NUM_COURSE_FIELDS = 7;
    public static final int SENDER_ID_POS = 0;
    public static final int RECEIVER_ID_POS = 1;
    public static final int WAVE_NUM_FIELDS = 2;

    private MessageListener messageListener;

    List<PersonWithCourses> nearbyStudents;
    Context context;

    public NearbyStudentsFinder(Context context) {
        this.messageListener = createMessageListener();
        this.nearbyStudents = new ArrayList<>();
        this.context = context;
        addFakeUser();
    }

    public void addFakeUser() {
        UUID personId = UUID.randomUUID();
        databaseHandler.insertCourse(new CourseEntry(UUID.randomUUID(), personId, "2021", QuarterName.FALL.getText(), "CSE", "101", SizeName.SMALL.getText()));
        databaseHandler.insertPersonWithCourses(new Person(personId, "Bob", "https://i.imgur.com/6ieINZP_d.webp?maxwidth=520&shape=thumb&fidelity=high"));
        nearbyStudents.add(databaseHandler.getPersonFromUUID(personId));


        UUID personId2 = UUID.randomUUID();
        databaseHandler.insertCourse(new CourseEntry(UUID.randomUUID(), personId2, "2021", QuarterName.WINTER.getText(), "CSE", "101", SizeName.GIGANTIC.getText()));
        databaseHandler.insertPersonWithCourses(new Person(personId2, "Bobette", "https://i.imgur.com/6ieINZP_d.webp?maxwidth=520&shape=thumb&fidelity=high"));
        nearbyStudents.add(databaseHandler.getPersonFromUUID(personId2));

        UUID personId3 = UUID.randomUUID();
        databaseHandler.insertCourse(new CourseEntry(UUID.randomUUID(), personId3, "2021", QuarterName.SPRING.getText(), "CSE", "101", SizeName.GIGANTIC.getText()));
        databaseHandler.insertPersonWithCourses(new Person(personId3, "Joe", "https://i.imgur.com/6ieINZP_d.webp?maxwidth=520&shape=thumb&fidelity=high"));
        nearbyStudents.add(databaseHandler.getPersonFromUUID(personId3));
    }

    @Override
    public List<PersonWithCourses> returnNearbyStudents() {
        return nearbyStudents;
    }

    @Override
    public void updateNearbyStudents() {
        Nearby.getMessagesClient(context).subscribe(messageListener);
        Log.d(TAG, "Subscribing to search for nearby students");
        Log.d(TAG, Nearby.getMessagesClient(context).getClass().getSimpleName());
    }

    public void publishToNearbyStudents() {
        Message userDetailsMsg = user.toMessage();
        Nearby.getMessagesClient(context).publish(userDetailsMsg);
        Log.d(TAG,"Publishing user profile to other students");
        Log.d(TAG, "Message content: " + userDetailsMsg.getContent());
        Log.d(TAG, Nearby.getMessagesClient(context).getClass().getSimpleName());
    }

    public void publishWave(UUID destinationStudent) {
        String wave = destinationStudent.toString() + "," + user.person.personId;
        Message waveMsg = new Message(wave.getBytes(StandardCharsets.UTF_8));
        Nearby.getMessagesClient(context).publish(waveMsg);

        Log.d(TAG, "Waving to UUID " + destinationStudent.toString());
        Log.d(TAG, "Message content: " + waveMsg.getContent());
        Log.d(TAG, Nearby.getMessagesClient(context).getClass().getSimpleName());
    }

    @Override
    public int numNearbyStudents() {
        return nearbyStudents.size();
    }

    private MessageListener createMessageListener() {
        return new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                String[] decodedMessage = decodeMessage(message);

                if (msgIsWave(decodedMessage)) {
                    Wave wave = strToWave(decodedMessage);
                    Log.d(TAG,"UUID " + wave.getSenderID() + " sent a wave to UUID " + wave.getReceiverID());
                    Log.d(TAG, "Wave content: " + message.getContent());
                    Log.d(TAG, Nearby.getMessagesClient(context).getClass().getSimpleName());

                    Toast.makeText(context, wave.getSenderID().toString() + " just waved to you!", Toast.LENGTH_SHORT);
                }

                else {
                    PersonWithCourses student = strToPersonWithCourses(decodedMessage);
                    Log.d(TAG, "Found student " + student.person.name);
                    Log.d(TAG, "Message content: " + message.getContent());
                    Log.d(TAG, Nearby.getMessagesClient(context).getClass().getSimpleName());

                    nearbyStudents.add(student);
                }
            }

            @Override
            public void onLost(@NonNull Message message) {

            }
        };
    }

    boolean msgIsWave(String[] msg) {
        return msg.length == WAVE_NUM_FIELDS;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public String[] decodeMessage(Message message) {
        Log.d(TAG, "Decoding message: " + message);
        String messageContent = new String(message.getContent());
        String[] messageVals = messageContent.split(MSG_DELIMITER);

        return messageVals;
    }

    public Wave strToWave(String[] messageVals) {
        UUID senderID   = UUID.fromString(messageVals[SENDER_ID_POS]);
        UUID receiverID = UUID.fromString(messageVals[RECEIVER_ID_POS]);

        return new Wave (senderID, receiverID);
    }

    public PersonWithCourses strToPersonWithCourses(String[] messageVals) {
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