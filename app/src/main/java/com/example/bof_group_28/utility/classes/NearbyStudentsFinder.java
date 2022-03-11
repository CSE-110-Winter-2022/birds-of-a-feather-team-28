package com.example.bof_group_28.utility.classes;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.databaseHandler;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.bof_group_28.utility.interfaces.StudentFinder;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

import model.db.AppDatabase;
import model.db.CourseEntry;
import model.db.PersonWithCourses;

public class NearbyStudentsFinder implements StudentFinder {

    public static final String TAG = "BOF-TEAM-28";
    public static final String MSG_DELIMITER = ",";
    public static final int NUM_COURSE_FIELDS = 7;

    private MessageListener messageListener;

    List<PersonWithCourses> nearbyStudents;
    Context context;
    DatabaseHandler databaseHandler;

    public NearbyStudentsFinder(Context context){
        this.messageListener = createMessageListener();
        this.nearbyStudents = new ArrayList<PersonWithCourses>();
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

        AppDatabase db = AppDatabase.singleton(context);
        PersonWithCourses curUser = db.personWithCoursesDAO().get(1); // 1 is always the ID of the current user

        Message userDetailsMsg = curUser.toMessage();
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
        String messageContent = new String(message.getContent());
        String[] messageVals = messageContent.split(MSG_DELIMITER);

        int numCourses = Integer.parseInt(messageVals[0]);
        String studentName = messageVals[1];
        List<CourseEntry> studentCourses = new ArrayList<CourseEntry>();

        for (int i = 0; i < numCourses; i++) {

            // 2 is the offset, as the first element is the number of course and the second element is the student name
            // After that, we increment by one to get the next value for the course

            int courseId        = Integer.parseInt(messageVals[(i*NUM_COURSE_FIELDS)+3]);
            int personId        = Integer.parseInt(messageVals[(i*NUM_COURSE_FIELDS)+4]);
            String year         = messageVals[(i*NUM_COURSE_FIELDS)+5];
            String quarter      = messageVals[(i*NUM_COURSE_FIELDS)+6];
            String subject      = messageVals[(i*NUM_COURSE_FIELDS)+7];
            String courseNum    = messageVals[(i*NUM_COURSE_FIELDS)+8];
            String size         = messageVals[(i*NUM_COURSE_FIELDS)+9];

            CourseEntry course = new CourseEntry(courseId, personId, year, quarter, subject, courseNum, size);
            //FIXME fix course entry

            Log.d(TAG, course.toString());
            studentCourses.add(course);
        }

        PersonWithCourses student = new PersonWithCourses();

        instead add student to database and update accordingly

        if (!databaseHandler.databaseHasPerson(student)) {
            // If the person has never been seen by the database, add all their information
            for (CourseEntry course : p.getCourses()) {
                databaseHandler.insertCourse(course);
            }
            databaseHandler.insertPersonWithCourses(p.person);
        } else {
            // If the person is already in the database, update their information
            databaseHandler.deleteCourses(p.getId());
            for (CourseEntry course : p.getCourses()) {
                databaseHandler.insertCourse(course);
            }
            databaseHandler.updatePerson(p.getId(), p.getName(), p.getProfilePic());
        }

        student.person.name = studentName;
        student.courseEntries = studentCourses;

        return student;
    }
}
