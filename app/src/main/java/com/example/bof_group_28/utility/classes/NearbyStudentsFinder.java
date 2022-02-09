package com.example.bof_group_28.utility.classes;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bof_group_28.utility.interfaces.CourseEntry;
import com.example.bof_group_28.utility.interfaces.Person;
import com.example.bof_group_28.utility.interfaces.StudentFinder;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;

import java.util.ArrayList;
import java.util.List;

public class NearbyStudentsFinder implements StudentFinder {

    public static final String TAG = "BOF-TEAM-28";

    private MessageListener messageListener;
    // TODO: Messages Client needed to broadcast messages
    // private MessagesClient messagesClient;

    List<Person> nearbyStudents;
    Context context;

    NearbyStudentsFinder(Context context){
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
        Nearby.getMessagesClient(this.context).subscribe(this.messageListener);
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

    public DummyStudent decodeMessage(Message message) {
        String messageContent = new String(message.getContent());
        String[] messageVals = messageContent.split(",");

        int numCourses = Integer.parseInt(messageVals[0]);
        String studentName = messageVals[1];
        List<CourseEntry> studentCourses = new ArrayList<CourseEntry>();

        Log.d(TAG, studentName + ",");

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
