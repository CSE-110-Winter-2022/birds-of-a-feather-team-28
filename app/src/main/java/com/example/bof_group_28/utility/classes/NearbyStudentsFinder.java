package com.example.bof_group_28.utility.classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.bof_group_28.utility.interfaces.CourseEntry;
import com.example.bof_group_28.utility.interfaces.Person;
import com.example.bof_group_28.utility.interfaces.StudentFinder;
import com.example.bof_group_28.utility.services.NearbyStudentsService;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

import model.db.AppDatabase;

public class NearbyStudentsFinder implements StudentFinder {

    public static final String TAG = "BOF-TEAM-28";
    public static final String MSG_DELIMITER = ",";

    private MessageListener messageListener;

    List<Person> nearbyStudents;
    Context context;

    public NearbyStudentsFinder(Context context){
        this.messageListener = createMessageListener();
        this.nearbyStudents = new ArrayList<Person>();
        this.context = context;
    }

    @Override
    public List<Person> returnNearbyStudents() {
        return nearbyStudents;
    }

    @Override
    public void updateNearbyStudents() {
        Nearby.getMessagesClient(context).subscribe(messageListener);
    }

    // NOTE: Is this the correct way to read from the DB?
    public void publishToNearbyStudents() {
        /* Commenting out this code, as I'm not sure if this is the correct way to get Person from Room DB

        AppDatabase db;
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        Person curUser = ((Person) db.personWithCoursesDAO().get(1).person); // 1 is always the ID of the current user

        Message userDetailsMsg = ((DummyStudent) curUser).toMessage();
        Nearby.getMessagesClient(context).publish(userDetailsMsg);*/
    }

    @Override
    public int numNearbyStudents() {
        return nearbyStudents.size();
    }

    private MessageListener createMessageListener() {
        return new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                DummyStudent foundStudent = decodeMessage(message);
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

    public DummyStudent decodeMessage(Message message) {
        String messageContent = new String(message.getContent());
        String[] messageVals = messageContent.split(MSG_DELIMITER);

        int numCourses = Integer.parseInt(messageVals[0]);
        String studentName = messageVals[1];
        List<CourseEntry> studentCourses = new ArrayList<CourseEntry>();

        for (int i = 0; i < numCourses; i++) {

            // 2 is the offset, as the first element is the number of course and the second element is the student name
            // After that, we increment by one to get the next value for the course

            String year = messageVals[(i*4)+2];
            String quarter = messageVals[(i*4)+3];
            String subject = messageVals[(i*4)+4];
            String courseNum = messageVals[(i*4)+5];

            CourseEntry course = new DummyCourse(year, quarter, subject, courseNum);
            Log.d(TAG, course.toString());
            studentCourses.add(course);
        }

        DummyStudent student = new DummyStudent(studentName);
        student.courses = studentCourses;

        return student;
    }
}
