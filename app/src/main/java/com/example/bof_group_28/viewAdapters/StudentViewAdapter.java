package com.example.bof_group_28.viewAdapters;

import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.PREF_NAME;
import static com.example.bof_group_28.activities.BirdsOfAFeatherActivity.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bof_group_28.activities.BirdsOfAFeatherActivity;
import com.example.bof_group_28.utility.classes.Converters;
import com.example.bof_group_28.utility.classes.NearbyStudentsHandler;
import com.example.bof_group_28.R;
import com.example.bof_group_28.activities.StudentSelectedActivity;

import java.util.List;
import java.util.Set;

import model.db.CourseEntry;
import model.db.PersonWithCourses;

/**
 * The student view adapter handles a view of nearby students with similar classes
 */
public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder> {

    public static final String PFP = "pfp";

    // Instance Variables
    public final List<PersonWithCourses> students;
    private final NearbyStudentsHandler handler;

    // Constants
    public static final String SELECTED_STUDENT_NAME = "selectedName";
    public static final String SELECTED_STUDENT_COURSES = "selectedCourses";

    /**
     * Construct a student view adapter with a nearby handler and list of students
     * @param students list of students to show
     * @param handler nearby handler
     */
    public StudentViewAdapter(List<PersonWithCourses> students, NearbyStudentsHandler handler) {
        super();
        this.handler = handler;
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.person_row, parent, false);

        return new ViewHolder(view, handler);
    }

    /**
     * Setup the person button and shared courses count
     * @param holder the holder
     * @param position the position of student to bind to in array
     */
    @Override
    public void onBindViewHolder(@NonNull StudentViewAdapter.ViewHolder holder, int position) {
        holder.setPersonButton(students.get(position));
        holder.setSharedCoursesCount(handler.getStudentClassMap().get(students.get(position)).size());
        holder.setProfilePicture(students.get(position).getProfilePic());
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    /**
     * Clear the student view adapter
     */
    public void clear(){
        int size = students.size();
        students.clear();
        notifyItemRangeRemoved(0,size);
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        private final Button personButton;
        private PersonWithCourses student;
        private final TextView classCount;
        private byte[] pfp;

        /**
         * Construct a Recycler View Item with onClick to select a student
         * @param itemView the item
         * @param handler the nearby handler
         */
        ViewHolder(View itemView, NearbyStudentsHandler handler) {
            super(itemView);

            // shared count
            this.classCount = itemView.findViewById(R.id.sharedClassesCount);

            // onclick to select a student
            this.personButton = itemView.findViewById(R.id.session_button);
            personButton.setOnClickListener((view) -> {
                Context c = view.getContext();

                Intent intent = new Intent(c, StudentSelectedActivity.class);
                SharedPreferences preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                // share their name as an extra
                editor.putString(SELECTED_STUDENT_NAME, student.getName());

                // get the shared courses as a set, and pass as an extra
                Set<String> sharedCourses = new ArraySet<>();
                if (handler.getStudentClassMap() != null && handler.getStudentClassMap().containsKey(student)) {
                    for (CourseEntry course : handler.getStudentClassMap().get(student)) {
                        sharedCourses.add(course.toString());
                    }
                } else {
                    Log.e(BirdsOfAFeatherActivity.TAG, "ERROR! Student button pressed for student that does not exist in map.");
                }
                editor.putStringSet(SELECTED_STUDENT_COURSES, sharedCourses);

                if (pfp != null) {
                    intent.putExtra(PFP, pfp);
                }

                editor.apply();

                view.getContext().startActivity(intent);
            });
        }

        public void setPersonButton(PersonWithCourses student) {
            this.student = student;
            this.personButton.setText(student.getName());
        }

        public void setSharedCoursesCount(int courses) {
            classCount.setText(courses + " shared courses.");
        }

        /**
         * Set the profile picture
         * @param pfp byte array
         */
        public void setProfilePicture(byte[] pfp) {
            if(pfp != null){
                this.pfp = pfp;
                Bitmap pfpBitmap = Converters.byteArrToBitmap(pfp);
                ((ImageView) itemView.findViewById(R.id.smallProfilePicture)).setImageBitmap(pfpBitmap);
            }
        }
    }
}
